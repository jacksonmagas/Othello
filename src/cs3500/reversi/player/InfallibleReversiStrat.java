package cs3500.reversi.player;

import cs3500.reversi.model.IROModel;
import cs3500.reversi.model.PlayerDisc;

/**
 * The interface {@code InfallibleReversiStrat} represents a strategy for playing Reversi that
 * never fails to find a move. It is the opposite of the interface {@code FallibleReversiStrat} as
 * it returns an enum of either {@code PlayerMoves.MAKE_MOVE} or {@code PlayerMoves.SKIP_TURN}.
 * With this interface, the player must make some sort of move.
 */
public interface InfallibleReversiStrat {

  /**
   * Chooses a move for the given player in the given model.
   * @param model the model to choose a move in
   * @param who the player to choose a move for
   * @return either {@code PlayerMoves.MAKE_MOVE} or {@code PlayerMoves.SKIP_TURN}. If
   *        {@code PlayerMoves.MAKE_MOVE} is returned, then the selected hex of the move is
   *        stored in {@code PlayerMoves.MAKE_MOVE.getSelectedHex()}. If
   *        {@code PlayerMoves.SKIP_TURN} is returned, then the player is advised to skip their
   *        turn.
   */
  public PlayerMoves chooseMove(IROModel model, PlayerDisc who);
}
