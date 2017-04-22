package game;

/**
 * Created by Igorek on 22-Apr-17 at 3:31 PM.
 */
public enum GameState {
    /**
     * A player is thinking about his move, do nothing here, just wait.
     */
    PLAYER_TURN,

    /**
     * State when one player has made his move, but the other player's move hasn't started yet.
     * Animate checker's movement here.
     */
    TRANSITION,

    /**
     * No game is being played, display some animation meanwhile.
     */
    IDLE;
}
