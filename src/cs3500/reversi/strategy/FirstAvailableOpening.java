package cs3500.reversi.strategy;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReversiModel;

/**
 * A Strategy: find the first (topmost-leftmost) available spot.
 */
public class FirstAvailableOpening implements InfallibleMoveStrategy {

  /**
   * Chooses the first move for computer player.
   */
  @Override
  public Move chooseMove(ReversiModel model, CellState player) {
    return model.getLegalMoves().get(0);
  }

  @Override
  public void newGUIMove(Move newMove) {
    //ignore
  }
}
