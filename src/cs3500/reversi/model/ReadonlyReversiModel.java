package cs3500.reversi.model;

import java.util.List;
import java.util.Optional;

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
  CellState getStateAt(int r, int c);

  /**
   * Get a list of the legal moves for this model.
   * The list is ordered so that the topmost leftmost moves come first.
   * @return the legal moves for this model.
   */
  List<Move> getLegalMoves();

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
   * Notify all controllers that the state of the model has changed,
   * and they should refresh their views.
   */
  void notifyStateChanged();

  /**
   * Get the winner of the game if one exists.
   * @return An optional containing the winner's CellState, or an empty optional if there is
   *         no winner.
   */
  Optional<CellState> getWinner();

  /**
   * Get the status of the game, which is either NotStarted, Playing, Won, or Tied.
   * @return The current status of the game
   */
  Status getStatus();

  /**
   * Retrieve current player hints enable mode.
   * @return true if hints are enabled for this player
   */
  boolean isPlayerHintsEnabled(CellState player);

  /**
   * Get the type of board that this model is being played on.
   */
  BoardType getBoardType();
}
