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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import checkers.util.Vector2d;

import java.io.IOException;
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
    private Stage window = null;

    /**
     * The thing we will be drawing with.
     */
    private GraphicsContext gc = null;

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
    private static final String comboBoxComputerPlayer = "Computer";

    /**
     * The String code for the HumanPlayer.
     */
    // TODO: move as a static field into the Player class
    private static final String comboBoxHumanPlayer = "Human";

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
    private CheckersSettings checkersSettings = null;

    /**
     * The currently game instance being played.
     */
    private Game game = null;

    /**
     * The method that gets called at the start of the application.
     *
     * @param primaryStage the window of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        // For easy reference
        this.window = primaryStage;

        final int boardSizeInCells = 8;
        final double boardSizeInPixels = 500.0;
        this.checkersSettings = new CheckersSettings(
                "Checkers",
                900,
                600,
                boardSizeInPixels,
                boardSizeInPixels / boardSizeInCells,
                true,
                MovementSpeed.FAST);

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
        this.gc = canvas.getGraphicsContext2D();

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
        comboBoxPlayerDown.promptTextProperty().setValue(comboBoxHumanPlayer);
        comboBoxPlayerDown.getSelectionModel().select(comboBoxHumanPlayer);
        comboBoxPlayerDown.getItems().addAll(new String[]{comboBoxComputerPlayer, comboBoxHumanPlayer});

        // Set up the PlayerUp ComboBox
        comboBoxPlayerUp.promptTextProperty().setValue(comboBoxHumanPlayer);
        comboBoxPlayerUp.getSelectionModel().select(comboBoxHumanPlayer);
        comboBoxPlayerUp.getItems().addAll(new String[]{comboBoxComputerPlayer, comboBoxHumanPlayer});

        // Set up the BoardSize ComboBox
        Arrays.stream(supportedBoardSizes).forEach(comboBoxBoardSize.getItems()::add);
        comboBoxBoardSize.promptTextProperty().setValue(supportedBoardSizes[0] + "");
        comboBoxBoardSize.getSelectionModel().select(0);
    }

    /**
     * Starts a new game with default settings.
     */
    // TODO: move default settings into another location
    private void startDefaultGame() {
        this.game = new Game(
                gc,
                8,
                new HumanPlayer(PlayerSide.PLAYER_UP),
                new HumanPlayer(PlayerSide.PLAYER_DOWN),
                CheckerColor.WHITE,
                CheckerColor.BLACK,
                BoardCellColor.BROWN);
    }

    /**
     * What to do when the NewGame button is pressed.
     */
    @FXML
    private void actionStartNewGame() {
        this.game = new Game(
                gc,
                8,
                retrievePlayerUp(),
                retrievePlayerDown(),
                CheckerColor.WHITE,
                CheckerColor.BLACK,
                BoardCellColor.BROWN);

        // Clear the information label
        labelRulesInfo.setText("");
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
            case comboBoxComputerPlayer:
                player = new ComputerPlayer(playerSide);
                break;
            case comboBoxHumanPlayer:
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
     * What to do when the user presses the SaveGame button
     */
    @FXML
    private void actionSaveGame() {

    }

    /**
     * * What to do when the user presses the LoadGame button
     */
    @FXML
    private void actionLoadGame() {

    }

    /**
     * * What to do when the user presses the About button
     */
    @FXML
    private void actionAbout() {

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
