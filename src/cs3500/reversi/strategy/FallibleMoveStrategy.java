package cs3500.reversi.strategy;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import java.util.Optional;

/**
 * An interface for a strategy that can possibly fail to produce a legal move.
 */
public interface FallibleMoveStrategy extends MoveStrategy {
  /**
   * Choose a move to make in the given model. This method may fail to produce a move.
   * @param model the reversi game to choose a move for
   * @param player Which player to try and make a move for
   * @return An optional containing either the move or an empty optional if this strategy produced
   *         no move
   */
  Optional<Move> chooseMove(ReversiModel model, CellState player);
}
