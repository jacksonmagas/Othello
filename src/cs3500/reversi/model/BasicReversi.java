package cs3500.reversi.model;

import cs3500.reversi.model.Cell.Location;
import java.util.ArrayList;

/**
 * It implements the primary model interface for playing a game of Reversi.
 * Moves to this model are made using the coordinate system (row, index) where the row is the
 * horizontal row number and the index is the location in the row 0 indexed from left.
 */
public class BasicReversi implements ReversiModel {
  //Keeps track of the cells in the game in 3 lists, one for each principal direction of a hexagon
  //Invariant: All three lists contain the same cells
  //the horizontal rows 0 indexed from top
  private final ArrayList<ArrayList<Cell>> horizontalRows;
  //the rows which follow the down-right direction 0 indexed from top right of hexagon
  private final ArrayList<ArrayList<Cell>> downRightRows;
  //the rows which follow the down-left direction 0 indexed from top left of hexagon
  private final ArrayList<ArrayList<Cell>> downLeftRows;

  boolean isGameON = false;
  boolean isGameOver = false;

  private int sideLength;
  private int totalNumRows;
  private CellState currentPlayer;

  private int yourScore;
  private int computerScore;

  private int blackPlayerPassTurnsCount;
  private int whitePlayerPassTurnsCount;

  /**
   * Constructor for BasicReversi public class.
   */
  public BasicReversi() {
    this.horizontalRows = new ArrayList<ArrayList<Cell>>();
    this.downRightRows = new ArrayList<ArrayList<Cell>>();
    this.downLeftRows = new ArrayList<ArrayList<Cell>>();
    yourScore = 0;
    computerScore = 0;
    blackPlayerPassTurnsCount = 0;
    whitePlayerPassTurnsCount = 0;
  }

  // put the cell with the given horizontal row and index into the correct location in all three
  // lists
  private void setEmptyCellAt(int hRow, int hIndex) {
    Cell c = new Cell(new Location(hRow, hIndex));
    horizontalRows.get(hRow).add(hIndex, c);
  }

  //get the downRightRow row coordinate of the cell at the given horizontal row and index
  private int getRRow(int hRow, int hIndex) {
    if (hRow <= sideLength) {
      //at and above the centerline the RRow is mid - hIndex + hRow
      return sideLength - hIndex + hRow;
    } else {
      //n rows below the centerline the RRow is mid - hIndex + hRow - n
      return 2 * sideLength - hIndex;
    }
  }

  //get the downRightRow index coordinate of the cell at the given horizontal row and index
  private int getRIndex(int hRow, int hIndex) {
    int rRow = getRRow(hRow, hIndex);
    if (rRow <= sideLength) {
      // left of the downLeft centerline the lIndex is the horizontal row
      return hRow;
    } else {
      // n hIndicies to the right of downLeft centerline the lIndex is the hRow - n
      return hRow - rRow + sideLength;
    }
  }

  //get the downLeftRow row coordinate of the cell at the given horizontal row and index
  private int getLRow(int hRow, int hIndex) {
    if (hRow <= sideLength) {
      //at and above the centerline the LRow is simply the hIndex
      return hIndex;
    } else {
      //n rows below the centerline the LRow is hIndex + n
      return hIndex + hRow - sideLength;
    }
  }

  //get the downLeftRow index coordinate of the cell at the given horizontal row and index
  private int getLIndex(int hRow, int hIndex) {
    int lRow = getLRow(hRow, hIndex);
    if (lRow <= sideLength) {
      // left of the downLeft centerline the lIndex is the horizontal row
      return hRow;
    } else {
      // n hIndicies to the right of downLeft centerline the lIndex is the hRow - n
      return hRow - lRow + sideLength;
    }
  }

  // starts the reversi game
  @Override
  public void startGame(int sideLength) {
    if (this.isGameON) {
      throw new IllegalStateException("Game is already started!");
    }
    if (sideLength < 3) {
      throw new IllegalArgumentException("sideLength should be at-least 3 or above !");
    }

    //initialize game
    this.sideLength = sideLength;
    this.isGameON = true;
    this.isGameOver = false;

    int rowSize = sideLength;
    totalNumRows = 2 * this.sideLength - 1;

    //create empty arrayLists to hold cells
    for (int row = 0; row < totalNumRows; row++) {
      this.horizontalRows.add(new ArrayList<>());
      this.downRightRows.add(new ArrayList<>());
      this.downLeftRows.add(new ArrayList<>());
    }

    // build grid
    for (int rowNum = 0; rowNum < totalNumRows; rowNum++) {
      for (int col = 0; col < rowSize; col++) {
        setEmptyCellAt(rowNum, col);
      }
      if (rowNum < sideLength) {
        rowSize++;
      } else {
        rowSize--;
      }
    }

    //place each players 2 discs
    horizontalRows.get(this.sideLength - 2).get(this.sideLength - 2).setState(CellState.BLACK);
    horizontalRows.get(this.sideLength - 2).get(this.sideLength - 1).setState(CellState.WHITE);
    horizontalRows.get(this.sideLength - 1).get(this.sideLength - 2).setState(CellState.WHITE);
    horizontalRows.get(this.sideLength - 1).get(this.sideLength).setState(CellState.BLACK);
    horizontalRows.get(this.sideLength).get(this.sideLength - 2).setState(CellState.BLACK);
    horizontalRows.get(this.sideLength).get(this.sideLength - 1).setState(CellState.WHITE);
    yourScore = 3;
    computerScore = 3;

    //set black to move first
    this.currentPlayer = CellState.BLACK;
  }

  // switch the current player at end of turn
  private void switchTurn() {
    this.currentPlayer = this.currentPlayer.opposite();
  }

  // gets the cell at its current position
  private Cell getCellAt(int hRow, int hIndex) {
    return this.horizontalRows.get(hRow).get(hIndex);
  }

  // Given a direction grid and coordinates find if the direction sandwiches
  // some number of pieces of the other color.
  private boolean isValidMoveInThisDirection(ArrayList<ArrayList<Cell>> direction,
      int row, int index) {
    int rowLen = direction.get(row).size();
    boolean validInPlus = false;
    boolean validInMinus = false;
    CellState oppositeColor = this.currentPlayer.opposite();
    //Is there an adjacent opponent cell in negative direction?
    if (index > 0 && direction.get(row).size() > 0) {
      Cell prevCell = direction.get(row).get(index - 1);
      validInMinus = prevCell.getState().equals(oppositeColor);
    }

    //If is there an ally cell on the other side of opponent cells?
    if (validInMinus) {
      for (int newIndex = index - 1; newIndex >= 0; newIndex--) {
        CellState curCellState = direction.get(row).get(newIndex).getState();
        if (curCellState == CellState.EMPTY
            || (newIndex == 0 && curCellState != this.currentPlayer)) {
          validInMinus = false;
          break;
        } else if (curCellState == this.currentPlayer) {
          break;
        }
      }
    }

    //Is there an adjacent opponent cell in positive direction?
    if (index < rowLen - 1) {
      Cell nextCell = direction.get(row).get(index + 1);
      validInPlus = nextCell.getState().equals(oppositeColor);
    }

    //If is there an ally cell on the other side of opponent cells?
    if (validInPlus) {
      for (int newIndex = index + 1; newIndex < rowLen; newIndex++) {
        CellState curCellState = direction.get(row).get(newIndex).getState();
        if (curCellState == CellState.EMPTY
            || (newIndex == 0 && curCellState != this.currentPlayer)) {
          validInPlus = false;
          break;
        } else if (curCellState == this.currentPlayer) {
          break;
        }
      }
    }

    return validInMinus || validInPlus;
  }

  // A move is valid if all the following are true:
  // - the cell to move in is empty
  // - the cell to move in is adjacent to the one of the opponent player's cells
  // - in that direction there is a friendly player cell before empty cell/end of list
  private boolean isValidMove(int hRow, int hIndex) {
    return getCellAt(hRow, hIndex).isEmpty()
      && (isValidMoveInThisDirection(this.horizontalRows, hRow, hIndex)
        || isValidMoveInThisDirection(this.downRightRows,
            getRRow(hRow, hIndex),
            getRIndex(hRow, hIndex))
        || isValidMoveInThisDirection(this.downLeftRows,
            getLRow(hRow, hIndex),
            getLIndex(hRow, hIndex)));
  }

  // flips the player disc if the move is valid
  private void flipTilesIfValid(ArrayList<ArrayList<Cell>> direction,
                                             int row, int index) {
    boolean validInPlus = false;
    boolean validInMinus = false;
    CellState oppositeColor = this.currentPlayer.opposite();
    //Is there an adjacent opponent cell in negative direction?
    if (index > 0 && direction.size() > row && direction.get(row).size() > 0) {
      Cell prevCell = direction.get(row).get(index - 1);
      validInMinus = prevCell.getState().equals(oppositeColor);
    }

    //If is there an ally cell on the other side of opponent cells?
    if (validInMinus) {
      for (int newIndex = index - 1; newIndex >= 0; newIndex--) {
        CellState curCellState = direction.get(row).get(newIndex).getState();
        if (curCellState == CellState.EMPTY
                || (newIndex == 0 && curCellState != this.currentPlayer)) {
          validInMinus = false;
          break;
        } else if (curCellState == this.currentPlayer) {
          break;
        }
      }
    }
    // valid so flip tile now
    if (validInMinus) {
      for (int newIndex = index - 1; newIndex >= 0; newIndex--) {
        CellState curCellState = direction.get(row).get(newIndex).getState();
        if (curCellState == CellState.EMPTY
                || (newIndex == 0 && curCellState != this.currentPlayer)) {
          validInMinus = false;
          break;
        } else if (curCellState == this.currentPlayer) {
          break;
        } else {
          flipTileToCurrentPlayer(row, newIndex);
        }
      }
    }

    if (direction.size() > row) {
      int rowLen = direction.get(row).size();
      //Is there an adjacent opponent cell in positive direction?
      if (index < rowLen - 1) {
        Cell nextCell = direction.get(row).get(index + 1);
        validInPlus = nextCell.getState().equals(oppositeColor);
      }

      //If is there an ally cell on the other side of opponent cells?
      if (validInPlus) {
        for (int newIndex = index + 1; newIndex < rowLen; newIndex++) {
          CellState curCellState = direction.get(row).get(newIndex).getState();
          if (curCellState == CellState.EMPTY
                  || (newIndex == 0 && curCellState != this.currentPlayer)) {
            validInPlus = false;
            break;
          } else if (curCellState == this.currentPlayer) {
            break;
          }
        }
      }

      // valid so flip tile now
      if (validInPlus) {
        for (int newIndex = index + 1; newIndex < rowLen; newIndex++) {
          CellState curCellState = direction.get(row).get(newIndex).getState();
          if (curCellState == CellState.EMPTY
                  || (newIndex == 0 && curCellState != this.currentPlayer)) {
            validInPlus = false;
            break;
          } else if (curCellState == this.currentPlayer) {
            break;
          } else {
            flipTileToCurrentPlayer(row, newIndex);
          }
        }
      }
    }
  }

  // flips the player tiles once prompted
  private void flipTiles(int hRow, int hIndex) {
    flipTilesIfValid(this.horizontalRows, hRow, hIndex);
    flipTilesIfValid(this.downLeftRows,
            getLRow(hRow, hIndex),
            getLIndex(hRow, hIndex));
    flipTilesIfValid(this.downRightRows,
            getRRow(hRow, hIndex),
            getRIndex(hRow, hIndex));
  }

  // gives an empty tile space to the current player
  private void setEmptyTileToCurrentPlayer(int row, int index) {
    horizontalRows.get(row).get(index).setState(this.currentPlayer);
    if (this.currentPlayer.equals(CellState.BLACK)) {
      yourScore++;
    } else {
      computerScore++;
    }
  }

  // flips the tile to the current player
  private void flipTileToCurrentPlayer(int row, int index) {
    horizontalRows.get(row).get(index).setState(this.currentPlayer);
    if (this.currentPlayer.equals(CellState.BLACK)) {
      yourScore++;
      computerScore--;
    } else {
      computerScore++;
      yourScore--;
    }
  }

  // makes the move a player prompted
  @Override
  public void makeMove(int row, int index) {
    if (row < 0 || index < 0) {
      throw new IllegalArgumentException("Input parameters either row or column is invalid!");
    }
    if (!isGameON) {
      throw new IllegalStateException("Game is not yet started!");
    }
    if (isGameOver) {
      throw new IllegalStateException("Game is over!");
    }
    if (isValidMove(row, index)) {
      flipTiles(row, index);
      setEmptyTileToCurrentPlayer(row, index);
      switchTurn();
    } else {
      throw new IllegalStateException("There is no legal move at " + row + ", " + index + ".");
    }
  }

  // passes the turn of the player if there are no legal moves left for that player
  @Override
  public void passTurn() {
    if (!isGameON) {
      throw new IllegalStateException("Game is not yet started!");
    }
    if (isGameOver) {
      throw new IllegalStateException("Game is over!");
    }
    if (this.currentPlayer.equals(CellState.BLACK)) {
      blackPlayerPassTurnsCount++;
    } else {
      whitePlayerPassTurnsCount++;
    }
    if (blackPlayerPassTurnsCount >= 1 && whitePlayerPassTurnsCount >= 1) {
      isGameOver = true;
    }
    switchTurn();
  }

  private ArrayList<ArrayList<Cell>> getGrid() {
    if (this.isGameON) {
      return horizontalRows;
    } else {
      throw new IllegalStateException("Game is not yet started!");
    }

  }

  // gets the score of the game for the human player
  @Override
  public int getYourScore() {
    return this.yourScore;
  }

  // gets the score of the game for the computer player
  @Override
  public int getComputerScore() {
    return this.computerScore;
  }

  // returns the output in a string format
  @Override
  public String toString() {
    String output = "";
    int yourScore = 0;
    int computerScore = 0;
    int rowSize = sideLength;
    for (int rowNum = 0; rowNum < totalNumRows; rowNum++) {
      String rowStr = "";
      for (int col = 0; col < rowSize; col++) {
        Cell cell = horizontalRows.get(rowNum).get(col);
        rowStr += cell.toString() + " ";
        if (cell.getState().equals(CellState.BLACK)) {
          yourScore++;
        } else if (cell.getState().equals(CellState.WHITE)) {
          computerScore++;
        }
      }
      if (rowNum < sideLength - 1) {
        rowSize++;
      } else {
        rowSize--;
      }
      int leftSpaces = (totalNumRows + (totalNumRows - 1) - rowStr.length() + 1) / 2;
      output += ((leftSpaces > 0) ? String.format("%-" + leftSpaces + "s", " ") : "") + rowStr
              + ((leftSpaces > 0) ? String.format("%-" + leftSpaces + "s", " ") : "");
      output += "\n";
    }
    output += "Your Score: " + yourScore + "\n";
    output += "Computer Score: " + computerScore + "\n";
    if (!isGameOver) {
      if (this.currentPlayer.equals(CellState.BLACK)) {
        output += "Your turn (Black Disc)!\n";
      } else {
        output += "Computer turn (White Disc)!\n";
      }
    } else {
      output += "Game is over!\n";
    }
    return output;
  }
}
