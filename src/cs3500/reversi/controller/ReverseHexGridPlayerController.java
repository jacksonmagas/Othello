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
    model.addYourTurnListener(this);
    this.player = player;
    this.view = view;
    setListeners();
  }

  private void setListeners() {
    view.addMoveListener(this);
    view.addMoveListener(this.player);
    view.addHintsListener(player);
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
    view.repaint();

    Move move = this.player.play(this.model);
    if (move != null) {
      makeMoveUntilLegalOrTooManyAttempts(move, 0);
      System.out.println("model after move\n" + model);
      view.repaint();
    }
    if (model.isGameOver()) {
      model.notifyStateChanged();
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
      view.repaint();
      view.displayErrorMessage(e.getMessage());
      makeMoveUntilLegalOrTooManyAttempts(this.player.play(this.model), numAttempts + 1);
    }
  }

  @Override
  public void gameOver() {
    System.out.println("Game Over event received for player " + this.player.getPiece()
            + " with model\n" + this.model);
    view.repaint();
  }

  /**
   * Handle restarts when they happen, and if this player is waiting for their move
   * skip it to allow restart.
   * @param m the move that corresponds to the last click
   */
  @Override
  public void receiveGUIAction(Move m) {
    if (m.isRestartGame()) {
      model.makeMove(new Move(false, true, false, false));
    } else if (m.isQuitGame()) {
      model.makeMove(new Move(false, false, true, false));
    } else if (m.isToggleHints()) {
      model.togglePlayerHints(this.player.getPiece());
    }
  }

  @Override
  public void endTurn() {
    this.player.receiveGUIAction(new Move(false, false, false, false));
  }
}

