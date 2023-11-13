package cs3500.reversi.view;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.HashMap;

import cs3500.reversi.model.ReadonlyReversiModel;

/**
 * ReversiFrame assists in creating the GUI for the game.
 */
public interface ReversiFrame {
  void addFeatureListener(ViewFeatures features);

  /**
   * This method will be called when the drawing area has changed
   */
  void setCanvasSize(int width, int height);

  /**
   * This method will be called to set a mouse listener
   */
  void setMouseListener(MouseListener listener);

  // repaints the ReversiFrame
  public void repaint();

  HashMap<Point, Point> getMap();

  // sets the model
  public void setModel(ReadonlyReversiModel model);
}
