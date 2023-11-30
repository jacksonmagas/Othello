package cs3500.reversi.strategy;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;

/**
 * The basic minimax strategy does a game tree search to a depth of 2 and chooses the move that
 * minimizes the best opponent response.
 */
public class BasicMinimaxStrategy implements InfallibleMoveStrategy {
  @Override
  public Move chooseMove(ReversiModel model, CellState player) {
    return new GameTree(model, 2).getBestMove();
  }

  @Override
  public void newGUIMove(Move newMove) {
    // ignore
  }
}
