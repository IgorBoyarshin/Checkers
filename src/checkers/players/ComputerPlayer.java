package checkers.players;

/**
 * Created by Igorek on 10-Apr-17 at 9:22 PM.
 */
public class ComputerPlayer extends Player {

    private static final long THINKING_TIME_MILLIS = 1000;

    public ComputerPlayer(PlayerSide playerSide) {
        super(playerSide);
    }

    @Override
    public void makeMove() {

    }

    @Override
    public boolean isHuman() {
        return false;
    }
}
