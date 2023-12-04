package cs3500.reversi.controller;

import cs3500.reversi.model.IMutableModel;
import cs3500.reversi.model.PlayerDisc;

/**
 * The interface {@code IModelFeatures} represents the features that the model can perform.
 * In the context of the model, features represents the various notifications that a model can
 * publish to the controller. These notifications can include information such as whose
 * turn it is in the game. The interface is used to connect the model to the controller,
 * so that the controller can call the model to perform the various features.
 */
public interface IModelFeatures {

  /**
   * Handles the player with the given {@code PlayerDisc}'s turn. Classes implementing the
   * {@code IModelFeatures} will carry a field to determine the {@code PlayerDisc} they represent.
   * To handle a player's turn, this method will check with the model to see if the current player's
   * turn corresponds with the {@code PlayerDisc} the controller carries as a field. If it is the
   * player's turn, then this method will communicate with the {@code IPlayer} to get the
   * player's next action. Based on the player's action, this method will also update the view
   * to the recent state of the model.
   */
  void handlePlayerTurn(PlayerDisc player);

  /**
   * Connects the model of this controller to the given model by adding this controller
   * as a listener to the given model. This method carries the mutable
   * version of the model to allow the controller to call the model to perform various
   * actions that would change the actual state of the model.
   * EFFECT: reassigns the model of this controller to the given model.
   *
   * @param m the model to be set
   */
  void setModel(IMutableModel m);

  /**
   * Gets called by the model when the game is over in the model. Handles game over by interacting
   * with the views that telling them to display that the game is over. If there is a winner,
   * this method will display the winner. If there is a tie, this method will display that there
   * is a tie.
   */
  void handleGameOver(String winner);
}
