package cs3500.reversi.view;

import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReadonlyReversiModel;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The ReversiMouseListener takes mouse events and translates them logical actions in reversi.
 * This MouseListener takes no action on its own, other than notifying the relevant listeners
 * subscribed to it.
 */
class ReversiMouseListener extends MouseAdapter {
  ReadonlyReversiModel model;
  ReversiFrame frame;
  final List<MoveListener> listeners;

  /**
   * Constructor for MyMouseListener class.
   */
  public ReversiMouseListener(ReadonlyReversiModel model, ReversiFrame frame) {
    super();
    this.model = model;
    this.frame = frame;
    this.listeners = new ArrayList<>();
  }

  /**
   * Register a new MoveListener.
   * @param subscriber the listener to register
   */
  public void register(MoveListener subscriber) {
    listeners.add(subscriber);
  }

  // notify all listeners that a new valid click has happened
  private void notifyListeners(Move move) {
    for (MoveListener listener : listeners) {
      listener.receiveGUIAction(move);
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    // Determine row, col using x, y
    HashMap<Point, Point> keyMap = frame.getMap();
    // 0,0=467,170 0,1=555,170 0,2=644,170
    // 1,0=423,247 1,1=511,247 1,2=600,247
    // horizontal cell distance 44
    // vertical cell distance 33
    Point rowCol = findRowCols(keyMap, new Point(x, y));
    int row = -1;
    int col = -1;
    if (rowCol != null) {
      //System.out.println("row " + rowCol.x + " col " + rowCol.y);
      row = rowCol.x;
      col = rowCol.y;
    }
    // highlight cell
    frame.setHighlightedCell(row, col);
    frame.repaint();
  }

  /**
   * MouseClicked checks for if a mouse was clicked on the game board.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    // Determine row, col using x, y
    HashMap<Point, Point> keyMap = frame.getMap();
    //Point rowCol = (Point) keyMap.get(new Point(x, y));
    // 0,0=467,170 0,1=555,170 0,2=644,170
    // 1,0=423,247 1,1=511,247 1,2=600,247
    // horizontal cell distance 44
    // vertical cell distance 33
    Point rowCol = findRowCols(keyMap, new Point(x, y));
    this.model.getBoard();

    System.out.println("Controller mouse click event - x " + x + " y " + y);
    if (rowCol != null) {
      int row = rowCol.x;
      int col = rowCol.y;
      try {
        notifyListeners(new Move(row, col));
      } catch (IllegalArgumentException | IllegalStateException ex) {
        System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
        JOptionPane.showMessageDialog(((JFrame) frame).getContentPane(), ex.getMessage(),
            "Message", JOptionPane.ERROR_MESSAGE);
        frame.repaint();
      }
    } else {
      // check if user click Pass Turn button
      //if (x >= 248 && x <= 299 && y >= 51 && y <= 96) {
      //if (x >= 250 && x <= 363 && y >= 51 && y <= 163) {
      if (x >= 33 && x <= 148 && y >= 33 && y <= 142) {
        System.out.println("Pass Turn button is clicked!");
        try {
          System.out.println("Player " + this.model.getCurrentPlayer() + " is passing turn");
          notifyListeners(new Move(true, false, false));
          //this.playerIndex = (this.playerIndex + 1) % this.players.size();
          System.out.println("model after pass-turn\n" + this.model.toString());
        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
          JOptionPane.showMessageDialog(((JFrame) frame).getContentPane(), ex.getMessage(),
              "Message", JOptionPane.ERROR_MESSAGE);
          frame.repaint();
        }
        //} else if (x >= 851 && x <= 931 && y >= 62 && y <= 142) {
      } else if (x >= 146 && x <= 716 && y >= 43 && y <= 128) {
        // check if user click Restart button
        System.out.println("Restart button is clicked!");
        try {
          notifyListeners(new Move(false, true, false));
          
          frame.repaint();
        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
          JOptionPane.showMessageDialog(((JFrame) frame).getContentPane(), ex.getMessage(),
              "Message", JOptionPane.ERROR_MESSAGE);
          
          frame.repaint();
        }
      }
    }
  }

  // finds the rows and columns of a point in graphics coordinates in reversi logical coordinates
  private Point findRowCols(HashMap<Point, Point> keyMap, Point mouseClickedPoint) {
    Point rowColPoint = null;
    if (keyMap != null && mouseClickedPoint != null) {
      for (Point key : keyMap.keySet()) {
        if (key != null
            && isRowMatch(key.x, mouseClickedPoint.x)
            && isColMatch(key.y, mouseClickedPoint.y)) {
          rowColPoint = keyMap.get(key);
          break;
        }
      }
    }
    return rowColPoint;
  }

  // checks if the two points are in the same row
  private boolean isRowMatch(int x1, int x2) {
    return (Math.abs(x1 - x2) <= 44);
  }

  // checks if the two points are in the same column
  private boolean isColMatch(int y1, int y2) {
    return (Math.abs(y1 - y2) <= 33);
  }
}
