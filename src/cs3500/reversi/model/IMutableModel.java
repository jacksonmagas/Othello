package cs3500.reversi.model;

/**
 * <p>The {@code IMutableModel} represents a mutable model of the game cs3500.reversi.Reversi.
 * This model allows three movements to control a game of cs3500.reversi.Reversi.
 * It can either start a game of cs3500.reversi.Reversi (if it hasn't been started yet).
 * Once the game has started, this model allows making a move
 * (ensuring that it is the correct player's turn making the move and
 * the move is both legal and valid), and it can skip turns to the next player.</p>
 *
 * <p>Any specific moves that control the state of the game (such as capturing a disc,
 * checking if a move is legal, getting the neighbors of a cell that a player
 * hopes to play a disc on) are implemented in the classes that implement this
 * interface to allow more flexibility of different versions of the
 * game and to avoid unnecessary coupling or mutations.</p>
 *
 * <p>This interface extends {@code IROModel} and is able to access any of its methods.</p>
 */
public interface IMutableModel extends IROModel {

  /**
   * <p>This method officially starts a game of cs3500.reversi.Reversi.
   * It initializes a grid of Hex's for this cs3500.reversi.Reversi model.
   * The first turn is always given to the player with disc Black. </p>
   *
   * <p>This method should have no side effects other than configuring this model
   * instance, initializing the board, and updating the status of the game to
   * {@code Status.Playing}.</p>
   *
   * @param initializeGrid represents whether the grid should be initialized. If
   *                       a board is initialized, its initial pieces (altnernative discs around
   *                       the center of the board) are placed. If this is disabled, the game
   *                       can be started with a custom arranged board.
   * @throws IllegalStateException if the game has already started.
   * @throws IllegalArgumentException if the radius is negative.
   */
  public void startGame(boolean initializeGrid);

  /**
   * <p>Allows the player to make a move if they are able to make a move. This method will
   * automatically skip the player's turn if there are no legal moves remaining for the
   * player to make. If there are legal moves remaining, this method allows the player
   * to play a disc on the given coordinate.</p>
   * <p>EFFECT: if the move is legal, this method resets the number of consecutive passes
   * (i.e., number of times a player skips their turn) to 0.</p>
   * <p>EFFECT: if the move is legal, this method changes the turn to the next player.</p>
   * <p>EFFECT: once the player plays a disc, it updates the status accordingly.</p>
   * <p>EFFECT: if there are no moves remaining for the player whose turn it is now, this method
   * keeps track of the number of consecutive passes by adding one to the count.</p>
   * <p>EFFECT: if there are no moves remaining for the player whose turn it is now, this method
   * updates the status accordingly.</p>
   * @param coordQ represents the q-coordinate that the disc will be placed on.
   * @param coordR represents the R-coordinate that the disc will be placed on.
   * @param who represents the player who is placing the disc.
   * @throws IllegalStateException if the game is over.
   * @throws IllegalStateException if it isn't {@code who}'s turn.
   * @throws IllegalStateException if the requested hex has been taken.
   * @throws IllegalStateException if the game hasn't been started yet.
   * @throws IndexOutOfBoundsException if the requested coordinate does not exist.
   */
  public void makeMove(int coordQ, int coordR, PlayerDisc who);

  /**
   * <p>Passes the turn to the next player.</p>
   * <p>EFFECT: keeps track of the number of times players have skipped their turns
   * consecutively.</p>
   * <p>EFFECT: updates the status accordingly.</p>
   * <p>Preserves the invariant that the legal number of passes
   * cannot exceed the number of players.</p>
   * @throws IllegalStateException if the number of consecutive passes exceeds the number of
   *                               players.
   * @throws IllegalStateException if the game hasn't been started yet.
   * @throws IllegalStateException if the game is over.
   */
  public void skipTurn(PlayerDisc who);

  /**
   * <p>Notifies the appropriate controller that the player of that controller is the current
   * player to make their turn.</p>
   */
  public void notifyPlayerTurn(PlayerDisc player);

  /**
   * <p>Notifies all feature listeners that the game is over.</p>
   */
  public void notifyGameOver(String winner);
}
