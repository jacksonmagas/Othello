package cs3500.reversi.strategy;

import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReadonlyReversiModel;
import cs3500.reversi.model.ReversiModel;
import java.util.function.Function;

/**
 * A class to allow testing of a move or sequence of moves on a reversi model without
 * modifying the model. The moves are given an integer score based on the evaluation function
 * passed into this model.
 */
public class ParameterizedMoveTester implements MoveTester {
  ReadonlyReversiModel baseModel;
  Function<ReadonlyReversiModel, Integer> evalFunc;

  /**
   * Create a new move tester to test moves on the given Reversi Model with the given evaluation
   * function.
   * @param baseModel The ReversiModel to test moves on
   * @param evalFunc A function from ReversiModel to integer which scores moves based
   *                 on their effectiveness
   */
  public ParameterizedMoveTester(ReadonlyReversiModel baseModel,
      Function<ReadonlyReversiModel, Integer> evalFunc) {
    this.baseModel = baseModel;
    this.evalFunc = evalFunc;
  }

  @Override
  public int testMove(Move move) throws IllegalArgumentException {
    ReversiModel testModel = baseModel.copy();
    testModel.makeMove(move);
    return evalFunc.apply(testModel);
  }
}
