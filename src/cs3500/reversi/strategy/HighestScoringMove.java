package cs3500.reversi.strategy;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReadonlyReversiModel;
import cs3500.reversi.model.ReversiModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Function;

public class HighestScoringMove implements InfallibleMoveStrategy {

  /**
   * An implementation of move tester which evaluates moves based on the score for the player
   * making the move. (Which is the opposite of the current player after the move is made.)
   */
  private static class ScoreTester implements MoveTester {
    private final MoveTester delegate;
    private ScoreTester(ReversiModel model) {
      this.delegate = new ParameterizedMoveTester(model,
          (ReadonlyReversiModel m) -> m.getPlayerScore(m.getCurrentPlayer().opposite()));
    }

    @Override
    public int testMove(Move move) throws IllegalArgumentException {
      return delegate.testMove(move);
    }
  }

  /**
   * Chooses the move that produces the highest score on the following turn.
   */
  @Override
  public Move chooseMove(ReversiModel model, CellState player) {
    ScoreTester tester = new ScoreTester(model);
    Comparator<Move> compareScore = Comparator.comparingInt(tester::testMove);
    return Collections.max(model.getLegalMoves(), compareScore);
  }
}
