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
 * The ReversiEventListener takes mouse events and translates them logical actions in reversi.
 * This MouseListener takes no action on its own, other than notifying the relevant listeners
 * subscribed to it.
 */
class ReversiEventListener extends MouseAdapter {
  ReadonlyReversiModel model;
  ReversiFrame frame;
  final List<MoveListener> listeners;
  private HintsListener hintsListener;
  private int rowHeight;
  private int colWidth;

  /**
   * Constructor for MyMouseListener class.
   */
  public ReversiEventListener(ReadonlyReversiModel model, ReversiFrame frame) {
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

  /**
   * Register a new MoveListener.
   * @param subscriber the listener to register
   */
  public void registerHintsListener(HintsListener subscriber) {
    hintsListener = subscriber;
  }

  // receive hints from subscriber
  private int getPossiblePoints(Move move) {
    if (hintsListener != null) {
      return hintsListener.getPossiblePoints(this.model, this.frame.getPlayer(), move);
    }
    return -1;
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    Point rowCol = frame.XYToRowCol(x, y);
    int row = -1;
    int col = -1;
    String hints = null;
    if (rowCol != null) {
      //System.out.println("row " + rowCol.x + " col " + rowCol.y);
      row = rowCol.x;
      col = rowCol.y;
      try {
        if (this.model.isPlayerHintsEnabled(this.frame.getPlayer())) {
          int numFlipped = getPossiblePoints(new Move(row, col));
          hints = numFlipped == -1 ? "" : String.valueOf(numFlipped);
        }
      } catch (IllegalArgumentException | IllegalStateException ex) {
        System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
      }
    }
    // highlight cell
    frame.setHighlightedCell(row, col, hints);
    frame.repaint();
  }

  /**
   * MouseClicked checks for if a mouse was clicked on the game board.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    Point rowCol = frame.XYToRowCol(x, y);

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
          notifyListeners(new Move(true, false, false, false));
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
          notifyListeners(new Move(false, true, false, false));
          
          frame.repaint();
        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
          JOptionPane.showMessageDialog(((JFrame) frame).getContentPane(), ex.getMessage(),
              "Message", JOptionPane.ERROR_MESSAGE);
          
          frame.repaint();
        }
      } else if (x >= 18 && x <= 77 && y >= 648 && y <= 705) {
        // check if user click Enable/Disable Hints button
        System.out.println("Hints button is clicked!");
        try {
          notifyListeners(new Move(false, false, false, true));

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
}
