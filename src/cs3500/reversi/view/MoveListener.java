package cs3500.reversi.view;

import cs3500.reversi.model.Move;

/**
 * A MoveListener is any entity that wants to be notified whenever a click results in a valid
 * action for a game of reversi.
 */
public interface MoveListener {
  /**
   * Notify the listener that a new move has been made, and provide the move.
   * The move will be forwarded to the strategy to use or ignore.
   * @param m the move that corresponds to the last click
   */
  void receiveGUIAction(Move m);
}
