package cs3500.reversi.strategy;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReversiModel;

/**
 * A Strategy interface for choosing where to play next for the given player.
 */
public interface InfallibleMoveStrategy extends MoveStrategy {
  /**
   * Choose a move to make in the given model. This method is guaranteed to produce a move
   * @param model the reversi game to choose a move for
   * @param player Which player to try and make a move for
   * @return the move to make according to this strategy
   */
  Move chooseMove(ReversiModel model, CellState player);


}
