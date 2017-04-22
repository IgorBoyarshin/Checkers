import game.Game;
import game.board.BoardCellColor;
import game.checker.CheckerColor;
import game.players.ComputerPlayer;
import game.players.HumanPlayer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.Vector2d;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Igorek on 09-Apr-17 at 8:12 AM.
 */
public class CheckersWindow extends Application {

    private Stage window;

    private final String WINDOW_TITLE = "Checkers";
    private final int WINDOW_HEIGHT = 600;
    private final int WINDOW_WIDTH = 900;

    private final double CANVAS_SIZE = 500.0;

    @Override
    public void start(Stage primaryStage) {
        // For easy reference
        this.window = primaryStage;

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            terminate();
            return;
        }

        window.setTitle(WINDOW_TITLE);
        window.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));

        // What to do when the user closes the window
        window.setOnCloseRequest((event) -> terminate());



        // Add a canvas to the scene
        final Canvas canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        ((BorderPane) root).setCenter(canvas);

        // Thing we will we drawing with
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        // TODO: place default parameters somewhere else
        Game game = new Game(
                gc,
                8,
                CANVAS_SIZE,
                new HumanPlayer(),
                new ComputerPlayer(),
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

    public void terminate() {
        window.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
