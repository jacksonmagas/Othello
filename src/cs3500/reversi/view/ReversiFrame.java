package cs3500.reversi.view;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReadonlyReversiModel;

/**
 * Interface ReversiFrame creates frame for Reversi game. Sets the mouse/key listeners, view
 * and size for the game.
 */
public interface ReversiFrame {

  /**
   * Adds a feature listener to the reversiFrame.
   */
  void addFeatureListener(ViewFeatures features);

  /**
   * This method will be called when the drawing area has changed.
   */
  void setCanvasSize(int width, int height);

  /**
   * This method will be called to set a mouse listener.
   */
  void setMouseListener(MouseListener listener);

  /**
   * This method will be called to set a mouse motion listener.
   */
  void setMouseMotionListener(MouseMotionListener listener);

  /**
   * Redraws the view.
   */
  void repaint();

  /**
   * Get a map from points on the GUI to the corresponding logical coordinates for the game
   * @return The map from point to coordinate
   */
  HashMap<Point, Point> getMap();

  /**
   * Sets the model that the main panel will use to draw the game.
   * @param model the model to draw.
   */
  void setModel(ReadonlyReversiModel model);

  /**
   * Add the given player to the view. //TODO: the view doesn't seem to interact with the player. Is this an oversight?
   * @param viewPlayer the player to add to the view
   */
  void addPlayer(CellState viewPlayer);

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
}
