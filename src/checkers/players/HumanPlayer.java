package checkers.players;

/**
 * Created by Igorek on 10-Apr-17 at 9:22 PM.
 */
public class HumanPlayer extends Player {
    public HumanPlayer(PlayerSide playerSide) {
        super(playerSide);
    }

    @Override
    public void makeMove() {

    }

    @Override
    public boolean isHuman() {
        return true;
    }
}
