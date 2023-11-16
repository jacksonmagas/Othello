package cs3500.reversi.model;

import java.util.List;

/**
 * Represents the read-only model interface for playing a game of Reversi.
 */
public interface ReadonlyReversiModel {

  /**
   * Return the score of the given player either 0 or 1, which is the sum
   * of the values of the disc cards.
   * @return the score
   */
  int getPlayerScore(int playerNum);

  /**
   * Checks if the game is over.
   * @return True if the game is over.
   */
  boolean isGameOver();

  /**
   * Returns a copy of the game board.
   * @return a list containing each row of the game board with the state of each cell in the row.
   */
  List<List<CellState>> getGameBoard();

  int[][] getBoard();

  /**
   * Returns the state of the cell at the given horizontal row and index.
   * The state is either empty, occupied by black, or occupied by white.
   * @return The state of the cell
   */
  CellState getTileAt(int hRow, int hIndex);

  /**
   * Returns the side length of the board.
   * @return the length of one side of the board.
   */
  int sideLength();

  /**
   * Determines if the current player has any legal moves.
   * @return True if there are legal moves for the current player.
   */
  boolean anyLegalMoves();

  Cell.Location getFirstAvailableMove();

  String getNextStepInstructions();

  String getLastErrorMessage();

  CellState getPieceAt(int r, int c);

  /**
   * Create a read/write copy of the read only model.
   * @return A copy of the model
   */
  ReversiModel copy();
}
