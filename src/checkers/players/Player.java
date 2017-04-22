package checkers.players;

/**
 * Created by Igorek on 10-Apr-17 at 9:21 PM.
 */
public abstract class Player {

    private final PlayerSide playerSide;

    public Player(PlayerSide playerSide) {

        this.playerSide = playerSide;
    }

    public abstract void makeMove();

    public abstract boolean isHuman();

    public PlayerSide getPlayerSide() {
        return playerSide;
    }
}
