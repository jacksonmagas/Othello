package cs3500.reversi.controller;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.Move;
import cs3500.reversi.view.ReversiFrame;


/**
 * This controller updates the model and view according to user input.
 * In order to ease drawing of the blobs in the view, this controller enforces the following
 * constraint: no part of any blob will be in the -ve X and -ve Y space. That is, the smallest
 * region that contains all the blobs has a lower left corner of (0,0) at the least.
 */
public class ReverseHexGridPlayerController implements YourTurnListener {

  private final ReversiModel model;
  private final ReversiFrame view;
  private final Player player;

  /**
   * Constructor for ReversiHexGridController.
   */
  public ReverseHexGridPlayerController(ReversiModel model, ReversiFrame view, Player player) {
    if (model == null || view == null || player == null) {
      throw new IllegalArgumentException("Model or View or Player is null");
    }
    this.model = model;
    this.player = player;
    this.view = view;
    setListeners();
  }

  private void setListeners() {
    view.setMouseListener(new MyMouseListener(model, view, player));
    view.setMouseMotionListener(new MyMouseListener(model, view, player));
    view.setKeyListener(new MyKeyListener(model, view, player));
  }

  @Override
  public CellState getPlayer() {
    return this.player.getPiece();
  }

  @Override
  public void yourTurn() {
    System.out.println("Your Turn event received for player " + this.model.getCurrentPlayer() +
            " with model\n" + this.model);
    // Make view visible
    view.setModel(this.model);
    view.repaint();
    //view.setVisibleView(false);

    // check if computer move is enabled
    Move move = this.player.play(this.model);
    if (move != null) {
      makeMoveUntilLegalOrTooManyAttempts(move, 0);
      System.out.println("model after move\n" + model);
      view.repaint();
    }
  }

  /**
   * Make the given move and if it is not a legal move ask the player to play another move.
   * @param move the move to attempt to play
   * @param numAttempts the number of move attempts played so far
   * @throws IllegalStateException if more than 100 illegal moves in a row are made
   */
  private void makeMoveUntilLegalOrTooManyAttempts(Move move, int numAttempts) {
    if (numAttempts > 100) {
      throw new IllegalStateException("Too many invalid moves provided.");
    }
    try {
      this.model.makeMove(move);
    } catch (IllegalArgumentException e) {
      System.out.println("That was an illegal move.");
      makeMoveUntilLegalOrTooManyAttempts(this.player.play(this.model), numAttempts + 1);
    }
  }

  // MyMouseListener class listens for mouse movements.
  static class MyMouseListener extends MouseAdapter {

    ReversiModel model;
    ReversiFrame view;
    //int playerIndex;
    final Player player;

    /**
     * Constructor for MyMouseListener class.
     */
    public MyMouseListener(ReversiModel model, ReversiFrame view,
                           Player player) {
      super();
      this.model = model;
      this.view = view;
      this.player = player;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
      CellState currentPlayer = this.model.getCurrentPlayer();
      if (!(currentPlayer != null && currentPlayer.equals(this.player.getPiece()))) {
        // un highlight cell
        //Cell.Location currentCell = this.model.getHighlightedCell();
        this.model.setHighlightedCell(-1, -1);

        view.setModel(this.model);
        view.repaint();
        return;
      }
      int x = e.getX();
      int y = e.getY();

      // Determine row, col using x, y
      HashMap<Point, Point> keyMap = view.getMap();
      //Point rowCol = (Point) keyMap.get(new Point(x, y));
      // 0,0=467,170 0,1=555,170 0,2=644,170
      // 1,0=423,247 1,1=511,247 1,2=600,247
      // horizontal cell distance 44
      // vertical cell distance 33
      Point rowCol = findRowCols(keyMap, new Point(x, y));
      this.model.getBoard();
      //board[x][y] = (int)'X';
      //System.out.println("Controller mouse click event - x "+x+" y "+y);
      //System.out.println("Component "+e.getComponent().toString());
      //System.out.println("Source "+e.getSource());
      int row = -1;
      int col = -1;
      if (rowCol != null) {
        //System.out.println("row " + rowCol.x + " col " + rowCol.y);
        row = rowCol.x;
        col = rowCol.y;
      }
      // highlight cell
      this.model.setHighlightedCell(row, col);
      view.setModel(this.model);
      view.repaint();
    }

    /**
     * MouseClicked checks for if a mouse was clicked on the game board.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
      CellState currentPlayer = this.model.getCurrentPlayer();
      if (!(currentPlayer != null && currentPlayer.equals(this.player.getPiece()))) {
        return;
      }
      int x = e.getX();
      int y = e.getY();

      // Determine row, col using x, y
      HashMap<Point, Point> keyMap = view.getMap();
      //Point rowCol = (Point) keyMap.get(new Point(x, y));
      // 0,0=467,170 0,1=555,170 0,2=644,170
      // 1,0=423,247 1,1=511,247 1,2=600,247
      // horizontal cell distance 44
      // vertical cell distance 33
      Point rowCol = findRowCols(keyMap, new Point(x, y));
      this.model.getBoard();
      if (rowCol != null) {
        int row = rowCol.x;
        int col = rowCol.y;
        try {
          System.out.println("Player " + this.model.getCurrentPlayer() + " is doing move to " +
                  row + " " + col);
          this.player.recieveGUIMove(new Move(row, col));
          //this.playerIndex = (this.playerIndex + 1) % this.players.size();
          System.out.println("model after move\n" + this.model.toString());

        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
          JOptionPane.showMessageDialog(((JFrame)view).getContentPane(), ex.getMessage(),
                  "Message", JOptionPane.ERROR_MESSAGE);
          view.setModel(this.model);
          view.repaint();
        }
      } else {
        // check if user click Pass Turn button
        //if (x >= 248 && x <= 299 && y >= 51 && y <= 96) {
        //if (x >= 250 && x <= 363 && y >= 51 && y <= 163) {
        if (x >= 33 && x <= 148 && y >= 33 && y <= 142) {
          System.out.println("Pass Turn button is clicked!");
          try {
            System.out.println("Player " + this.model.getCurrentPlayer() + " is is passing turn");
            this.player.recieveGUIMove(new Move(true, false, false));
            //this.playerIndex = (this.playerIndex + 1) % this.players.size();
            System.out.println("model after pass-turn\n" + this.model.toString());
          } catch (IllegalArgumentException | IllegalStateException ex) {
            System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
            JOptionPane.showMessageDialog(((JFrame) view).getContentPane(), ex.getMessage(),
                    "Message", JOptionPane.ERROR_MESSAGE);
            view.setModel(this.model);
            view.repaint();
          }
          //} else if (x >= 851 && x <= 931 && y >= 62 && y <= 142) {
        } else if (x >= 146 && x <= 716 && y >= 43 && y <= 128) {
          // check if user click Restart button
          System.out.println("Restart button is clicked!");
          try {
            if (!this.model.isGameOver()) {
              this.player.recieveGUIMove(new Move(false, true, false));
            } else {
              // when the game is over the player is not listening for moves and can't make moves
              this.model.newGame();
            }
            System.out.println("model after restart\n" + this.model.toString());
            view.setModel(this.model);
            view.repaint();
          } catch (IllegalArgumentException | IllegalStateException ex) {
            System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
            JOptionPane.showMessageDialog(((JFrame)view).getContentPane(), ex.getMessage(),
                    "Message", JOptionPane.ERROR_MESSAGE);
            view.setModel(this.model);
            view.repaint();
          }
        }
      }
    }

    // finds the rows and columns of a point
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

    // checks if the row matches
    boolean isRowMatch(int x1, int x2) {
      return (Math.abs(x1 - x2) <= 44);
    }

    // checks if the columns matches
    boolean isColMatch(int y1, int y2) {
      return (Math.abs(y1 - y2) <= 33);
    }

  }

  // MyMouseListener class listens for mouse movements.
  static class MyKeyListener extends KeyAdapter implements KeyListener {
    ReversiModel model;
    ReversiFrame view;
    //int playerIndex;
    final Player player;

    /**
     * Constructor for MyMouseListener class.
     */
    public MyKeyListener(ReversiModel model, ReversiFrame view,
                           Player player) {
      super();
      this.model = model;
      this.view = view;
      this.player = player;
    }

    @Override
    public void keyTyped(KeyEvent e) {
      //System.out.println("KEY TYPED");
    }

    @Override
    public void keyPressed(KeyEvent e) {
      //System.out.println("KEY PRESSED");
    }

    @Override
    public void keyReleased(KeyEvent e) {
      CellState currentPlayer = this.model.getCurrentPlayer();
      if (!(currentPlayer != null && currentPlayer.equals(this.player.getPiece()))) {
        // un highlight cell
        //Cell.Location currentCell = this.model.getHighlightedCell();
        this.model.setHighlightedCell(-1, -1);

        view.setModel(this.model);
        view.repaint();
        return;
      }
      System.out.println("KEY RELEASED");

      this.model.getBoard();
      int rowsCount = this.model.getRows();

      Cell.Location currentCell = this.model.getHighlightedCell();
      int row = currentCell.getRow();
      int col = currentCell.getColumn();
      if (row <= 0) {
        row = 0;
      }
      if (col <= 0) {
        col = 0;
      }
      int colsCount = model.getColumns(row);
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        System.out.println("Up Arrow Key pressed!");
        if (row > 0) {
          row--;
        }
        // highlight cell
        this.model.setHighlightedCell(row, col);
        view.setModel(this.model);
        view.repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        System.out.println("Down arrow Key pressed!");
        if (row + 1 < rowsCount) {
          row++;
        }
        // highlight cell
        this.model.setHighlightedCell(row, col);
        view.setModel(this.model);
        view.repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        System.out.println("Left Arrow Key pressed!");
        if (col > 0) {
          col--;
        }
        // highlight cell
        this.model.setHighlightedCell(row, col);
        view.setModel(this.model);
        view.repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        System.out.println("Right Arrow Key pressed!");
        if (col + 1 < colsCount) {
          col++;
        }
        // highlight cell
        this.model.setHighlightedCell(row, col);
        view.setModel(this.model);
        view.repaint();
      }

      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        System.out.println("Space Key pressed!");
        try {
          System.out.println("Player " + this.model.getCurrentPlayer() + " is doing move to "
                  + row + " " + col);
          this.model.makeMove(row, col);
          System.out.println("model after move\n" + this.model.toString());
        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
          JOptionPane.showMessageDialog(((JFrame)view).getContentPane(), ex.getMessage(),
                  "Message", JOptionPane.ERROR_MESSAGE);
          view.setModel(this.model);
          view.repaint();
        }
      }  else if (e.getKeyCode() == KeyEvent.VK_P) {
        System.out.println("P Key pressed!");
        System.out.println("Pass Turn button is clicked!");
        try {
          System.out.println("Player " + this.model.getCurrentPlayer() + " is is passing turn");
          this.model.passTurn();
          System.out.println("model after pass-turn\n" + this.model.toString());
        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
          JOptionPane.showMessageDialog(((JFrame) view).getContentPane(), ex.getMessage(),
                  "Message", JOptionPane.ERROR_MESSAGE);
          view.setModel(this.model);
          view.repaint();
        }
      } else if (e.getKeyCode() == KeyEvent.VK_R) {
        System.out.println("R Key pressed!");
        System.out.println("Restart button is clicked!");
        try {
          this.model.newGame();
          System.out.println("model after restart\n" + this.model.toString());
          view.setModel(this.model);
          view.repaint();
        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
          JOptionPane.showMessageDialog(((JFrame)view).getContentPane(), ex.getMessage(),
                  "Message", JOptionPane.ERROR_MESSAGE);
          view.setModel(this.model);
          view.repaint();
        }

      } else if (e.getKeyCode() == KeyEvent.VK_Q) {
        System.out.println("Q Key pressed!");
        System.out.println("User wants to quit the game!");
        System.out.println("model before exit\n" + this.model.toString());
        System.exit(1);
      }
    }
  }

}

