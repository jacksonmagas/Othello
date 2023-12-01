package cs3500.reversi.controller;

import cs3500.reversi.model.Cell;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.view.ReversiFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


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
    view.addMoveListener(this);
    view.addMoveListener(this.player);
    view.setKeyListener(new MyKeyListener(model, view, player));
  }

  @Override
  public CellState getPlayer() {
    return this.player.getPiece();
  }

  @Override
  public void yourTurn() {
    System.out.println("Your Turn event received for player " + this.model.getCurrentPlayer()
            + " with model\n" + this.model);
    // Make view visible
    view.setFocusable(true);
    view.repaint();

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
      System.out.println("Player " + this.model.getCurrentPlayer() + " is doing move " + move);
      this.model.makeMove(move);
    } catch (IllegalArgumentException e) {
      System.out.println("That was an illegal move.");
      this.refreshView();
      JOptionPane.showMessageDialog(((JFrame)view).getContentPane(), e.getMessage(),
              "Message", JOptionPane.ERROR_MESSAGE);
      makeMoveUntilLegalOrTooManyAttempts(this.player.play(this.model), numAttempts + 1);
    }
  }

  @Override
  public void refreshView() {
    System.out.println("Refresh View event received for player " + this.player.getPiece()
            + " with model\n" + this.model);
    
    view.repaint();
  }

  /**
   * Forward the GUI action to the player of this model.
   * If the action from the GUI is restarting the game and not the current player then restart
   * the model because the current player doesn't know about the restart event.
   * @param m the move that corresponds to the last click
   */
  @Override
  public void receiveGUIAction(Move m) {
    if (player.getPiece() == model.getCurrentPlayer()) {
      model.makeMove(new Move(false, true, false));
    }
  }

  @Override
  public void endTurn() {
    this.player.receiveGUIAction(new Move(false, false, false));
  }

  // Key Listener controls final making of moves
  static class MyKeyListener extends KeyAdapter implements KeyListener {
    ReversiModel model;
    ReversiFrame view;
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
        
        view.repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        System.out.println("Down arrow Key pressed!");
        if (row + 1 < rowsCount) {
          row++;
        }
        // highlight cell
        this.model.setHighlightedCell(row, col);
        
        view.repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        System.out.println("Left Arrow Key pressed!");
        if (col > 0) {
          col--;
        }
        // highlight cell
        this.model.setHighlightedCell(row, col);
        
        view.repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        System.out.println("Right Arrow Key pressed!");
        if (col + 1 < colsCount) {
          col++;
        }
        // highlight cell
        this.model.setHighlightedCell(row, col);
        
        view.repaint();
      }

      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        System.out.println("Space Key pressed!");
        try {
          System.out.println("Player " + this.model.getCurrentPlayer() + " is doing move to "
                  + row + " " + col);
          this.player.receiveGUIAction(new Move(row, col));
          System.out.println("model after move\n" + this.model);
          
          view.repaint();
          this.model.refreshAllViews();
        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
          JOptionPane.showMessageDialog(((JFrame)view).getContentPane(), ex.getMessage(),
                  "Message", JOptionPane.ERROR_MESSAGE);
          
          view.repaint();
        }
      }  else if (e.getKeyCode() == KeyEvent.VK_P) {
        System.out.println("P Key pressed!");
        System.out.println("Pass Turn button is clicked!");
        try {
          System.out.println("Player " + this.model.getCurrentPlayer() + " is passing turn");
          this.player.receiveGUIAction(new Move(true, false, false));
          System.out.println("model after pass-turn\n" + this.model);
          
          view.repaint();
          this.model.refreshAllViews();
        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
          JOptionPane.showMessageDialog(((JFrame) view).getContentPane(), ex.getMessage(),
                  "Message", JOptionPane.ERROR_MESSAGE);
          
          view.repaint();
        }
      } else if (e.getKeyCode() == KeyEvent.VK_R) {
        System.out.println("R Key pressed!");
        System.out.println("Restart button is clicked!");
        try {
          this.player.receiveGUIAction(new Move(false, true, false));
          System.out.println("model after restart\n" + this.model);
          
          view.repaint();
          this.model.refreshAllViews();
        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
          JOptionPane.showMessageDialog(((JFrame)view).getContentPane(), ex.getMessage(),
                  "Message", JOptionPane.ERROR_MESSAGE);
          
          view.repaint();
        }

      } else if (e.getKeyCode() == KeyEvent.VK_Q) {
        System.out.println("Q Key pressed!");
        System.out.println("User wants to quit the game!");
        System.out.println("model before exit\n" + this.model);
        System.exit(1);
      }
    }
  }

}

