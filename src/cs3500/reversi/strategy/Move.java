package cs3500.reversi.strategy;

/**
 * A value object representing move details:
 * coordinates in the board or pass turn
 */
public class Move {
  boolean passTurn;
  Posn posn;

  /**
   * Constructor for Move class.
   */
  public Move(boolean passTurn) {
    this.passTurn = passTurn;
    this.posn = null;
  }

  /**
   * Constructor for Move class.
   */
  public Move(int row, int col) {
    this.passTurn = false;
    this.posn = new Posn(row, col);
  }

  public boolean isPassTurn() {
    return passTurn;
  }

  public Posn getPosn() {
    return posn;
  }

  public static class Posn {
    public final int row, col;
    public Posn(int row, int col) {
      this.row = row;
      this.col = col;
    }
  }

}
