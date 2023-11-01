package cs3500.reversi.model;

import java.util.Objects;

/**
 * A BlackPlayer is a player with the black tile and can be either the
 * computer player or human player.
 */
public class BlackPlayer implements Player {

  private boolean isComputer = false;
  private boolean isPerson = true;

  @Override
  public boolean isComputerPlayer() {
    return isComputer;
  }

  @Override
  public boolean isPersonPlayer() {
    return isPerson;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlackPlayer that = (BlackPlayer) o;
    return isComputer == that.isComputer && isPerson == that.isPerson;
  }

  @Override
  public int hashCode() {
    return Objects.hash(isComputer, isPerson);
  }

  @Override
  public String toString() {
    return "X";
  }
}
