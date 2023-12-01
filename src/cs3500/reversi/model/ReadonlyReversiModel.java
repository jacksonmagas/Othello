package cs3500.reversi.model;

import java.util.List;

/**
 * Represents the read-only model interface for playing a game of Reversi.
 */
public interface ReadonlyReversiModel {
  /**
   * Return the score of the player with the given cell state.
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

  /**
   * Gets the directions for the next step of the game at any given time.
   * The directions include telling the players whose turn it is and whether the game is over
   * or they should make a move.
   * @return A string containing instructions for the players to play the next move
   */
  String getNextStepInstructions();

  /**
   * Returns the error message corresponding to the last illegal move that was attempted.
   * Views can use this information to inform users of illegal moves.
   * @return The error message
   */
  String getLastErrorMessage();

  /**
   * Get the state of the piece at the given location.
   * @param r the row of the piece
   * @param c the column of the piece
   * @return the state of the piece at that row
   */
  CellState getPieceAt(int r, int c);

  /**
   * Get a list of the legal moves for this model.
   * The list is ordered so that the topmost leftmost moves come first.
   * @return the legal moves for this model.
   */
  List<Move> getLegalMoves();

  /*
  TODO: reorganize listeners from controller to view and have controller listen to player actions
  this will allow removal of model interacting with highlighted cell
  */
  /**
   * Get the location of the current active cell in the model.
   * @return the location of the highlighted cell
   */
  Cell.Location getHighlightedCell();

  /**
   * Create a read/write copy of the read only model.
   * @return A copy of the model
   */
  ReversiModel copy();


  /**
   * Get the number of columns in the given row if the row exists in this game.
   * @param row the row whose length to get
   * @return the length of the row
   */
  int getColumns(int row);

  /**
   * Get the total number of rows in the board for this game.
   * @return the total number of rows
   */
  int getRows();

  /**
   * Refreshes all views of the game.
   */
  void refreshAllViews();
}
