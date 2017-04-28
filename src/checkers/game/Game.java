package checkers.game;

import checkers.CheckersSettings;
import checkers.board.Board;
import checkers.board.BoardCellColor;
import checkers.checker.CheckerColor;
import checkers.players.HumanPlayer;
import checkers.players.Player;
import checkers.players.PlayerSide;
import checkers.util.Pair;
import checkers.util.Vector2i;
import javafx.scene.canvas.GraphicsContext;
import checkers.util.Vector2d;

import java.util.function.Consumer;

/**
 * This class describes the game itself and its mechanics.
 * <p>
 * Created by Igor Boyarshin on April, 2017.
 */
public class Game {

    /**
     * The thing we will be drawing with on the canvas.
     */
    private final GraphicsContext gc;

    /**
     * The Player that plays for the UP side.
     */
    private final Player playerUp;

    /**
     * The Player that plays for the DOWN side.
     */
    private final Player playerDown;

    /**
     * The current Player(whose turn it is now).
     */
    private Player currentPlayer;

    /**
     * The color of the Checkers of the UP Player.
     */
    private final CheckerColor playerUpCheckerColor;

    /**
     * The color of the Checkers of the DOWN Player.
     */
    private final CheckerColor playerDownCheckerColor;

    /**
     * The board of the game that we will be playing on.
     */
    private final Board board;

    /**
     * The current state that the game is in.
     */
    private GameState currentGameState;

    /**
     * The time in seconds when the transition(Checker movement) started.
     * Is used to determine when to switch the GameState.
     * Transition should take exactly CheckersSettings.getInstance().movementSpeed.durationInSeconds seconds.
     */
    private double transitionTimeStart;

    /**
     * Whether the current Player has moved during his current turn.
     */
    private boolean playerHasMoved;

    /**
     * Whether the current Player has eaten any Checker during his current turn.
     */
    private boolean playerEatsThisTurn;

    /**
     * Whether we have checked the ability of the current Player to make any move during his current turn.
     */
    private boolean haveCheckedAbilityToMoveThisTurn;

    /**
     * The position(new) of the Checker that the current Player moved with last.
     */
    private Vector2i checkerOfLastMove;

    private Consumer<String> writeOnGameInfoLabel;

    /**
     * The constructor of the class. Initializes the fields and the board.
     *
     * @param gc                     the thing that we will be drawing will.
     * @param boardSizeInCells       the size of the board in cells.
     * @param playerUp               the Player that plays UP.
     * @param playerDown             the Player that plays DOWN.
     * @param playerUpCheckerColor   the color of the Checkers of the UP Player.
     * @param playerDownCheckerColor the color of the Checkers of the DOWN Player.
     * @param boardCellColor         the color scheme of the cells of the board.
     * @param writeOnGameInfoLabel   the function to write on GameInfo Label.
     */
    public Game(
            GraphicsContext gc,
            int boardSizeInCells,
            Player playerUp, Player playerDown,
            CheckerColor playerUpCheckerColor, CheckerColor playerDownCheckerColor,
            BoardCellColor boardCellColor,
            Consumer<String> writeOnGameInfoLabel) {

        this.gc = gc;
        this.playerUp = playerUp;
        this.playerDown = playerDown;
        this.playerUpCheckerColor = playerUpCheckerColor;
        this.playerDownCheckerColor = playerDownCheckerColor;
        this.writeOnGameInfoLabel = writeOnGameInfoLabel;

        this.board = new Board(
                boardSizeInCells,
                playerUpCheckerColor,
                playerDownCheckerColor,
                boardCellColor);

        this.currentPlayer = playerDown;

        this.currentGameState = GameState.PLAYER_TURN;
        System.out.println(currentPlayer.getPlayerSide().toString() + " move");

        this.transitionTimeStart = 0.0;

        this.playerHasMoved = false;
        this.playerEatsThisTurn = false;
        this.haveCheckedAbilityToMoveThisTurn = false;

        this.checkerOfLastMove = null;
    }

    /**
     * The constructor of the class. Initializes the fields and the board.
     *
     * @param gc                     the thing that we will be drawing will.
     * @param boardSizeInCells       the size of the board in cells.
     * @param boardRepresentation    the array representation of the board.
     * @param isPlayerDownTurn       whether it is currently PlayerDOwn turn.
     * @param playerUp               the Player that plays UP.
     * @param playerDown             the Player that plays DOWN.
     * @param playerUpCheckerColor   the color of the Checkers of the UP Player.
     * @param playerDownCheckerColor the color of the Checkers of the DOWN Player.
     * @param boardCellColor         the color scheme of the cells of the board.
     * @param writeOnGameInfoLabel   the function to write on GameInfo Label.
     */
    public Game(
            GraphicsContext gc,
            int boardSizeInCells,
            int[][] boardRepresentation,
            boolean isPlayerDownTurn,
            Player playerUp, Player playerDown,
            CheckerColor playerUpCheckerColor, CheckerColor playerDownCheckerColor,
            BoardCellColor boardCellColor,
            Consumer<String> writeOnGameInfoLabel) {

        this.gc = gc;
        this.playerUp = playerUp;
        this.playerDown = playerDown;
        this.playerUpCheckerColor = playerUpCheckerColor;
        this.playerDownCheckerColor = playerDownCheckerColor;
        this.writeOnGameInfoLabel = writeOnGameInfoLabel;

        this.board = new Board(
                boardSizeInCells,
                boardRepresentation,
                playerUpCheckerColor,
                playerDownCheckerColor,
                boardCellColor);

        this.currentPlayer = isPlayerDownTurn ? playerDown : playerUp;

        this.currentGameState = GameState.PLAYER_TURN;
        System.out.println(currentPlayer.getPlayerSide().toString() + " move");
        writeOnGameInfoLabel.accept(currentPlayer.getPlayerSide().toString() + " move");

        this.transitionTimeStart = 0.0;

        this.playerHasMoved = false;
        this.playerEatsThisTurn = false;
        this.haveCheckedAbilityToMoveThisTurn = false;

        this.checkerOfLastMove = null;
    }

    /**
     * Processes the mouse press event on the board.
     *
     * @param position the position where the press occurred. (0;0) is top left corner.
     */
    public void processMousePress(Vector2d position) {
        switch (currentGameState) {
            case PLAYER_TURN:
                if (currentPlayer.isHuman()) {
                    HumanPlayer humanPlayer = (HumanPlayer) currentPlayer;
                    final Vector2i newPosition = board.convertPositionToVector2i(position);

                    // Select a checker or make the move
                    if (!board.isCheckerSelected()) {
                        // Allow selecting a Checker only if:
                        // - this is the first move of the turn
                        // - not first => we're eating => have to eat with the same Checker
                        if (checkerOfLastMove == null || (checkerOfLastMove.equals(newPosition))) {
                            final boolean isCheckerNowSelected =
                                    board.selectChecker(currentPlayer.getPlayerSide(), newPosition);
                        }
                    } else {
                        // A checker is already selected, try to make a move now
                        final boolean moveValid = board.isMovePossibleWithSelectedChecker(newPosition);
                        if (moveValid) {
                            // Move
//                            board.moveSelectedChecker(newPosition);
                            humanPlayer.acceptMove(new Pair<>(board.getPositionOfSelectedChecker(), newPosition));
                        }

                        board.deselectChecker(); // will get selected at the update() method
                    }
                }
                break;
            case IDLE:

                break;
            case TRANSITION:

                break;
            default:
        }
    }

    /**
     * Updates the current state of the whole game and all of its components.
     *
     * @param secondsSinceStart seconds elapsed since the start of the game.
     */
    public void update(double secondsSinceStart) {
        switch (currentGameState) {
            case PLAYER_TURN:
                final Pair<Vector2i> move = currentPlayer.makeMove(
                        board::getBoardRepresentationAsArray,
                        getPlayerCodeInBoardRepresentation(currentPlayer),
                        board::canCheckerEat,
                        board::canCheckerMove
                );

                if (move == null) {
                    if (currentPlayer.isTurnFinished()) {
                        // transition
                        currentPlayer.confirmTurnFinished();
                        this.currentGameState = GameState.TRANSITION;

                        // Prepare for next turn
                        swapPlayers();
                        this.playerHasMoved = false;
                        this.playerEatsThisTurn = false;
                        this.haveCheckedAbilityToMoveThisTurn = false;
                        this.checkerOfLastMove = null;
                    } else { // Player hasn't finished his turn yet
                        if (currentPlayer.isHuman()) {
                            final HumanPlayer humanPlayer = (HumanPlayer) currentPlayer;
                            // A HumanPlayer can't finish his turn on his own except for using a button.

                            if (playerHasMoved) {
                                // There are three possible scenarios here:
                                // - just move. [already done that, so just finish the turn] {1}
                                // - eat, eat, eat, nothing to eat. [finish when nothing left ot eat] {2}
                                // - eat, eat, EndTurnButton press. [eating not mandatory => allow to finish] {3}

                                if (!playerEatsThisTurn) { // scenario {1}
                                    // Not allowed to make moves anymore => finish
                                    humanPlayer.forceTurnFinish();
                                } else { // player already ate this turn
                                    if (board.canCheckerEat(checkerOfLastMove)) { // scenario {3}
                                        if (CheckersSettings.getInstance().isEatingMandatory) {
                                            // Wait for Player to make his move
                                        } else {
                                            // Wait for Player to press the EndTurn button
                                        }
                                    } else { // can't eat now. scenario {2}
                                        // The Player has already eaten but has nothing to eat anymore => finish
                                        humanPlayer.forceTurnFinish();
                                    }
                                }
                            } else { // player hasn't moved yet
                                // Player doesn't make a move => maybe he can't => check it
                                if (!haveCheckedAbilityToMoveThisTurn) {
                                    haveCheckedAbilityToMoveThisTurn = true;
                                    final boolean ableToMoveThisTurn = board.canPlayerMakeAnyMove(currentPlayer.getPlayerSide());

                                    if (!ableToMoveThisTurn) {
                                        // TODO: maybe notify the user somehow that he can't move
                                        humanPlayer.forceTurnFinish();
                                    }
                                }

                                // otherwise nothing, he's just thinking, give him some time, DAMN IT!!!
                            }
                        } else { // Player is a ComputerPlayer
                            // A ComputerPlayer can figure out everything for himself and can finish his turn on his own
                            // (we will even believe him when he hasn't made a move but tells us that he has finished).

                            // So we don't need to do anything, just wait for Computer to inform us that he's finished.
                        }
                    }
                } else { // move != null
                    // Make the actual move
                    board.selectChecker(currentPlayer.getPlayerSide(), move.a);
                    final boolean ateSomeone = board.moveSelectedChecker(move.b);

                    checkerOfLastMove = move.b;
                    playerHasMoved = true;
                    playerEatsThisTurn = playerEatsThisTurn || ateSomeone;
                }

                break;
            case TRANSITION:
                if (board.getAmountOfCheckersOnBoard(currentPlayer.getPlayerSide()) == 0) {
                    // He has been beaten => game finished
                    this.currentGameState = GameState.IDLE;
                }

                // Just waits for the movement to end, then switches back to PLAYER_TURN

                if (transitionTimeStart == 0.0) { // We just got here
                    transitionTimeStart = secondsSinceStart;
                } else {
                    // TODO: mb speed up by not accessing the constant each frame
                    final double duration = CheckersSettings.getInstance().movementSpeed.durationInSeconds;
                    if (secondsSinceStart - transitionTimeStart > duration) {
                        // Transition has ended => switch the GameState
                        this.currentGameState = GameState.PLAYER_TURN;
                        this.transitionTimeStart = 0.0; // prepare for next transition

                        System.out.println(currentPlayer.getPlayerSide().toString() + " move");
                        writeOnGameInfoLabel.accept(currentPlayer.getPlayerSide().toString() + " move");
                    }
                }
                break;
            case IDLE:
//                currentGameState = GameState.PLAYER_TURN;

                break;
            default:
        }

        board.update(secondsSinceStart);
    }

    /**
     * Returns the representation of the board(its current state) as an array.
     * 0 == no Checker.
     * 1 == PLAYER_UP Checker
     * -1 == PLAYER_UP Checker queen
     * 2 == PLAYER_DOWN Checker
     * -2 == PLAYER_DOWN Checker queen
     *
     * @return the representation of the board(its current state) as an array.
     */
    public int[][] getBoardRepresentationAsArray() {
        return board.getBoardRepresentationAsArray();
    }

    /**
     * Returns whether it is currently PlayerDown's turn.
     *
     * @return whether it is currently PlayerDown's turn.
     */
    public boolean isPlayerDownTurn() {
        return currentPlayer == playerDown;
    }

    /**
     * Translates the PlayerSide into player code in board representation.
     *
     * @param player the Player to get the PlayerSide from.
     * @return the player code in board representation.
     */
    private int getPlayerCodeInBoardRepresentation(Player player) {
        final PlayerSide playerSide = player.getPlayerSide();
        switch (playerSide) {
            case PLAYER_DOWN:
                return Board.boardRepresentationPlayerDownCheckerCode;
            case PLAYER_UP:
                return Board.boardRepresentationPlayerUpCheckerCode;
            default:
                return 0;
        }
    }

    /**
     * Renders the game.
     *
     * @param secondsSinceStart seconds elapsed since the start of the game.
     */
    // TODO: consider removing the argument
    public void render(double secondsSinceStart) {
        board.draw(gc);
    }

    /**
     * Swaps the Players(the new current Player is the one who was idle during the last turn, and vice versa).
     */
    private void swapPlayers() {
        currentPlayer = (currentPlayer.equals(playerDown)) ? playerUp : playerDown;
    }

    /**
     * Returns whether the PlayerUp is a HumanPlayer.
     *
     * @return whether the PlayerUp is a HumanPlayer.
     */
    public boolean isPlayerUpHuman() {
        return playerUp.isHuman();
    }

    /**
     * Returns whether the PlayerDown is a HumanPlayer.
     *
     * @return whether the PlayerDown is a HumanPlayer.
     */
    public boolean isPlayerDownHuman() {
        return playerDown.isHuman();
    }
}
