package cs3500.reversi.controller;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;

/**
 * Your Turn Listerner implemented by Controllers interested in Your Turn events
 */
public interface YourTurnListener {
  CellState getPlayer();
  void yourTurn();
  void refreshView();
}
