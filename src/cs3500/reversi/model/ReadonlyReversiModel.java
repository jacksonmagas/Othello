package cs3500.reversi.model;

/**
 * Represents the read-only model interface for playing a game of Reversi.
 */
public interface ReadonlyReversiModel {
  /**
   * Return the score of the given player either 0 or 1, which is the sum
   * of the values of the disc cards.
   * @return the score
   * @throws IllegalStateException if the game hasn't been started yet
   */
  public int getPlayerScore(int playerNum);
}
