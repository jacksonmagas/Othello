package cs3500.reversi.model;

/**
 * This enum represents the different states a disc in our game of cs3500.reversi.Reversi can be in.
 * A disc can be either Black, White, or Empty.
 */
public enum PlayerDisc {
  BLACK,
  WHITE,
  EMPTY;

  /**
   * Checks if this disc the opposite color as the given disc.
   * @param opp represents the disc to compare to.
   * @return {@code true} if this disc is the same color as the given disc,
   *         {@code false} otherwise.
   */
  public boolean isOppColor(PlayerDisc opp) {
    if (opp.equals(BLACK)) {
      return this.equals(WHITE);
    } else if (opp.equals(WHITE)) {
      return this.equals(BLACK);
    } else {
      return false;
    }
  }

  /**
   * Checks if this disc is empty.
   * @return {@code true} if this disc is empty, {@code false} otherwise.
   */
  public boolean isDiscEmpty() {
    return this.equals(EMPTY);
  }

  /**
   * Gets the opposite color of this disc.
   * @return the opposite color of this disc.
   * @throws IllegalArgumentException if this disc is empty.
   */
  public PlayerDisc getOpp() {
    if (this.equals(BLACK)) {
      return WHITE;
    } else if (this.equals(WHITE)) {
      return BLACK;
    } else {
      throw new IllegalArgumentException("Cannot get opposite color of empty disc.");
    }
  }
}
