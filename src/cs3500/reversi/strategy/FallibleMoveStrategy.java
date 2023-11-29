package cs3500.reversi.strategy;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import java.util.Optional;

/**
 * An interface for a strategy that can possibly fail to produce a legal move.
 */
public interface FallibleMoveStrategy extends MoveStrategy {

  Optional<Move> chooseMove(ReversiModel model, CellState player);
}
