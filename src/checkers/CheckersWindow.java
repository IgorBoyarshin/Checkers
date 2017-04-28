package checkers;

import checkers.game.Game;
import checkers.board.BoardCellColor;
import checkers.checker.CheckerColor;
import checkers.players.ComputerPlayer;
import checkers.players.HumanPlayer;
import checkers.players.Player;
import checkers.players.PlayerSide;
import checkers.position.MovementSpeed;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import checkers.util.Vector2d;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * The main class of the whole game.
 * The game is started from here.
 * Creates the window, the game and listens for the input.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class CheckersWindow extends Application implements Initializable {

    /**
     * The reference to the game window.
     */
    private static Stage window = null;

    /**
     * The thing we will be drawing with.
     */
    private static GraphicsContext gc = null;

    /**
     * The CheckBox to select whether eating is mandatory.
     * Part of the rules of the game.
     */
    @FXML
    private CheckBox checkBoxIsEatingMandatory;

    /**
     * The ComboBox to select what type of Player the UP Player is.
     */
    @FXML
    private ComboBox comboBoxPlayerUp;

    /**
     * The ComboBox to select what type of Player the DOWN Player is.
     */
    @FXML
    private ComboBox comboBoxPlayerDown;

    /**
     * The String code for the ComputerPlayer.
     */
    // TODO: move as a static field into the Player class
    private static final String computerPlayerStringCode = "Computer";

    /**
     * The String code for the HumanPlayer.
     */
    // TODO: move as a static field into the Player class
    private static final String humanPlayerStringCode = "Human";

    /**
     * The ComboBox to select the size of the board in cells.
     */
    @FXML
    private ComboBox comboBoxBoardSize;

    /**
     * The supported board sizes in cells.
     */
    private static final int[] supportedBoardSizes = {8, 10};

    /**
     * The Label where info about the game rules is shown.
     */
    @FXML
    private Label labelRulesInfo;

    /**
     * The Label where into about the game is shown.
     */
    @FXML
    private Label labelGameInfo;

    /**
     * The settings of the whole game and window.
     */
    private static CheckersSettings checkersSettings = null;

    /**
     * The currently game instance being played.
     */
    private static Game game = null;

    /**
     * The method that gets called at the start of the application.
     *
     * @param primaryStage the window of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        // For easy reference
        this.window = primaryStage;

        // Set the default settings for the application
        setDefaultApplicationSettings();

        // Load the scene from the file
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            terminate();
            return;
        }

        window.setTitle(checkersSettings.windowTitle);
        window.setScene(new Scene(root, checkersSettings.windowWidth, checkersSettings.windowHeight));

        // What to do when the user closes the window
        window.setOnCloseRequest((event) -> terminate());

        // Add a canvas to the scene
        final Canvas canvas = new Canvas(checkersSettings.boardSizeInPixels, checkersSettings.boardSizeInPixels);
        ((BorderPane) root).setCenter(canvas);

        // Thing we will we drawing with
        gc = canvas.getGraphicsContext2D();

        // Process mouse events on the canvas
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED,
                mouseEvent -> {
                    final Vector2d position = new Vector2d(mouseEvent.getX(), mouseEvent.getY());
                    game.processMousePress(position);
                });

        // Main loop
        final long startNanoTime = System.nanoTime();
        final AnimationTimer animationTimer = new AnimationTimer() {
            final double ONE_BILLION = 1000000000.0;

            // Gets called 60 times per second
            public void handle(long currentNanoTime) {
                double secondsSinceStart = (currentNanoTime - startNanoTime) / ONE_BILLION;

                // TODO: consider keeping the gc as a local field in the Game class(set in the constructor)
                game.render(secondsSinceStart);
                game.update(secondsSinceStart);
            }
        };

        // Create, pack and display the window
        window.show();

        startDefaultGame();

        // Start the loop
        animationTimer.start();
    }

    /**
     * The method that gets called to initialize the view of the window.
     *
     * @param location  the URL location
     * @param resources the ResourceBundle
     */
    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the PlayerDown ComboBox
        comboBoxPlayerDown.getItems().addAll(new String[]{computerPlayerStringCode, humanPlayerStringCode});
        comboBoxPlayerDown.getSelectionModel().select(humanPlayerStringCode);
        comboBoxPlayerDown.promptTextProperty().setValue(humanPlayerStringCode);

        // Set up the PlayerUp ComboBox
        comboBoxPlayerUp.getItems().addAll(new String[]{computerPlayerStringCode, humanPlayerStringCode});
        comboBoxPlayerUp.getSelectionModel().select(humanPlayerStringCode);
        comboBoxPlayerUp.promptTextProperty().setValue(humanPlayerStringCode);

        // Set up the BoardSize ComboBox
        Arrays.stream(supportedBoardSizes).forEach(comboBoxBoardSize.getItems()::add);
        comboBoxBoardSize.promptTextProperty().setValue(supportedBoardSizes[0] + "");
        comboBoxBoardSize.getSelectionModel().select(0);
    }

    public void writeOnGameInfoLabel(String text) {
//        labelGameInfo.setText(text);
    }

    /**
     * Sets the default parameters for the CheckersSettings.
     */
    private void setDefaultApplicationSettings() {
        final int boardSizeInCells = 8;
        final double boardSizeInPixels = 500.0;
        final boolean isEatingMandatory = true;

        checkersSettings = new CheckersSettings(
                "Checkers",
                900,
                600,
                boardSizeInPixels,
                boardSizeInCells,
                boardSizeInPixels / boardSizeInCells,
                isEatingMandatory,
                MovementSpeed.FAST);
    }

    /**
     * Updates the application settings.
     * Essentially retrieves whether eating is mandatory from he view and leaves all other settings unchanged.
     */
    private void updateApplicationSettings(int newBoardSizeInCells, boolean isEatingMandatory) {
        checkersSettings = new CheckersSettings(
                checkersSettings.windowTitle,
                checkersSettings.windowWidth,
                checkersSettings.windowHeight,
                checkersSettings.boardSizeInPixels,
                newBoardSizeInCells,
                checkersSettings.boardSizeInPixels / newBoardSizeInCells,
                isEatingMandatory,
                checkersSettings.movementSpeed);
    }

    /**
     * Starts a new game with default settings.
     */
    // TODO: move default settings into another location
    private void startDefaultGame() {
        game = new Game(
                gc,
                checkersSettings.boardSizeInCells,
                new ComputerPlayer(PlayerSide.PLAYER_UP),
                new HumanPlayer(PlayerSide.PLAYER_DOWN),
                CheckerColor.WHITE,
                CheckerColor.BLACK,
                BoardCellColor.BROWN,
                this::writeOnGameInfoLabel);
    }

    /**
     * What to do when the NewGame button is pressed.
     */
    @FXML
    private void actionStartNewGame() {
        // Update the settings for the new game.
        final int newBoardSizeInCells = retrieveBoardSizeInCells();
        final boolean isEatingMandatory = retrieveIsEatingMandatory();
        updateApplicationSettings(newBoardSizeInCells, isEatingMandatory);

        // Set the game instance
        game = new Game(
                gc,
                newBoardSizeInCells,
                retrievePlayerUp(),
                retrievePlayerDown(),
                CheckerColor.WHITE,
                CheckerColor.BLACK,
                BoardCellColor.BROWN,
                this::writeOnGameInfoLabel);

        // Clear the information label
        labelRulesInfo.setText("");
    }

    /**
     * Retrieve whether the eating is mandatory from the view.
     *
     * @return whether the eating is mandatory.
     */
    private boolean retrieveIsEatingMandatory() {
        return checkBoxIsEatingMandatory.isSelected();
    }

    /**
     * Retrieves the size of the board in cells from the view.
     *
     * @return the size of the board in cells.
     */
    private int retrieveBoardSizeInCells() {
        return Integer.parseInt(comboBoxBoardSize.getSelectionModel().getSelectedItem().toString());
    }

    /**
     * Retrieves the DOWN Player from the view.
     *
     * @return the DOWN Player.
     */
    private Player retrievePlayerDown() {
        final String selectedPlayerDownString = comboBoxPlayerDown.getSelectionModel().getSelectedItem().toString();

        return createPlayerFromString(selectedPlayerDownString, PlayerSide.PLAYER_DOWN);
    }

    /**
     * Retrieves the UP Player from the view.
     *
     * @return the UP Player.
     */
    private Player retrievePlayerUp() {
        final String selectedPlayerDownString = comboBoxPlayerUp.getSelectionModel().getSelectedItem().toString();

        return createPlayerFromString(selectedPlayerDownString, PlayerSide.PLAYER_UP);
    }

    /**
     * Create a new Player based on its String code.
     *
     * @return the new Player created based on the String code.
     */
    private Player createPlayerFromString(String playerString, PlayerSide playerSide) {
        final Player player;

        switch (playerString) {
            case computerPlayerStringCode:
                player = new ComputerPlayer(playerSide);
                break;
            case humanPlayerStringCode:
                player = new HumanPlayer(playerSide);
                break;
            default:
                player = null;
        }

        return player;
    }

    /**
     * Displays a Label which tells the user that a Game restart is required in order for changes to take effect.
     */
    @FXML
    private void informAboutRequiredGameRestart() {
        final String requiredGameRestartText = "The changes will take effect once a new game is started";
        labelRulesInfo.setText(requiredGameRestartText);
    }

    /**
     * What to do end the user presses the EndTurn button.
     */
    @FXML
    private void actionEndTurn() {

    }

    /**
     * What to do when the user presses the SaveGame button.
     */
    @FXML
    private void actionSaveGame() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file to save");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.dir") + "\\saves\\")
        );

        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            try (PrintWriter out = new PrintWriter(new FileWriter("saves\\" + file.getName()))) {
                // print board size in cells
                out.println(checkersSettings.boardSizeInCells);

                // print if eating is mandatory
                out.println(checkersSettings.isEatingMandatory ? "yes" : "no");

                // print PlayerUp type
                out.println(game.isPlayerUpHuman() ? "human" : "computer");

                // print PlayerDown type
                out.println(game.isPlayerDownHuman() ? "human" : "computer");

                // print whose turn it is
                out.println(game.isPlayerDownTurn() ? "down" : "up");

                // print board
                final int[][] boardRepresentation = game.getBoardRepresentationAsArray();
                for (int column = 0; column < checkersSettings.boardSizeInCells; column++) {
                    for (int row = 0; row < checkersSettings.boardSizeInCells; row++) {
                        out.print(boardRepresentation[column][row] + " ");
                    }
                    out.println();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * * What to do when the user presses the LoadGame button.
     */
    @FXML
    private void actionLoadGame() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file to load");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.dir") + "\\saves\\")
        );

        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            try (BufferedReader in = new BufferedReader(new FileReader("saves\\" + file.getName()))) {
                // read board size in cells
                final int boardSizeInCells = Integer.parseInt(in.readLine());

                // read if eating is mandatory
                final boolean isEatingMandatory = in.readLine().equals("yes");

                // read PlayerUp type
                final String playerUpTypeCode =
                        in.readLine().equals("human") ? humanPlayerStringCode : computerPlayerStringCode;

                // read PlayerDown type
                final String playerDownTypeCode =
                        in.readLine().equals("human") ? humanPlayerStringCode : computerPlayerStringCode;

                // read whose turn it is
                final boolean isPlayerDownTurn = in.readLine().equals("down");

                // read board
                final int[][] boardRepresentation = new int[boardSizeInCells][boardSizeInCells];
                for (int column = 0; column < boardSizeInCells; column++) {
                    final String[] line = in.readLine().split(" ");
                    for (int row = 0; row < boardSizeInCells; row++) {
                        int code;
                        switch (line[row]) {
                            case "0":
                                code = 0;
                                break;
                            case "1":
                                code = 1;
                                break;
                            case "2":
                                code = 2;
                                break;
                            case "-1":
                                code = -1;
                                break;
                            case "-2":
                                code = -2;
                                break;
                            default:
                                code = 0;
                        }

                        boardRepresentation[column][row] = code;
                    }
                }

                // Update the application settings
                updateApplicationSettings(boardSizeInCells, isEatingMandatory);

                // Create a new Game instance
                game = new Game(
                        gc,
                        boardSizeInCells,
                        boardRepresentation,
                        isPlayerDownTurn,
                        createPlayerFromString(playerUpTypeCode, PlayerSide.PLAYER_UP),
                        createPlayerFromString(playerDownTypeCode, PlayerSide.PLAYER_DOWN),
                        CheckerColor.WHITE,
                        CheckerColor.BLACK,
                        BoardCellColor.BROWN,
                        this::writeOnGameInfoLabel);

                // Update the scene boxes now
                checkBoxIsEatingMandatory.setSelected(isEatingMandatory);

                comboBoxBoardSize.getSelectionModel().select(boardSizeInCells + "");
                comboBoxBoardSize.promptTextProperty().setValue(boardSizeInCells + "");

                comboBoxPlayerDown.getSelectionModel().select(playerDownTypeCode);
                comboBoxPlayerDown.promptTextProperty().setValue(playerDownTypeCode);

                comboBoxPlayerUp.getSelectionModel().select(playerUpTypeCode);
                comboBoxPlayerUp.promptTextProperty().setValue(playerUpTypeCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * * What to do when the user presses the About button.
     */
    @FXML
    private void actionAbout() {
        showAboutDialog();
    }

    /**
     * * Create the AboutMessage dialog window.
     */
    private void showAboutDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("About");
        String infoAbout = "			    							Welcome to the Checkers!												" +
                "\n" +
                "\n" +
                "\n" +
                "    	You are able to play in 3 following modes :" +
                "\n" +
                "        	1. Human vs Human" +
                "\n" +
                "        	2. Human vs Computer" +
                "\n" +
                "		3. Computer vs Computer" +
                "\n" +
                "\n" +
                "	You can also change the board size by switching between the " +
                "\n" +
                "	fields of 'Board size' option which is located on the right side" +
                "\n" +
                "        of the application window; 2 choices are available - 8x8 and 10x10" +
                "\n" +
                "\n" +
                "	You also may choose whether mandatory eating option is turned on -" +
                "\n" +
                "	click on the checkbox 'Mandatory eating' in the left side of the" +
                "\n" +
                "	application window" +
                "\n" +
                "\n" +
                "\n" +
                "@Download related projects (Chess, Gomoku, Go) from our website" +
                "\n" +
                " http://www.TopDicksAndPussies.Efficient-And-Beautiful-Code.html" +
                "\n" +
                " You may also rate our app on the website and leave there your comment" +
                "\n" +
                "\n" +
                "@Project sponsored by Google, Coca-Cola, Facebook and many other" +
                "\n" +
                " very mega top companies" +
                "\n" +
                "\n" +
                "@Created on 15 April, 2017 by very megaTopDick Igor Boyarshin";

        dialog.setHeaderText(infoAbout);
        dialog.showAndWait();
    }

    /**
     * Terminate the window instance. Finished the game.
     */
    private void terminate() {
        window.close();
    }

    /**
     * The main method.
     *
     * @param args the arguments of the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
