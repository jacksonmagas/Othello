package cs3500.reversi.strategy;

/**
 * A Strategy interface for choosing where to play next for the given player.
 */
public interface MoveStrategy {
  /**
   * Notify the strategy that there is a new move from the gui.
   * Non-gui based strategies can implement this as a stub.
   */
  void newGUIMove(Move newMove);
}
