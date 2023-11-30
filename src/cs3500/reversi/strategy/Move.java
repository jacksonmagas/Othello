package cs3500.reversi.strategy;

import java.util.Objects;

/**
 * A value object representing move details:
 * coordinates in the board or pass turn.
 */
public class Move {
  boolean passTurn;
  boolean restartGame;
  boolean quitGame;
  Posn posn;

  /**
   * Constructor for Move class.
   */
  public Move(boolean passTurn, boolean restartGame, boolean quitGame) {
    this.passTurn = passTurn;
    this.restartGame = restartGame;
    this.quitGame = quitGame;
    this.posn = null;
  }

  /**
   * Constructor for Move class.
   */
  public Move(int row, int col) {
    this.passTurn = false;
    this.posn = new Posn(row, col);
  }

  // checks if the turn is being passed
  public boolean isPassTurn() {
    return passTurn;
  }

  // checks if game is being restarted
  public boolean isRestartGame() {
    return restartGame;
  }

  // checks if game is being quit
  public boolean isQuitGame() {
    return quitGame;
  }

  // gets the position
  public Posn getPosn() {
    return posn;
  }

  /**
   * Public class Posn is the position of a move.
   */
  public static class Posn {
    public final int row;

    public final int col;

    /**
     * Constructor for Posn class.
     */
    public Posn(int row, int col) {

      this.row = row;

      this.col = col;
    }

    @Override
    public boolean equals(Object o) {
      return o instanceof Posn && ((Posn) o).row == this.row && ((Posn) o).col == this.col;
    }

    @Override
    public int hashCode() {
      return Objects.hash(row, col);
    }
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof Move)
        && ((Move) o).passTurn == this.passTurn
        && ((Move) o).quitGame == this.quitGame
        && ((Move) o).restartGame == this.restartGame
        && Objects.equals(((Move) o).posn, this.posn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(passTurn, restartGame, quitGame, posn);
  }


  @Override
  public String toString() {
    String asString;
    if (passTurn) {
      asString = "Pass Turn";
    } else if (isQuitGame()) {
      asString = "Quit Game";
    } else if (isRestartGame()) {
      asString = "Restart Game";
    } else if (posn != null) {
      asString = String.format("Move at %d, %d", posn.row, posn.col);
    } else {
      asString = "Nonsense Move";
    }
    return asString + System.lineSeparator();
  }
}
