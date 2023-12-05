package cs3500.reversi.provider.controller;


import cs3500.reversi.provider.player.IPlayer;
import cs3500.reversi.provider.view.IGraphicalReversiView;


/**
 * The interface {@code IFeatures} represents the features that the controller can perform on
 * the view. It is used to connect the controller to the view, so that the view can call the
 * controller when something interesting happens (e.g., a high-level event triggered by a low-level
 * one).
 */
public interface IViewFeatures {

  /**
   * Skips the turn of the current player. This method is called when the user clicks on the
   * "s" key on their keyboard. If the move is not legal, then this method catches the
   * {@code IllegalStateException} and communicates with the view to display the appropriate
   * error message.
   * EFFECT: changes the current player turn to the other player, without making any moves or
   * changes to the board.
   */
  void skipTurn();

  /**
   * Places a disc on the board. This method is called when the user clicks on the "Enter" key
   * on their keyboard. If the move is not legal, then this method catches the
   * {@code IllegalStateException} and communicates with the view to display the appropriate
   * error message.
   * EFFECT: this method places a disc on the board, if the move is legal. If the move is not
   * legal, then this method does nothing. Also, this method changes the current player turn to the
   * other player.
   *
   * @param selectedHexQ the Q-coord of the hex that the user clicked on
   * @param selectedHexR the R-coord of the hex that the user clicked on
   *
   */
  void placeDisc(int selectedHexQ, int selectedHexR);

  /**
   * Connects this controller to the given view by adding this controller
   * as a listener to the given view.
   * EFFECT: reassigns the view of this controller to the given view.
   *
   * @param v the view to be set
   */
  void setView(IGraphicalReversiView v);

  /**
   * Sets the player of this controller to the given player. This player can either be a human
   * or a machine. This player will be the one interacting with the controller to choose the next
   * action.
   * EFFECT: reassigns the player of this controller to the given player.
   *
   * @param player the player to be set
   */
  void setPlayer(IPlayer player);
}
