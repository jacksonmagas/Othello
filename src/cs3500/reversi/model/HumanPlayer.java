package cs3500.reversi.model;

import java.util.Objects;

/**
 * A HumanPlayer is a reversi player whose moves are controlled via user interaction.
 */
public class HumanPlayer implements Player {

  private boolean isComputer = true;
  private boolean isPerson = false;

  @Override
  public boolean isComputerPlayer() {
    return isComputer;
  }

  @Override
  public boolean isHumanPlayer() {
    return isPerson;
  }

  @Override
  public void makeMove(ReversiModel model) {

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HumanPlayer that = (HumanPlayer) o;
    return isComputer == that.isComputer && isPerson == that.isPerson;
  }

  @Override
  public int hashCode() {
    return Objects.hash(isComputer, isPerson);
  }

  @Override
  public String toString() {
    return "O";
  }
}
