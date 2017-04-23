package checkers;

import checkers.game.Game;
import checkers.board.BoardCellColor;
import checkers.checker.CheckerColor;
import checkers.players.ComputerPlayer;
import checkers.players.HumanPlayer;
import checkers.players.PlayerSide;
import checkers.position.MovementSpeed;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import checkers.util.Vector2d;

import java.io.IOException;

/**
 * Created by Igorek on 09-Apr-17 at 8:12 AM.
 */
public class CheckersWindow extends Application {

    private Stage window;

    private CheckersSettings checkersSettings;

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
//                boardSizeInCells,
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
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        // TODO: place parameters into CheckersSettings
        Game game = new Game(
                gc,
                8,
                new HumanPlayer(PlayerSide.PLAYER_UP),
                new HumanPlayer(PlayerSide.PLAYER_DOWN),
                CheckerColor.WHITE,
                CheckerColor.BLACK,
                BoardCellColor.BROWN);

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

        // Start the loop
        animationTimer.start();
    }

//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
////        canvas.setWidth(CANVAS_SIZE);
////        canvas.setHeight(CANVAS_SIZE);
//    }

    private void terminate() {
        window.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
