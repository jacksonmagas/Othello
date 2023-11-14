package cs3500.reversi.view.hexgrid;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.HashMap;

import javax.swing.JPanel;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.ReadonlyReversiModel;

/**
 * Public class MainPanel creates the panel of the hexgrid view.
 */
public class MainPanel extends JPanel {
  private final int WIDTH = 1200;

  private final int HEIGHT = 800;

  private Font font = new Font("Arial", Font.BOLD, 12);

  FontMetrics metrics;

  private static int[][] board;

  private int currentRow = 0;
  private int currentCol = 0;

  private ReadonlyReversiModel model;

  public static HashMap<Point, Point> POINTS_TO_ROW_COLS = new HashMap<Point, Point>();

  /**
   * Constructor for MainPanel class.
   */
  public MainPanel(ReadonlyReversiModel model) {
    this.model = model;
    this.board = model.getBoard();
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
  }

  /**
   * Sets the model.
   */
  public void setModel(ReadonlyReversiModel model) {
    this.model = model;
    this.board = model.getBoard();
    Cell.Location location = model.getHighlightedCell();
    this.currentRow = location.getRow();
    this.currentCol = location.getColumn();
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
  }

  // paints the component
  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    Point origin = new Point(WIDTH / 2, HEIGHT / 2);

    g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
    g2d.setFont(font);
    metrics = g.getFontMetrics();

    drawRectangle(g2d, origin, 380, true, true, Color.BLACK, 0);
    drawHexGridLoop(g2d, origin, 7, 50, 1);
    drawStatusMessages(g2d, origin, 380, true, Color.WHITE);
    drawErrorMessages(g2d, origin, 380, true, Color.RED);
    drawPassTurnButton(g2d, origin, 380, true, Color.LIGHT_GRAY);
    drawRestartButton(g2d, origin, 380, true, Color.LIGHT_GRAY);
  }

  // draws the restart button
  private void drawRestartButton(Graphics2D g, Point origin, int radius,
                                  boolean centered, Color colorValue) {
    Graphics2D g2d = (Graphics2D) g;
    int x2 = centered ? origin.x - radius : origin.x;
    int y2 = centered ? origin.y - radius : origin.y;

    StringBuffer text = new StringBuffer();
    text.append("Restart");
    int w = metrics.stringWidth(text.toString());
    int h = metrics.getHeight();

    g.setColor(colorValue);
    g.fillOval(x2 + w * 15, y2 + w, w * 2, w * 2);

    g.setColor(Color.BLACK);
    g.drawString(text.toString(), x2 + w * 15 + 18, y2 + w * 2);
  }

  // draws the passTurn button
  private void drawPassTurnButton(Graphics2D g, Point origin, int radius,
                                  boolean centered, Color colorValue) {
    Graphics2D g2d = (Graphics2D) g;
    int x2 = centered ? origin.x - radius : origin.x;
    int y2 = centered ? origin.y - radius : origin.y;

    StringBuffer text = new StringBuffer();
    text.append("Pass Turn");
    int w = metrics.stringWidth(text.toString());
    int h = metrics.getHeight();

    g.setColor(colorValue);
    g.fillOval(x2 + w / 2, y2 + w / 2, w * 2, w * 2);

    g.setColor(Color.BLACK);
    g.drawString(text.toString(), x2 + w / 2 + 25, y2 + w + w / 2);
  }

  // draws the status messages
  private void drawStatusMessages(Graphics2D g, Point origin, int radius,
                                  boolean centered, Color colorValue) {
    Graphics2D g2d = (Graphics2D) g;
    int x2 = centered ? origin.x - radius : origin.x;
    int y2 = centered ? origin.y - radius : origin.y;

    StringBuffer text = new StringBuffer();
    text.append("Player one Score: ");
    text.append(model.getPlayerScore(0));
    text.append(", ");
    text.append("Player two Score: ");
    text.append(model.getPlayerScore(1));
    text.append(", ");
    text.append(model.getNextStepInstructions());
    text.append(", ");
    int w = metrics.stringWidth(text.toString());
    int h = metrics.getHeight();
    g2d.setColor(colorValue);
    g2d.drawString(text.toString(), x2 + w / 2, y2 + h * 2);
  }

  // draws the error messages
  private void drawErrorMessages(Graphics2D g, Point origin, int radius,
                                  boolean centered, Color colorValue) {
    //System.out.println("Error "+model.getLastErrorMessage());
    Graphics2D g2d = (Graphics2D) g;
    if (model.getLastErrorMessage().length() > 0) {
      int x2 = centered ? origin.x - radius : origin.x;
      int y2 = centered ? origin.y - radius : origin.y;

      StringBuffer text = new StringBuffer();
      text.append(model.getLastErrorMessage());
      int w = metrics.stringWidth(text.toString());
      int h = metrics.getHeight();
      g2d.setColor(colorValue);
      g2d.drawString(text.toString(), x2 + w + 72, y2 + h * 4);
    }
  }

  // draws the hexgrid loop
  private void drawHexGridLoop(Graphics g, Point origin, int size, int radius, int padding) {
    double ang30 = Math.toRadians(30);
    double xOff = Math.cos(ang30) * (radius + padding);
    double yOff = Math.sin(ang30) * (radius + padding);
    int half = size / 2;

    POINTS_TO_ROW_COLS = new HashMap<Point, Point>();
    /*
    System.out.println("size "+size+" half "+half+" board length "+board.length);
    System.out.println("Printing the game board");
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board.length; col++) {
        System.out.print(board[row][col]);
      }
      System.out.println("");
    }
    */
    Color defaultValue = null;
    for (int row = 0; row < size; row++) {
      int cols = size - java.lang.Math.abs(row - half);
      //System.out.println("cols "+cols);
      for (int col = 0; col < cols; col++) {
        int xLbl = row < half ? col - row : col - half;
        int yLbl = row - half;
        xLbl = row;
        yLbl = col;
        int x = (int) (origin.x + xOff * (col * 2 + 1 - cols));
        int y = (int) (origin.y + yOff * (row - half) * 3);
        POINTS_TO_ROW_COLS.put(new Point(x, y), new Point(row, col));
        defaultValue = null;
        Color hightlightColor = null;
        if (board[row][col] != 0) {
          if (board[row][col] == (int)'X') {
            defaultValue = Color.BLACK;
          } else if (board[row][col] == (int)'O') {
            defaultValue = Color.WHITE;
          }
          if (this.currentRow == row && this.currentCol == col) {
            hightlightColor = Color.CYAN;
          }
        }
        drawHex(g, xLbl, yLbl, x, y, radius, defaultValue, hightlightColor);
      }
    }
  }

  // gets the map
  public HashMap<Point, Point> getMap() {
    return POINTS_TO_ROW_COLS;
  }

  // draw the hexagon
  private void drawHex(Graphics g, int posX, int posY, int x, int y, int r, Color colorValue,
                       Color hightlightColor) {
    Graphics2D g2d = (Graphics2D) g;

    Hexagon hex = new Hexagon(x, y, r);

    char bulletSymbol = '⚪';
    String text = String.format("%s : %s", coord(posX), coord(posY));
    int w = metrics.stringWidth(text);
    int h = metrics.getHeight();

    if (hightlightColor != null) {
      hex.draw(g2d, x, y, 0, hightlightColor, true);
    } else {
      hex.draw(g2d, x, y, 0, Color.GRAY, true);
    }
    hex.draw(g2d, x, y, 4, Color.DARK_GRAY, false);


    if (colorValue != null) {
      g.setColor(colorValue);
      g.drawString(text, x - w / 2, y + h / 2);
      g.fillOval(x - w, y - w, w * 2, w * 2);

    }

    // debug
    /*
    if (colorValue == null) {
      colorValue = Color.WHITE;
    }
    g.setColor(colorValue);
    g.drawString(text, x - w/2, y + h/2);
    */
  }

  // coordinate value of a point
  private String coord(int value) {
    return (value > 0 ? "+" : "") + Integer.toString(value);
  }

  /**
   * Draws the rectangle.
   */
  public void drawRectangle(Graphics2D g, Point origin, int radius,
                         boolean centered, boolean filled, Color colorValue, int lineThickness) {
    // Store before changing.
    Stroke tmpS = g.getStroke();
    Color tmpC = g.getColor();

    g.setColor(colorValue);
    g.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND));

    int diameter = radius * 2;
    int x2 = centered ? origin.x - radius : origin.x;
    int y2 = centered ? origin.y - radius : origin.y;

    if (filled) {
      g.fillRect(x2, y2, diameter, diameter);
    }
    else {
      g.drawRect(x2, y2, diameter, diameter);
    }

    // Set values to previous when done.
    g.setColor(tmpC);
    g.setStroke(tmpS);
  }

  /*
  public static void main(String[] args) {

    int noOfCells = 4;
    ReversiModel reversi = new BasicReversi(noOfCells);
    JFrame f = new JFrame();
    MainPanel p = new MainPanel(reversi);
    board = reversi.getBoard();

    f.setContentPane(p);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.pack();
    f.setLocationRelativeTo(null);
    f.setVisible(true);
  }
  */
}
