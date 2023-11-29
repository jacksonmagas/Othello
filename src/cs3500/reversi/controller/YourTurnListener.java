package cs3500.reversi.controller;

import cs3500.reversi.model.CellState;

/**
 * Your Turn Listerner implemented by Controllers interested in Your Turn events.
 */
public interface YourTurnListener {

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
   * Refresh the view.
   */
  void refreshView();
}
