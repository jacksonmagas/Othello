package cs3500.reversi.view;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReadonlyReversiModel;

/**
 * A HintsListener is any entity that wants to be notified whenever a mouse or key move by player for a game of reversi.
 */
public interface HintsListener {
  /**
   * Notify the listener (player) that a new move may happen, and provide the hints of possible points earn to player.
   * The move will be forwarded to the player to calculate the possible scoring points
   * i.e. possible coins that can be flipped.
   * @param m the move that corresponds to the last mouse or key move
   */
  int getPossiblePoints(ReadonlyReversiModel model, CellState player, Move m);
}
