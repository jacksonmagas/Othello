package cs3500.reversi.controller;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.Move;
import cs3500.reversi.view.MoveListener;

/**
 * A player is an actor that can play moves on a model using a particular piece.
 * Players can also get notified of moves from the GUI.
 */
public interface Player extends MoveListener {
  /**
   * The play method returns the move this player will choose according to their strategy.
   * @param model the model to try and play a move on
   * @return the move this player chooses according to their strategy
   */
  Move play(ReversiModel model);

  /**
   * Gets the CellState that represents the piece used by the current player in this game.
   * Used to tell whether this player is playing for white or black.
   * @return the CellState this player is using
   */
  CellState getPiece();
}
