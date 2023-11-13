package cs3500.reversi.strategy;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;

/**
 * A Strategy: find the first (topmost-leftmost) available spot
 */
public class FirstAvailableOpening implements MoveStrategy {

  /**
   * Chooses a move for computer player.
   */
  @Override
  public Move chooseMove(ReversiModel model, CellState player) {
    Cell.Location location = model.getFirstAvailableMove();
    Move move;
    if (location != null) {
      move = new Move(location.getRow(), location.getColumn());
    } else {
      // no valid move remaining hence pass-turn
      move = new Move(true);
    }
    return move;
  }
}
