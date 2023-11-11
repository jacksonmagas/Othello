package cs3500.reversi.model;

import java.util.Objects;

/**
 * A ComputerPlayer is a reversi game player that is not controlled by a human.
 */
public class SimpleComputerPlayer implements Player {
  int playerNum;
  ReversiModel actualGame;
  ReversiModel testCopy;

  public SimpleComputerPlayer(ReversiModel model, int playerNum) {
    this.actualGame = model;
    this.playerNum = playerNum;
  }

  private static class Location {
    final int hRow;
    final int hIndex;

    public Location(int hRow, int hIndex) {
      this.hRow = hRow;
      this.hIndex = hIndex;
    }
  }

  @Override
  public boolean isComputerPlayer() {
    return true;
  }

  @Override
  public boolean isHumanPlayer() {
    return false;
  }

  @Override
  public void makeMove() {
    Location bestMove = findBestMove();
    try {
      actualGame.makeMove(bestMove.hRow, bestMove.hIndex);
    } catch (IllegalArgumentException e) {
      actualGame.passTurn();
    }
  }

  private Location findBestMove() {
    int rowSize = actualGame.sideLength();
    int numRows = 2 * rowSize - 1;
    int prevBest = 0;
    Location bestMove = new Location(-1, -1);

    for (int row = 0; row < numRows; row++) {
      testCopy = actualGame.copy();
      for (int index = 0; index < rowSize; index++) {
        try {
          testCopy.makeMove(row, index);
          if (testCopy.getPlayerScore(playerNum) > prevBest) {
            bestMove = new Location(row, index);
            prevBest = testCopy.getPlayerScore(playerNum);
          }
          testCopy = actualGame.copy();
        } catch (IllegalArgumentException e) {
          // if the move is illegal we move on to the next move
          // this means we only have to copy the board after legal moves
        }
      }
      if (row < actualGame.sideLength() - 1) {
        rowSize++;
      } else {
        rowSize--;
      }
    }
    return bestMove;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimpleComputerPlayer that = (SimpleComputerPlayer) o;
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
