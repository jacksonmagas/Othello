package cs3500.reversi.model;

/**
 * A player can be either a human player or a computer player.
  */
public interface Player {

  public boolean isComputerPlayer();

  public boolean isHumanPlayer();

  /**
   * The player makes a move in its reversi game.
   */
  public void makeMove();
}
