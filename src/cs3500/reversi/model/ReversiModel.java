package cs3500.reversi.model;

/**
 * Represents the primary model interface for playing a game of Reversi.
 * Moves in this model are made using the coordinate system (row, index) where the row is the
 * horizontal row number 0 indexed from top and the index is the location in the row 0
 * indexed from left.
 */
public interface ReversiModel extends ReadonlyReversiModel {

  /**
   * Allow current player to make move disc to given row and column if allowable by the rules
   * of the game.
   * @param row the 0-based indexed from top i.e. the horizontal row number
   * @param column the 0-based indexed from left i.e. vertical column number
   * @throws IllegalArgumentException if either input parameters are invalid
   * @throws IllegalStateException if the move is not allowable or if game is over!
   */
  void makeMove(int row, int column);

  /**
   * Allow current player to pass the move.
   * If both players does their own passes then game will be over.
   * @throws IllegalStateException if game is over!
   */
  void passTurn();

  /**
   * Create a new reversi model with the same board state as the current reversi model.
   * @return A copy of the current model
   */
  ReversiModel copy();
}
