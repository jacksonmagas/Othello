package cs3500.reversi.provider.model;

import java.util.List;
import java.util.Optional;

import cs3500.reversi.provider.controller.IModelFeatures;

/**
 * <p>The {@code IROModel} interface represents the read-only view of a Reversi game.
 * Reversi is played on a pointy-top orientation hex grid using an axial coordinate system.
 * Implementing classes provide methods for querying the game state without allowing
 * modifications to the game board (i.e., players can observe the state of the Reversi game
 * through this interface without altering the ongoing gameplay).</p>
 *
 * <p>In the Reversi game, players take turns to place their discs on the grid.
 * Discs are placed in empty cells in a way that enclose the opponent's discs
 * in a straight line. Enclosed opponent's discs are then flipped or captured to the
 * current player's color. The game ends when the grid is fully occupied,
 * no valid moves are possible, or both players pass their turns.
 * The player with the most discs of their color on the grid at the end of the game wins.
 *
 * <p>Implementing classes must ensure thread safety when providing access to the game state.</p>
 */
public interface IROModel {

  /**
   * Is the game at the current state over?
   * A game of cs3500.reversi.Reversi is over if either of these conditions apply:
   * 1) Both players have passed their turns.
   * 2) The board is full (physically no more moves left).
   * @return {@code true} if the game is over or {@code false} if the game
   *         is still ongoing.
   * @throws IllegalStateException if the game hasn't started yet.
   */
  public boolean isGameOver();

  /**
   * Gets the score of the given player.
   * A score in the game of Reversi represents the number of discs a Player has placed
   * on the board.
   * @param p represents the score of the given player.
   * @return the number of discs the player has placed on the board.
   * @throws IllegalStateException if the game hasn't started yet.
   */
  public int getScore(PlayerDisc p);

  /**
   * Gets the winner of the game.
   * @return the Player who has won the game.
   * @throws IllegalStateException if {@code getStatus() != Status.Won}
   * @throws IllegalStateException if the game hasn't started yet.
   * */
  public PlayerDisc getWinner();

  /**
   * Gets the status of the game.
   * @return the current status.
   * @throws IllegalStateException if the game hasn't started yet.
   */
  public Status getStatus();

  /**
   * Gets the radius of the grid.
   * @return the radius.
   * @throws IllegalStateException if the game hasn't started yet.
   */
  public int getRadius();

  /**
   * Checks if the board is full. For a board to not be full, there must be at least one empty hex.
   * @return {@code true} if the board is full, {@code false} otherwise.
   * @throws IllegalStateException if the game hasn't started yet.
   */
  public boolean isBoardFull();

  /**
   * Retrieves a copy of the Board for this game of cs3500.reversi.Reversi.
   * @return the Board.
   * @throws IllegalStateException if the game hasn't started yet.
   */
  public Board getBoard();

  /**
   * Retrieves the player whose turn it is now.
   * @return the playerDisc of that player.
   * @throws IllegalStateException if the game hasn't started yet.
   * @throws IllegalStateException if the next player disc results in empty.
   */
  public PlayerDisc getNextPlayerTurn();

  /**
   * Counts the number of discs that a player has on the board.
   * @param playerDisc represents the player in question.
   * @return int represents the number of discs that a player has on the board.
   * @throws IllegalStateException if the game hasn't started yet.
   */
  public int countDiscs(PlayerDisc playerDisc);

  /**
   * Retrieves the PlayerDisc of the hex at a given hex coordinate.
   * @param coordQ represents the q-coordinate of the hex.
   * @param coordR represents the r-coordinate of the hex.
   * @return the relevant PlayerDisc.
   */
  public PlayerDisc getDiscAt(int coordQ, int coordR);

  /**
   * Counts the number of legal moves left for {@code this.nextPlayerDisc} current turn.
   * @return the number of legal moves left for the player whose turn it is now.
   * @throws IllegalStateException if next player disc is empty.
   */
  public int getNumOfLegalMovesForCurrentPlayer();

  /**
   * <p>Checks hows many valid paths there are. If there are none, a player cannot place a disc
   * on startHex and the move is considered illegal. If there is at least one path, a player can
   * place a disc on startHex and the move is considered legal.</p>
   *
   * <p>A play is legal for player A if the disc being played (startHex) is adjacent
   * (in at least one direction) to a straight line of the opponent player B’s discs,
   * at the far end of which is another player A disc. Adjacency means that two cells
   * share an edge (i.e., it is not enough touch merely at a corner).
   * An additional criterion for a play to be legal is that startHex must be empty.</p>
   * The result of this method will be used for {@code capture}</p>
   *
   * @param q represents the q-coord of the cell that a player could potentially play a disc on.
   * @param r represents the r-coord of the cell that a player could potentially play a disc on.
   * @return {@code true} if the play is legal or {@code false} otherwise.
   * @throws IllegalStateException if startHex is not within the bounds of this game.
   */
  public boolean isLegal(int q, int r);

  /**
   * <p>This method makes a copy of the list of neighbors that surround the hex with the given
   * coordinates. All hexes within this list are in bounds (placed within the grid).
   * Information regarding the neighbors of a certain hex was made public as it contains important
   * information about the model: one core verb of our model is that the model ought to
   * know who its neighbors are. By publicizing this information, this method can find relevant
   * info about a hex’s neighbors that can help in planning strategic game plays. </p>
   * @param q the q-coordinate of the hex whose neighbors we want to retrieve.
   * @param r the r-coordinate of the hex whose neighbors we want to retrieve.
   * @return a list of hexes that are neighbors of the hex with the given coordinates.
   */
  public List<Hex> getInboundNeighbors(int q, int r);

  /**
   * creates a copy of the list returned from the method allValidPaths, which stores all the
   * discs that are going to be flipped if the player makes a move at the given hex.
   * @param startHex represents the cell that a player could potentially play a disc on.
   * @return {@code List<List<Hex>>} represents a list of all the valid paths surrounding startHex.
   */
  public List<List<Hex>> getAllValidPaths(Hex startHex);

  /**
   * Gets the latest move that was made in this Reversi game. It can return an empty
   * optional if no moves have been made yet.
   */
  public Optional<Hex> getLatestMove();

  /**
   * Adds the given features to the list of features that this model can perform.
   * @param features the features to be added to this model.
   */
  void addFeatures(IModelFeatures features);

  /**
   * Creates a copy of a new model that has the same state as the current model.
   * @param currentModel the current model.
   * @return a copy of the current model as a mutatable model (this is to ensure that the copy
   *         of the model could be started).
   */
  IMutableModel getPreviousModel(IROModel currentModel);

  /**
   * Determines if the game has started yet.
   * @return {@code true} if the game has started, {@code false} otherwise.
   */
  boolean isGameStarted();
}
