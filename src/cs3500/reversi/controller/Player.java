package cs3500.reversi.controller;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.Move;

/**
 * A simple Player interface
 */
public interface Player {
  Move play(ReversiModel model);
  CellState getPiece();
}
