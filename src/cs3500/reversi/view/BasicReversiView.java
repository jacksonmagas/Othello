package cs3500.reversi.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JFrame;

import cs3500.reversi.model.ReadonlyReversiModel;
import cs3500.reversi.view.hexgrid.MainPanel;

/**
 * BasicReversiView creates a frame for a view.
 */
public class BasicReversiView extends JFrame implements ReversiFrame {

  /**
   * Adds a listener.
   */
  @Override
  public void addFeatureListener(ViewFeatures features) {
    // adds feature listener
  }

  private MainPanel drawPanel;

  private Font font = new Font("Arial", Font.BOLD, 22);

  FontMetrics metrics;

  /**
   * Constructor for BasicReversiView.
   */
  public BasicReversiView(int width, int height, ReadonlyReversiModel model) {
    setTitle("Reversi Hex Grid Game");

    setSize(new Dimension(WIDTH, HEIGHT));
    //setPreferredSize(new Dimension(WIDTH, HEIGHT));

    drawPanel = new MainPanel(model);
    setContentPane(drawPanel);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setResizable(true);
    setVisible(true);

  }

  /**
   * This method is called whenever the drawing area changes Our drawing area is drawPanel, which is
   * decorated by the JScrollPane We reset the area of the drawPanel, and then ask it to revalidate.
   * This will cause its parent, the scrollbar, to update itself. Read more at {@see <a
   * href="https://docs.oracle.com/javase/7/docs/api/javax/swing/JComponent.html#revalidate()"
   * revalidate documentation</a>}
   * Finally, call repaint to redraw the panel
   */
  @Override
  public void setCanvasSize(int width, int height) {
    drawPanel.setPreferredSize(new Dimension(width, height));
    drawPanel.revalidate();
    drawPanel.repaint();
  }

  // sets the model
  @Override
  public void setModel(ReadonlyReversiModel model) {
    drawPanel.setModel(model);
  }

  // repaints the board
  @Override
  public void repaint() {
    drawPanel.removeAll();
    drawPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    drawPanel.revalidate();
    drawPanel.repaint();
  }

  // sets the mouse listener
  @Override
  public void setMouseListener(MouseListener listener) {
    drawPanel.addMouseListener(listener);
  }

  // gets the hashMap
  @Override
  public HashMap<Point, Point> getMap() {
    return drawPanel.getMap();
  }
}
