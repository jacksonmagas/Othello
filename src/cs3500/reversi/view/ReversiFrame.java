package cs3500.reversi.view;

import java.awt.event.KeyListener;

/**
 * Interface ReversiFrame creates frame for Reversi game. Sets the mouse/key listeners, view
 * and size for the game.
 */
public interface ReversiFrame {
  /**
   * Adds a feature listener to the reversiFrame.
   */
  void addMoveListener(MoveListener features);

  /**
   * Redraws the view.
   */
  void repaint();

  /**
   * Display the given message as an error.
   * @param message the message to display
   */
  void displayErrorMessage(String message);

  /**
   * Make the view visible or invisible.
   * @param visible A boolean representing whether the view should be visible
   */
  void setVisibleView(boolean visible);

  /**
   * This method will be called to set a key listener.
   */
  void setKeyListener(KeyListener listener);

  /**
   * Set the highlighted cell to be the given row and column.
   * @param row the row of the cell
   * @param col the column of the cell
   */
  void setHighlightedCell(int row, int col);
}
