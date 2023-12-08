package cs3500.reversi.view;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReadonlyReversiModel;
import cs3500.reversi.model.ReversiModel;

/**
 * A HintsListener is any entity that wants to be notified whenever a mouse or key move by player for a game of reversi.
 */
public interface HintsListener {
  /**
   * Notify the listener (player) that a new move may happen, and provide the hints of possible points earn to player.
   * The move will be forwarded to the strategy to calculate the possible points.
   * @param m the move that corresponds to the last mouse or key move
   */
  int getPossiblePoints(ReadonlyReversiModel model, CellState player, Move m);
}
