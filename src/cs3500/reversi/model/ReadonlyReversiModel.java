package cs3500.reversi.model;

import cs3500.reversi.strategy.Move;
import java.util.List;

/**
 * Represents the read-only model interface for playing a game of Reversi.
 */
public interface ReadonlyReversiModel {
  /**
   * Return the score of the player with the given cell state
   * @return the score
   */
  int getPlayerScore(CellState player);

  /**
   * Get the cell state of the current player.
   * @return the cell state of the player to move
   */
  CellState getCurrentPlayer();

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

  /**
   * Create a new reversi model of the same type in the new game configuration.
   */
  ReversiModel newGame();

  /**
   * Get a list of the legal moves for this model.
   * The list is ordered so that the topmost leftmost moves come first.
   * @return the legal moves for this model.
   */
  List<Move> getLegalMoves();

  //TODO
  Cell.Location getHighlightedCell();
}
