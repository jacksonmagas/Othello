package cs3500.reversi.model;

import java.util.Objects;

/**
 * A ComputerPlayer is a reversi game player that is not controlled by a human.
 */
public class ComputerPlayer implements Player {


  @Override
  public boolean isComputerPlayer() {
    return true;
  }

  @Override
  public boolean isHumanPlayer() {
    return false;
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
    ComputerPlayer that = (ComputerPlayer) o;
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash();
  }

  @Override
  public String toString() {
    return "X";
  }
}
