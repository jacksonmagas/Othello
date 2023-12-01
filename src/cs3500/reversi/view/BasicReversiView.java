package cs3500.reversi.view;

import cs3500.reversi.model.ReadonlyReversiModel;
import cs3500.reversi.view.hexgrid.MainPanel;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.util.HashMap;
import javax.swing.JFrame;

/**
 * BasicReversiView creates a frame for a view extending JFrame and implementing the ReversiFrame.
 * It sets the size, model, view and mouse/key listeners for the game.
 */
public class BasicReversiView extends JFrame implements ReversiFrame {
  private final MainPanel drawPanel;
  private final ReversiMouseListener mouseListener;

  /**
   * Constructor for BasicReversiView.
   */
  public BasicReversiView(ReadonlyReversiModel model, String playerLabel) {
    String text = "Reversi Hex Grid Game"
        + " "
        + playerLabel
        + " View";
    setTitle(text);

    setSize(new Dimension(WIDTH, HEIGHT));
    //setPreferredSize(new Dimension(WIDTH, HEIGHT));

    drawPanel = new MainPanel(model, playerLabel);
    drawPanel.setFocusable(true);
    setContentPane(drawPanel);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setResizable(true);
    setVisible(true);
    setFocusable(true);
    mouseListener = new ReversiMouseListener(model, this);
    drawPanel.addMouseListener(mouseListener);
    drawPanel.addMouseMotionListener(mouseListener);
  }


  @Override
  public void setVisibleView(boolean visible) {
    drawPanel.setVisible(visible);
    this.setVisible(visible);
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

  // repaints the board
  @Override
  public void repaint() {
    drawPanel.removeAll();
    drawPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    drawPanel.revalidate();
    drawPanel.repaint();
    drawPanel.setFocusable(true);
  }

  // sets the key listener
  @Override
  public void setKeyListener(KeyListener listener) {
    drawPanel.setFocusable(true);
    drawPanel.addKeyListener(listener);
  }

  /**
   * Adds a move listener to this view.
   */
  @Override
  public void addMoveListener(MoveListener listener) {
    mouseListener.register(listener);
  }

  @Override
  public void setFocusable(boolean focus) {
    drawPanel.setFocusable(focus);
  }

  @Override
  public void setHighlightedCell(int row, int col) {
    drawPanel.setHighlightedCell(row, col);
  }

  // gets the hashMap
  @Override
  public HashMap<Point, Point> getMap() {
    return drawPanel.getMap();
  }
}
