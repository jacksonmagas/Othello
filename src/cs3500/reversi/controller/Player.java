package cs3500.reversi.controller;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.Move;

/**
 * A simple Player interface.
 */
public interface Player {

  Move play(ReversiModel model);

  CellState getPiece();

  /**
   * Notify the listener that a new move has been made, and provide the move.
   * The move will be forwarded to the strategy to use or ignore.
   * @param m the move that corresponds to the last click
   */
  void recieveGUIMove(Move m);
}
