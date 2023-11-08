package cs3500.reversi.view;

import com.sun.tools.javac.Main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.*;

import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.ReadonlyReversiModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.view.hexgrid.Hexagon;
import cs3500.reversi.view.hexgrid.MainPanel;

public class BasicReversiView extends JFrame implements ReversiFrame {
  @Override
  public void addFeatureListener(ViewFeatures features) {

  }

  private MainPanel drawPanel;

  private Font font = new Font("Arial", Font.BOLD, 22);
  FontMetrics metrics;

  public BasicReversiView(int width, int height, ReadonlyReversiModel model) {
    setTitle("Reversi Hex Grid Game");

    setPreferredSize(new Dimension(WIDTH, HEIGHT));

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
   *
   * Finally, call repaint to redraw the panel
   */
  @Override
  public void setCanvasSize(int width, int height) {
    drawPanel.setPreferredSize(new Dimension(width, height));
    drawPanel.revalidate();
    drawPanel.repaint();
  }

  @Override
  public void setModel(ReadonlyReversiModel model) {
    drawPanel.setModel(model);
  }
  @Override
  public void repaint() {
    drawPanel.removeAll();
    drawPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    drawPanel.revalidate();
    drawPanel.repaint();
  }

  @Override
  public void setMouseListener(MouseListener listener) {
    drawPanel.addMouseListener(listener);
  }

  @Override
  public HashMap<Point, Point> getMap() {
    return drawPanel.getMap();
  }

  public static void main(String[] args) {
    int noOfCells = 4;
    int WIDTH = 1200;
    int HEIGHT = 800;

    ReversiModel reversi = new BasicReversi(noOfCells);
    BasicReversiView view = new BasicReversiView(WIDTH, HEIGHT, reversi);

  }

}
