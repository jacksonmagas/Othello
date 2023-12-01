package cs3500.reversi.controller;

import cs3500.reversi.model.CellState;
import cs3500.reversi.view.MoveListener;

/**
 * Your Turn Listener implemented by Controllers interested in Your Turn events.
 * Implements MoveListener because the model needs to be able to let controllers know of
 * restarts from another view.
 */
public interface YourTurnListener extends MoveListener {
  /**
   * Returns the player associated with this feature listener.
   * @return The CellState of the player played by this listener
   */
  CellState getPlayer();

  /**
   * The yourTurn method should be called to notify this turn listener that it is time for it
   * to take its turn.
   * When this method is called the listener will interact with the model.
   */
  void yourTurn();

  /**
   * Refreshes all views on demand such as restart game event.
   */
  void refreshView();

  /**
   * End the turn of this player.
   */
  void endTurn();
}
