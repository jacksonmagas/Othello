package cs3500.reversi.model;

import cs3500.reversi.model.Cell.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A HexagonBoard is a reversi board make using hexagonal tiles.
 * It has 3 major axis, and is implemented using the coordinate system (row, index) where the row
 * is the horizontal row number and the index is the location in the row 0 indexed from left.
 *
 */
class HexagonBoard implements ReversiBoard {
  //Keeps track of the cells in the game in 3 lists, one for each principal direction of a hexagon
  //Invariant: All three lists contain the same cells
  //the horizontal rows 0 indexed from top
  private final List<List<Cell>> horizontalRows;

  //the rows which follow the down-right direction 0 indexed from top right of hexagon
  private final List<List<Cell>> downRightRows;

  //the rows which follow the down-left direction 0 indexed from top left of hexagon
  private final List<List<Cell>> downLeftRows;

  private final int center;

  private final int totalNumRows;

  private final Map<CellState, Integer> numTilesEachState;

  private final List<Location> starterBlackTiles;
  private final List<Location> starterWhiteTiles;


  HexagonBoard(int sideLength) {
    if (sideLength < 3) {
      throw new IllegalArgumentException("sideLength should be at least 3");
    }

    // initialize number of tiles of each state to 0
    this.numTilesEachState = new HashMap<>();
    Arrays.stream(CellState.values()).forEach((CellState c) -> numTilesEachState.put(c, 0));

    this.center = sideLength - 1;

    // initialize board with empty Lists
    this.horizontalRows = new ArrayList<>();
    this.downRightRows = new ArrayList<>();
    this.downLeftRows = new ArrayList<>();

    int rowSize = sideLength;
    totalNumRows = 2 * sideLength - 1;

    for (int row = 0; row < totalNumRows; row++) {
      this.horizontalRows.add(new ArrayList<>());
      this.downRightRows.add(new ArrayList<>());
      this.downLeftRows.add(new ArrayList<>());
      for (int col = 0; col < rowSize; col++) {
        this.horizontalRows.get(row).add(null);
        this.downRightRows.get(row).add(null);
        this.downLeftRows.get(row).add(null);
      }
      if (row < center) {
        rowSize++;
      } else {
        rowSize--;
      }
    }

    // fill lists with empty cells.
    rowSize = sideLength;
    for (int rowNum = 0; rowNum < totalNumRows; rowNum++) {
      for (int col = 0; col < rowSize; col++) {
        setEmptyCellAt(rowNum, col);
      }
      if (rowNum < center) {
        rowSize++;
      } else {
        rowSize--;
      }
    }

    this.starterBlackTiles = List.of(new Location(sideLength() - 2, sideLength() - 2),
        new Location(sideLength() - 1, sideLength()),
        new Location(sideLength(), sideLength() - 2));
    this.starterWhiteTiles = List.of(new Location(sideLength() - 2, sideLength() - 1),
        new Location(sideLength() - 1, sideLength() - 2),
        new Location(sideLength(), sideLength() - 1));

    // place starter tiles
    starterBlackTiles.forEach((Location l) -> placeDisc(l, CellState.BLACK));
    starterWhiteTiles.forEach((Location l) -> placeDisc(l, CellState.WHITE));
  }

  /**
   * Constructor for new hexagon board in custom configuration.
   * @param sideLength the size of the board
   * @param blackTiles List of locations to put black tiles
   * @param whiteTiles List of locations to put white tiles
   */
  HexagonBoard(int sideLength, List<Location> blackTiles, List<Location> whiteTiles) {
    this(sideLength);
    // clear starter tiles then place custom tiles
    starterBlackTiles.forEach((Location l) -> placeDisc(l, CellState.EMPTY));
    starterWhiteTiles.forEach((Location l) -> placeDisc(l, CellState.EMPTY));
    blackTiles.forEach((Location l) -> placeDisc(l, CellState.BLACK));
    whiteTiles.forEach((Location l) -> placeDisc(l, CellState.WHITE));
  }

  /**
   * Copy constructor for HexagonBoard;
   * @param base the HexagonBoard to copy
   */
  private HexagonBoard(HexagonBoard base) {
    this.totalNumRows = base.totalNumRows;
    this.center = base.center;
    this.downRightRows = dirCopy(base.downRightRows);
    this.horizontalRows = dirCopy(base.horizontalRows);
    this.downLeftRows = dirCopy(base.downLeftRows);
    this.numTilesEachState = new HashMap<>();
    numTilesEachState.putAll(base.numTilesEachState);
    this.starterBlackTiles = base.starterBlackTiles;
    this.starterWhiteTiles = base.starterWhiteTiles;
  }

  // deep copy a list of list of cells
  private List<List<Cell>> dirCopy(List<List<Cell>> dir) {
    return dir.stream()
        .map((List<Cell> cells) -> cells.stream()
            .map(Cell::new)
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  private void incrementTileCount(CellState player) {
    numTilesEachState.put(player, numTilesEachState.get(player) + 1);
  }

  private void decrementTileCount(CellState player) {
    numTilesEachState.put(player, numTilesEachState.get(player) - 1);
  }

  // put the cell with the given horizontal row and index into the correct location in all three
  // lists
  private void setEmptyCellAt(int hRow, int hIndex) {
    Cell c = new Cell(new Location(hRow, hIndex));
    horizontalRows.get(hRow).set(hIndex, c);
    downRightRows.get(getRRow(hRow, hIndex)).set(getRIndex(hRow, hIndex), c);
    downLeftRows.get(getLRow(hRow, hIndex)).set(getLIndex(hRow, hIndex), c);
    // increment number of empty tiles
    incrementTileCount(CellState.EMPTY);
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

  @Override
  public void placeDisc(Location location, CellState state) throws IllegalArgumentException {
    Cell cell = getCellAt(location);
    // decrement count of previous state in this tile and increment count of this state
    decrementTileCount(cell.getState());
    incrementTileCount(state);
    cell.setState(state);
  }

  @Override
  public Cell getCellAt(Location location) throws IllegalArgumentException {
    try {
      return this.horizontalRows.get(location.getRow()).get(location.getColumn());
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("The given location is out of bounds.", e);
    }
  }

  @Override
  public ReversiBoard copy() {
    return new HexagonBoard(this);
  }

  @Override
  public int numTiles(CellState state) {
    return numTilesEachState.get(state);
  }

  @Override
  public List<List<List<Cell>>> getBoardInAllDirections() {
    return List.of(horizontalRows, downRightRows, downLeftRows);
  }

  @Override
  public List<Location> getLocationInAllDirections(Location location) {
    return List.of(location,
        new Location(getRRow(location.row, location.column),
            getRIndex(location.row, location.column)),
        new Location(getLRow(location.row, location.column),
            getLIndex(location.row, location.column)));
  }

  @Override
  public int sideLength() {
    return this.center + 1;
  }

  @Override
  public void reset() {
    // set all cells to empty
    Arrays.stream(CellState.values()).forEach((CellState c) -> numTilesEachState.put(c, 0));
    horizontalRows.stream()
        .flatMap(Collection::stream)
        .forEach((Cell c) -> {
          c.setState(CellState.EMPTY);
          incrementTileCount(CellState.EMPTY);
        });

    starterBlackTiles.forEach((Location l) -> placeDisc(l, CellState.BLACK));
    starterWhiteTiles.forEach((Location l) -> placeDisc(l, CellState.WHITE));
  }

  @Override
  public BoardType getType() {
    return BoardType.HEXAGON;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HexagonBoard that = (HexagonBoard) o;
    return center == that.center
        && Objects.equals(horizontalRows, that.horizontalRows)
        && Objects.equals(downRightRows, that.downRightRows)
        && Objects.equals(downLeftRows, that.downLeftRows)
        && Objects.equals(numTilesEachState, that.numTilesEachState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(horizontalRows, downRightRows, downLeftRows, center, numTilesEachState);
  }

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
    return output.toString();
  }
}
