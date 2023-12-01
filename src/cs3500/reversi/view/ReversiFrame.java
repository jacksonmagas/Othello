package cs3500.reversi.view;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.util.HashMap;

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
   * This method will be called when the drawing area has changed.
   */
  void setCanvasSize(int width, int height);

  /**
   * Redraws the view.
   */
  void repaint();

  /**
   * Get a map from points on the GUI to the corresponding logical coordinates for the game.
   * @return The map from point to coordinate
   */
  HashMap<Point, Point> getMap();

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
   * This method will be called to set focus for keyboard support.
   */
  void setFocusable(boolean focus);

  /**
   * Set the highlighted cell to be the given row and column.
   * @param row the row of the cell
   * @param col the column of the cell
   */
  void setHighlightedCell(int row, int col);
}
