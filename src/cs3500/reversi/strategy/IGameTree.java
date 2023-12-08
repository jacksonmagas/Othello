package cs3500.reversi.strategy;

import cs3500.reversi.model.Move;

/**
 * An interface for a game tree for any game whose moves can be represented by Move.
 * This interface is primarily intended for the game of reversi, and enables a minimax
 * tree search to find the best move.
 */
public interface IGameTree {
  /**
   * Get the expected result if both players play optimally according to this game tree as a
   * differential between number of black pieces and white pieces.
   * <p>
   * For example a tree with an expected result of 5 black pieces and 3 white would return an
   * expected result of +3 while a tree with an expected result of 10 black pieces and 20 white
   * pieces would return an expected result of -10.
   * </p>
   * @return the expected result
   */
  int getExpectedResult();

  /**
   * Determines whether the current IGameTree has any dependent siblings.
   * @return true if the current tree has a sibling
   */
  boolean isLeaf();

  /**
   * Get the move with the highest expected value for the current player.
   * @return the best move
   */
  Move getBestMove();

  /**
   * Get the next node of the current game tree if it has one.
   * @return the next node
   */
  IGameTree getNext();

  /**
   * Get the move that lead to this game tree. If there is no recorded move return null.
   * @return the move that lead to this specific game tree
   */
  Move getLastMove();
}
