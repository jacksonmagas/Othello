package cs3500.reversi.strategy;

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

  /**
   * Make the given move in the given model handling passed turns.
   * @throws IllegalArgumentException if the move is not legal for the model.
   */
  private void makeMove(ReversiModel model, Move move) throws IllegalArgumentException {
    if (move.isPassTurn()) {
      model.passTurn();
    } else {
      model.makeMove(move.getPosn().row, move.getPosn().col);
    }
  }

  @Override
  public int testMove(Move move) throws IllegalArgumentException {
    ReversiModel testModel = baseModel.copy();
    makeMove(testModel, move);
    return evalFunc.apply(testModel);
  }
}
