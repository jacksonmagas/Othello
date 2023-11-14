package cs3500.reversi.controller;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.Move;
import cs3500.reversi.view.BasicReversiView;
import cs3500.reversi.view.ReversiFrame;


/**
 * This controller updates the model and view according to user input.
 * In order to ease drawing of the blobs in the view, this controller enforces the following
 * constraint: no part of any blob will be in the -ve X and -ve Y space. That is, the smallest
 * region that contains all the blobs has a lower left corner of (0,0) at the least.
 */
public class ReverseHexGridController implements ReversiPlayerStrategyController {

  private ReversiModel model;
  private ReversiFrame view;

  private int playerIndex;
  private final List<Player> players;
  private final int WIDTH = 1200;
  private final int HEIGHT = 800;

  /**
   * Constructor for ReversiHexGridController.
   */
  public ReverseHexGridController(ReversiModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model is null");
    }
    this.model = model;
    this.players = new ArrayList<Player>();
  }

  /**
   * Adds a player to the game.
   */
  @Override
  public void addPlayer(Player player) {
    this.players.add(Objects.requireNonNull(player));
  }

  /**
   * Plays the game.
   */
  @Override
  public void play() {
    if (model == null) {
      throw new IllegalArgumentException("Model is null");
    }
    if (players == null || players.size() != 2) {
      throw new IllegalArgumentException("Players are not added!");
    }
    this.view = new BasicReversiView(WIDTH, HEIGHT, model);
    setMouseListener();
    this.playerIndex = 0;
    while (!this.model.isGameOver()) {
      Move move = this.players.get(this.playerIndex).play(this.model);
      try {
        if (move != null) {
          if (move.getPosn() != null) {
            try {
              this.model.makeMove(move.getPosn().row, move.getPosn().col);
              this.playerIndex = (this.playerIndex + 1) % this.players.size();
              System.out.println(model.toString());
              view.setModel(this.model);
              view.repaint();
              this.playerIndex = (this.playerIndex + 1) % this.players.size();
            } catch (IllegalArgumentException | IllegalStateException ex) {
              System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
              view.setModel(this.model);
              view.repaint();
            }
          } else if (move.isPassTurn()) {
            try {
              this.model.passTurn();
              this.playerIndex = (this.playerIndex + 1) % this.players.size();
              System.out.println(model.toString());
              view.setModel(this.model);
              view.repaint();
            } catch (IllegalArgumentException | IllegalStateException ex) {
              System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
              view.setModel(this.model);
              view.repaint();
            }
          }
        }
      } catch (Exception ex) {
        System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
      }
    }
  }

  private void setMouseListener() {

    view.setMouseListener(new MyMouseListener(model, view, playerIndex, players));
  }

  // MyMouseListener class listens for mouse movements.
  class MyMouseListener extends MouseAdapter {

    ReversiModel model;
    ReversiFrame view;
    int playerIndex;
    final List<Player> players;

    /**
     * Constructor for MyMouseListener class.
     */
    public MyMouseListener(ReversiModel model, ReversiFrame view,
                           int playerIndex, List<Player> players) {
      super();
      this.model = model;
      this.view = view;
      this.playerIndex = playerIndex;
      this.players = players;
    }

    /**
     * MouseClicked checks for if a mouse was clicked on the game board.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
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
      int[][] board = this.model.getBoard();
      //board[x][y] = (int)'X';
      //System.out.println("Controller mouse click event - x "+x+" y "+y);
      //System.out.println("Component "+e.getComponent().toString());
      //System.out.println("Source "+e.getSource());
      if (rowCol != null) {
        //System.out.println("row " + rowCol.x + " col " + rowCol.y);
        int row = rowCol.x;
        int col = rowCol.y;
        try {
          this.model.makeMove(row, col);
          this.playerIndex = (this.playerIndex + 1) % this.players.size();
          System.out.println(model.toString());
          // check if computer move is enabled
          if (!this.model.isGameOver()) {
            Move move = this.players.get(this.playerIndex).play(this.model);
            if (move != null) {
              if (move.getPosn() != null) {
                System.out.println("Computer is doing move to " + move.getPosn().row + " " +
                        move.getPosn().col);
                this.model.makeMove(move.getPosn().row, move.getPosn().col);
                this.playerIndex = (this.playerIndex + 1) % this.players.size();
              } else if (move.isPassTurn()) {
                System.out.println("Computer is passing move");
                this.model.passTurn();
                this.playerIndex = (this.playerIndex + 1) % this.players.size();
              }
              System.out.println(model.toString());
            }
          }
          // refresh game after both moves
          view.setModel(this.model);
          view.repaint();
        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
          view.setModel(this.model);
          view.repaint();
        }
      } else {
        // check if user click Pass Turn button
        //if (x >= 248 && x <= 299 && y >= 51 && y <= 96) {
        if (x >= 250 && x <= 363 && y >= 51 && y <= 163) {
          System.out.println("Pass Turn button is clicked!");
          try {
            this.model.passTurn();
            this.playerIndex = (this.playerIndex + 1) % this.players.size();
            System.out.println(model.toString());
            // check if computer move is enabled
            if (!this.model.isGameOver()) {
              Move move = this.players.get(this.playerIndex).play(this.model);
              if (move != null) {
                if (move.getPosn() != null) {
                  System.out.println("Computer is doing move to " + move.getPosn().row + " " +
                          move.getPosn().col);
                  this.model.makeMove(move.getPosn().row, move.getPosn().col);
                  this.playerIndex = (this.playerIndex + 1) % this.players.size();
                } else if (move.isPassTurn()) {
                  System.out.println("Computer is passing move");
                  this.model.passTurn();
                  this.playerIndex = (this.playerIndex + 1) % this.players.size();
                }
                System.out.println(model.toString());
              }
            }
            // refresh game after both moves
            view.setModel(this.model);
            view.repaint();
          } catch (IllegalArgumentException | IllegalStateException ex) {
            System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
            view.setModel(this.model);
            view.repaint();
          }
        } else if (x >= 851 && x <= 931 && y >= 62 && y <= 142) {
          // check if user click Restart button
          this.playerIndex = 0;
          try {
            this.model.restart();
            System.out.println(model.toString());
            view.setModel(this.model);
            view.repaint();
          } catch (IllegalArgumentException | IllegalStateException ex) {
            System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
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
}

