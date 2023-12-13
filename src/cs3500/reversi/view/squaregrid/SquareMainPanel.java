package cs3500.reversi.view.squaregrid;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReadonlyReversiModel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Public class MainPanel creates the panel of the hexgrid view.
 */
public class SquareMainPanel extends JPanel {
  private final int WIDTH = 764;

  private final int HEIGHT = 764;
  private final int CELL_SIZE = 75;

  private final Font font = new Font("Arial", Font.BOLD, 12);

  FontMetrics metrics;

  private static List<List<CellState>> board;

  private int currentRow = 0;

  private int currentCol = 0;
  private String hints;

  private ReadonlyReversiModel model;

  // Map from the top left corner of each rectangle to the row and column of the rectangle
  public static HashMap<Point, Point> POINTS_TO_ROW_COLS;
  private CellState player;

  /**
   * Constructor for MainPanel class.
   */
  public SquareMainPanel(ReadonlyReversiModel model, CellState player) {
    this.model = model;
    this.player = player;
    board = model.getGameBoard();
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setFocusable(true);
    requestFocusInWindow();

    this.getInputMap().put(
        KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT);
    this.getActionMap().put(LEFT, left);
    this.getInputMap().put(
        KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT);
    this.getActionMap().put(RIGHT, right);
    this.getInputMap().put(
        KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), UP);
    this.getActionMap().put(UP, up);
    this.getInputMap().put(
        KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), DOWN);
    this.getActionMap().put(DOWN, down);
    this.getInputMap().put(
        KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), P);
    this.getActionMap().put(P, p);
    this.getInputMap().put(
        KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), R);
    this.getActionMap().put(R, r);
    this.getInputMap().put(
        KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), Q);
    this.getActionMap().put(Q, q);
    this.getInputMap().put(
        KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), SPACE);
    this.getActionMap().put(SPACE, space);
  }

  private static final String LEFT = "Left";
  private static final String RIGHT = "Right";
  private static final String UP = "Up";
  private static final String DOWN = "Down";
  private static final String P = "P";
  private static final String R = "R";
  private static final String Q = "Q";
  private static final String SPACE = "Space";

  private Action left = new AbstractAction(LEFT) {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.out.println(LEFT);
    }
  };

  private Action right = new AbstractAction(RIGHT) {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.out.println(RIGHT);
    }
  };

  private Action up = new AbstractAction(UP) {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.out.println(UP);
    }
  };

  private Action down = new AbstractAction(DOWN) {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.out.println(DOWN);
    }
  };

  private Action p = new AbstractAction(P) {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.out.println(P);
    }
  };

  private Action r = new AbstractAction(R) {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.out.println(R);
    }
  };

  private Action q = new AbstractAction(Q) {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.out.println(Q);
    }
  };

  private Action space = new AbstractAction(SPACE) {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.out.println(SPACE);
    }
  };

  // paints the component
  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    Point origin = new Point(WIDTH / 2, HEIGHT / 2);

    g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
    g2d.setFont(font);
    metrics = g.getFontMetrics();

    drawBackground(g2d, origin, 380, true, true, Color.BLACK, 0);
    drawGameBoard(g2d, origin, model.getRows());
    drawStatusMessages(g2d, origin, 380, true, Color.WHITE);
    drawPassTurnButton(g2d, origin, 380, true, Color.LIGHT_GRAY);
    drawRestartButton(g2d, origin, 380, true, Color.LIGHT_GRAY);
    //drawErrorMessages(g2d, origin, 380, true, Color.lightGray);
    drawEnableHintsButton(g2d, origin, 380, true, Color.LIGHT_GRAY);
  }

  // draws the restart button
  private void drawRestartButton(Graphics2D g, Point origin, int radius,
      boolean centered, Color colorValue) {
    int x2 = centered ? origin.x - radius : origin.x;
    int y2 = centered ? origin.y - radius : origin.y;

    StringBuilder text = new StringBuilder();
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
    int x2 = centered ? origin.x - radius : origin.x;
    int y2 = centered ? origin.y - radius : origin.y;

    StringBuilder text = new StringBuilder();
    text.append("Pass Turn");
    int w = metrics.stringWidth(text.toString());
    int h = metrics.getHeight();

    g.setColor(colorValue);
    g.fillOval(x2 + w / 2, y2 + w / 2, w * 2, w * 2);

    g.setColor(Color.BLACK);
    g.drawString(text.toString(), x2 + w / 2 + 25, y2 + w + w / 2);
  }

  // draws the enable/disable Hints button
  private void drawEnableHintsButton(Graphics2D g, Point origin, int radius,
      boolean centered, Color colorValue) {
    int x2 = centered ? origin.x - radius : origin.x;
    int y2 = centered ? origin.y + 250 : origin.y;

    StringBuilder text = new StringBuilder();
    text.append("Hints");
    int w = metrics.stringWidth(text.toString());
    int h = metrics.getHeight();

    g.setColor(colorValue);
    g.fillOval(x2 + w / 2, y2 + w / 2, w * 2, w * 2);

    g.setColor(Color.BLACK);
    g.drawString(text.toString(), x2 + w, y2 + w + w/2 + 5);
  }

  // draws the status messages
  private void drawStatusMessages(Graphics2D g, Point origin, int radius,
      boolean centered, Color colorValue) {

    int x2 = centered ? origin.x - radius : origin.x;
    int y2 = centered ? origin.y - radius : origin.y;

    StringBuilder text = new StringBuilder();
    text.append("Player one Score: ");
    text.append(model.getPlayerScore(CellState.BLACK));
    text.append(", ");
    text.append("Player two Score: ");
    text.append(model.getPlayerScore(CellState.WHITE));
    text.append(", ");
    text.append(model.getNextStepInstructions());
    int w = metrics.stringWidth(text.toString());
    int h = metrics.getHeight();
    g.setColor(colorValue);
    int xpos = Math.min((x2 + w / 2), 188);
    g.drawString(text.toString(), xpos, y2 + h * 2);
  }

  // draws the error messages
  private void drawErrorMessages(Graphics2D g, Point origin, int radius,
      boolean centered, Color colorValue) {
    //System.out.println("Error "+model.getLastErrorMessage());
    if (!model.getLastErrorMessage().isEmpty()) {
      int x2 = centered ? origin.x - radius : origin.x;
      int y2 = centered ? origin.y - radius : origin.y;

      StringBuilder text = new StringBuilder();
      text.append(model.getLastErrorMessage());
      int w = metrics.stringWidth(text.toString());
      int h = metrics.getHeight();
      g.setColor(colorValue);
      g.drawString(text.toString(), x2 + w + 72, y2 + h * 4);
    }
  }

  // draws the board loop
  private void drawGameBoard(Graphics g, Point origin, int size) {
    POINTS_TO_ROW_COLS = new HashMap<>();
    CellState pieceState;
    String text = "";
    for (int row = 0; row < size; row++) {
      //System.out.println("cols "+cols);
      for (int col = 0; col < size; col++) {
        boolean selected = false;
        int x = origin.x - CELL_SIZE * (size / 2 - col);
        int y = origin.y - CELL_SIZE * (size / 2 - row);
        POINTS_TO_ROW_COLS.put(new Point(x, y), new Point(row, col));
        pieceState = model.getStateAt(row, col);
        if (pieceState == CellState.EMPTY) {
          if (this.currentRow == row && this.currentCol == col) {
            //System.out.println("currentRow " + row + " currentCol " + col);
            selected = true;
            text = this.hints;
            //System.out.println("hints text " + text);
          }
        }
        drawSquare(g, x, y, pieceState, selected, text);
      }
    }
  }

  // gets the map
  public HashMap<Point, Point> getMap() {
    return POINTS_TO_ROW_COLS;
  }

  // draw a square on the game board at the given location with the given cell state and whether
  //  it is selected
  private void drawSquare(Graphics g, int x, int y, CellState state,
      Boolean isSelected, String text) {
    Graphics2D g2d = (Graphics2D) g;
    // draw highlighted or unhighlighted square
    if (isSelected) {
      g2d.setColor(Color.CYAN);
    } else {
      g2d.setColor(Color.LIGHT_GRAY);
    }
    g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
    // draw outline
    g2d.setColor(Color.DARK_GRAY);
    g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);

    // draw text or draw piece in the cell
    if (state == CellState.EMPTY) {
      //System.out.println("Hints "+text);
      if (isSelected && text != null) {
        int w = metrics.stringWidth(text);
        int h = metrics.getHeight();
        g.setColor(Color.BLACK);
        g.drawString(text, x + (CELL_SIZE - w) / 2, y + (CELL_SIZE + h) / 2);
      }
    } else {
      g.setColor(state == CellState.BLACK ? Color.BLACK : Color.WHITE);
      //text = String.format("%s : %s", coord(x), coord(y));
      //int w = metrics.stringWidth(text);
      double pieceScaleFactor = 0.75;
      g.fillOval(x + (int) (CELL_SIZE - pieceScaleFactor * CELL_SIZE) / 2,
          y + (int) (CELL_SIZE - pieceScaleFactor * CELL_SIZE) / 2,
          (int) (pieceScaleFactor * CELL_SIZE),
          (int) (pieceScaleFactor * CELL_SIZE));
    }
  }

  // coordinate value of a point
  private String coord(int value) {
    return (value > 0 ? "+" : "") + value;
  }

  /**
   * Draws the rectangle.
   */
  public void drawBackground(Graphics2D g, Point origin, int radius,
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

  /**
   * Set the cell to highlight cyan.
   * @param row the row of the cell
   * @param col the column of the cell
   */
  public void setHighlightedCell(int row, int col, String hints) {
    this.currentCol = col;
    this.currentRow = row;
    this.hints = hints;
  }
}
