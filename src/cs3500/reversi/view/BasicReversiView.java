package cs3500.reversi.view;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReadonlyReversiModel;
import cs3500.reversi.view.hexgrid.MainPanel;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * BasicReversiView creates a frame for a view extending JFrame and implementing the ReversiFrame.
 * It sets the size, model, view and mouse/key listeners for the game.
 */
public class BasicReversiView extends JFrame implements ReversiFrame {
  private final MainPanel drawPanel;
  private final ReversiMouseListener mouseListener;
  private CellState player;

  /**
   * Constructor for BasicReversiView.
   */
  public BasicReversiView(ReadonlyReversiModel model, CellState player) {
    String text = "Reversi Hex Grid Game"
        + " "
        + player.name()
        + " View";
    setTitle(text);

    setSize(new Dimension(WIDTH, HEIGHT));
    //setPreferredSize(new Dimension(WIDTH, HEIGHT));

    this.player = player;
    drawPanel = new MainPanel(model, player);
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
  public CellState getPlayer() {
    return this.player;
  }

  @Override
  public void setVisibleView(boolean visible) {
    drawPanel.setVisible(visible);
    this.setVisible(visible);
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

  @Override
  public void displayErrorMessage(String message) {
    JOptionPane.showMessageDialog(this.getContentPane(), message,
        "Message", JOptionPane.ERROR_MESSAGE);
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
  public void setHighlightedCell(int row, int col, String hints) {
    drawPanel.setHighlightedCell(row, col, hints);
  }

  @Override
  public void addHintsListener(HintsListener features) {
    mouseListener.registerHintsListener(features);
  }

  // gets a map from on screen coordinates to logical coordinates
  HashMap<Point, Point> getMap() {
    return drawPanel.getMap();
  }
}
