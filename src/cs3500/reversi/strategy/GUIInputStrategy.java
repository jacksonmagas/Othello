package cs3500.reversi.strategy;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;

/**
 *
 */
public class GUIInputStrategy implements InfallibleMoveStrategy {
  Move move;
  volatile boolean newMove;

  @Override
  public void newGUIMove(Move newMove) {
    this.newMove = true;
    move = newMove;
  }

  /**
   * Chooses the move for player using gui clicks.
   */
  @Override
  public Move chooseMove(ReversiModel model, CellState player) {
    // wait for a new move from the GUI
    while (!newMove) {
      Thread.onSpinWait();
    }
    newMove = false;
    return move;
  }
}
