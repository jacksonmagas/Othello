package cs3500.reversi.strategy;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;

public class HighestScoringMove implements InfallibleMoveStrategy {
  /**
   * Chooses the move that produces the highest score on the following turn.
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
