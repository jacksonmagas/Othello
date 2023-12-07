package cs3500.reversi.strategy;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReversiModel;

/**
 * The basic minimax strategy does a game tree search to a given depth and chooses the move that
 * minimizes the best opponent response.
 */
public class BasicMinimaxStrategy implements InfallibleMoveStrategy {
  private final int depth;

  /**
   * Search for the best move to the given depth.
   * WARNING: On boards above size 4 depths of 3 or greater are likely to cause out of memory
   * exceptions. On boards of size 4 depths of 4 or greater may cause out of memory exceptions.
   * @param depth the depth to search the tree to
   */
  public BasicMinimaxStrategy(int depth) {
    this.depth = depth;
  }

  /**
   * Search for the best move to the given depth.
   * WARNING: On boards above size 4 depths of 3 or greater are likely to cause out of memory
   * exceptions. On boards of size 4 depths of 4 or greater may cause out of memory exceptions.
   */
  public BasicMinimaxStrategy() {
    this.depth = 3;
  }

  @Override
  public Move chooseMove(ReversiModel model, CellState player) {
    // potential optimization: start with the game tree from the last move and build from there
    return new GameTree(model, depth).getBestMove();
  }

  @Override
  public void newGUIMove(Move newMove) {
    // ignore
  }
}
