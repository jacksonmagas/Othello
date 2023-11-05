package cs3500.reversi.model;

import cs3500.reversi.model.Cell.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

  boolean isGameOver = false;

  private final int center;
  private final int totalNumRows;
  private CellState currentPlayer;

  private final int[] playerScores;

  private int blackPlayerPassTurnsCount;
  private int whitePlayerPassTurnsCount;

  /**
   * Constructor for BasicReversi public class.
   */
  public BasicReversi(int sideLength) {
    if (sideLength < 3) {
      throw new IllegalArgumentException("sideLength should be at-least 3 or above !");
    }
    this.horizontalRows = new ArrayList<ArrayList<Cell>>();
    this.downRightRows = new ArrayList<ArrayList<Cell>>();
    this.downLeftRows = new ArrayList<ArrayList<Cell>>();
    this.playerScores = new int[2];
    blackPlayerPassTurnsCount = 0;
    whitePlayerPassTurnsCount = 0;
    //initialize game
    this.center = sideLength - 1;
    this.isGameOver = false;

    int rowSize = sideLength;
    totalNumRows = 2 * sideLength - 1;

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
    horizontalRows.get(this.center - 1).get(this.center - 1).setState(CellState.BLACK);
    horizontalRows.get(this.center - 1).get(this.center).setState(CellState.WHITE);
    horizontalRows.get(this.center).get(this.center - 1).setState(CellState.WHITE);
    horizontalRows.get(this.center).get(this.center + 1).setState(CellState.BLACK);
    horizontalRows.get(this.center + 1).get(this.center - 1).setState(CellState.BLACK);
    horizontalRows.get(this.center + 1).get(this.center).setState(CellState.WHITE);
    playerScores[0] = 3;
    playerScores[1] = 3;

    //set black to move first
    this.currentPlayer = CellState.BLACK;
  }

  // put the cell with the given horizontal row and index into the correct location in all three
  // lists
  private void setEmptyCellAt(int hRow, int hIndex) {
    Cell c = new Cell(new Location(hRow, hIndex));
    horizontalRows.get(hRow).add(hIndex, c);
  }

  //get the downRightRow row coordinate of the cell at the given horizontal row and index
  private int getRRow(int hRow, int hIndex) {

    if (hRow <= center) {
      //at and above the centerline the RRow is mid - hIndex + hRow
      return center - hIndex + hRow;
    } else {
      //n rows below the centerline the RRow is mid - hIndex + hRow - n
      return 2 * center - hIndex;
    }
  }

  //get the downRightRow index coordinate of the cell at the given horizontal row and index
  private int getRIndex(int hRow, int hIndex) {
    int rRow = getRRow(hRow, hIndex);
    if (rRow <= center) {
      // left of the downLeft centerline the lIndex is the horizontal row
      return hRow;
    } else {
      // n hIndicies to the right of downLeft centerline the lIndex is the hRow - n
      return hRow - rRow + center;
    }
  }

  //get the downLeftRow row coordinate of the cell at the given horizontal row and index
  private int getLRow(int hRow, int hIndex) {
    if (hRow <= center) {
      //at and above the centerline the LRow is simply the hIndex
      return hIndex;
    } else {
      //n rows below the centerline the LRow is hIndex + n
      return hIndex + hRow - center;
    }
  }

  //get the downLeftRow index coordinate of the cell at the given horizontal row and index
  private int getLIndex(int hRow, int hIndex) {
    int lRow = getLRow(hRow, hIndex);
    if (lRow <= center) {
      // left of the downLeft centerline the lIndex is the horizontal row
      return hRow;
    } else {
      // n hIndicies to the right of downLeft centerline the lIndex is the hRow - n
      return hRow - lRow + center;
    }
  }


  // switch the current player at end of turn
  private void switchTurn() {
    this.currentPlayer = this.currentPlayer.opposite();
  }

  // gets the cell at its current position
  private Cell getCellAt(int hRow, int hIndex) {
    return this.horizontalRows.get(hRow).get(hIndex);
  }

  // Given a direction grid and coordinates find if pieces of the current color sandwich
  // some number of pieces of the other color along this axis.
  private boolean isValidMoveInThisDirection(ArrayList<ArrayList<Cell>> direction,
      int row, int index) {
    return isValidInThisDirectionMinus(direction, row, index)
        || isValidInThisDirectionPlus(direction, row, index);
  }

  // Given a direction grid and coordinates find if pieces of the current color sandwich
  // some number of pieces of the other color along this axis in the increasing index direction.
  private boolean isValidInThisDirectionPlus(ArrayList<ArrayList<Cell>> direction,
      int row,
      int index) {
    int rowLen = direction.get(row).size();
    boolean validInPlus = false;
    CellState oppositeColor = this.currentPlayer.opposite();
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
    return validInPlus;
  }

  // Given a direction grid and coordinates find if pieces of the current color sandwich
  // some number of pieces of the other color along this axis in the decreasing index direction.
  private boolean isValidInThisDirectionMinus(ArrayList<ArrayList<Cell>> direction,
      int row,
      int index) {
    boolean validInMinus = false;
    CellState oppositeColor = this.currentPlayer.opposite();
    //Is there an adjacent opponent cell in negative direction?
    if (index > 0 && !direction.get(row).isEmpty()) {
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
    return validInMinus;
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
    CellState oppositeColor = this.currentPlayer.opposite();
    // valid so flip tile now
    if (isValidInThisDirectionMinus(direction, row, index)) {
      int newIndex = index - 1;
      CellState curCellState = direction.get(row).get(newIndex).getState();
      while (curCellState == oppositeColor) {
        flipTileToCurrentPlayer(row, newIndex);
        newIndex--;
        curCellState = direction.get(row).get(newIndex).getState();
      }
    }

    // valid so flip tile now
    if (isValidInThisDirectionPlus(direction, row, index)) {
      int newIndex = index + 1;
      CellState curCellState = direction.get(row).get(newIndex).getState();
      while (curCellState == oppositeColor) {
        flipTileToCurrentPlayer(row, newIndex);
        newIndex++;
        curCellState = direction.get(row).get(newIndex).getState();
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
      playerScores[0]++;
    } else {
      playerScores[1]++;
    }
  }

  // flips the tile to the current player
  private void flipTileToCurrentPlayer(int row, int index) {
    horizontalRows.get(row).get(index).setState(this.currentPlayer);
    if (this.currentPlayer.equals(CellState.BLACK)) {
      playerScores[0]++;
      playerScores[1]--;
    } else {
      playerScores[1]++;
      playerScores[0]--;
    }
  }

  // makes the move a player prompted
  @Override
  public void makeMove(int row, int index) {
    if (row < 0 || index < 0) {
      throw new IllegalArgumentException("Input parameters either row or column is invalid!");
    }
    if (isGameOver) {
      throw new IllegalStateException("Game is over!");
    }
    if (isValidMove(row, index)) {
      setEmptyTileToCurrentPlayer(row, index);
      flipTiles(row, index);
      switchTurn();
    } else {
      throw new IllegalStateException("There is no legal move at " + row + ", " + index + ".");
    }
  }

  // passes the turn of the player if there are no legal moves left for that player
  @Override
  public void passTurn() {
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
    return horizontalRows;
  }

  // gets the score of the game for the given player
  @Override
  public int getPlayerScore(int playerNum) {
    try {
      return this.playerScores[playerNum];
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("playerNum must be a valid player number.");
    }
  }

  @Override
  public boolean isGameOver() {
    return this.isGameOver;
  }

  @Override
  public List<List<CellState>> getGameBoard() {
    return this.horizontalRows.stream().map((ArrayList<Cell> c) -> c.stream().map(Cell::getState).collect(
        Collectors.toList())).collect(Collectors.toList());
  }

  @Override
  public CellState getTileAt(int hRow, int hIndex) {
    return this.horizontalRows.get(hRow).get(hIndex).getState();
  }

  // returns the output in a string format
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    int rowSize = center + 1;
    for (int rowNum = 0; rowNum < totalNumRows; rowNum++) {
      StringBuilder rowStr = new StringBuilder();
      for (int col = 0; col < rowSize; col++) {
        Cell cell = horizontalRows.get(rowNum).get(col);
        rowStr.append(cell.toString()).append(" ");
      }
      if (rowNum < center) {
        rowSize++;
      } else {
        rowSize--;
      }
      int numLeftSpaces = (totalNumRows + (totalNumRows - 1) - rowStr.length() + 1) / 2;
      String paddingSpaces =
          (numLeftSpaces > 0) ? String.format("%-" + numLeftSpaces + "s", " ") : "";
      output.append(paddingSpaces).append(rowStr).append(paddingSpaces);
      output.append("\n");
    }
    output.append("Player one Score: ").append(playerScores[0]).append("\n");
    output.append("Player two Score: ").append(playerScores[1]).append("\n");
    if (!isGameOver) {
      if (this.currentPlayer.equals(CellState.BLACK)) {
        output.append("Player one turn (Black)!\n");
      } else {
        output.append("Player two turn (White)!\n");
      }
    } else {
      output.append("Game is over!\n");
    }
    return output.toString();
  }
}
