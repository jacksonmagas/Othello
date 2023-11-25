package cs3500.reversi.view;

import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReadonlyReversiModel;

/**
 * Interface ReversiFrame creates frame.
 */
public interface ReversiFrame {

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

  public void repaint();

  HashMap<Point, Point> getMap();

  public void setModel(ReadonlyReversiModel model);

  void addPlayer(CellState viewPlayer);

  void setVisibleView(boolean visible);
}
