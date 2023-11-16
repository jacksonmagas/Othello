package cs3500.reversi.strategy;

/**
 * A move tester is able to evaluate a move or sequence of moves and give each move a score.
 */
public interface MoveTester {
  /**
   * Tests the given move according to the evaluation function of the move tester and gives it a
   * score for how good the move is.
   * @param move The move to evaluate
   * @throws IllegalArgumentException if the move is not legal according to this move tester.
   * @return The score given to the move
   */
  int testMove(Move move) throws IllegalArgumentException;

  /**
   * Tests the result of the given sequence of moves according to the evaluation function of the
   * move tester and gives it a score for how good the move is.
   * @param moves the moves to make in order
   * @throws IllegalArgumentException if any of the moves are not legal
   * @return The score given to the move
   */
  int testMoveSequence(Move... moves) throws IllegalArgumentException;
}
