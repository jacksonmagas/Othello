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
 * A SquareBoard is a reversi board made using square tiles.
 * It has 4 major axis, and is implemented using the coordinate system (row, index) where the row
 * is the horizontal row number and the index is the location in the row 0 indexed from left.
 *
 */
class SquareBoard implements ReversiBoard {
  //Keeps track of the cells in the game in 4 lists, one each for --, |, / and \ directions
  //Invariant: Both lists contain the same cells
  private final List<List<Cell>> horizontalRows; // --
  // the vertical rows are the transpose of the horizontal rows
  private final List<List<Cell>> verticalRows; // |
  private final List<List<Cell>> downRightRows; // \
  private final List<List<Cell>> downLeftRows; // /
  private final Map<CellState, Integer> numTilesEachState;
  private final int sideLength;

  private final List<Location> starterBlackTiles;
  private final List<Location> starterWhiteTiles;

  /**
   * Constructor for a default start game square reversi board.
   * @param sideLength the size of the board
   */
  SquareBoard(int sideLength) {
    if (sideLength < 4) {
      throw new IllegalArgumentException("SideLength must be at least 4.");
    }
    if (sideLength % 2 != 0) {
      throw new IllegalArgumentException("SideLength must be even.");
    }
    this.sideLength = sideLength;
    this.starterBlackTiles = List.of(new Location(sideLength / 2 - 1, sideLength / 2 - 1),
        new Location(sideLength / 2, sideLength / 2));
    this.starterWhiteTiles = List.of(new Location(sideLength / 2 - 1, sideLength / 2),
        new Location(sideLength / 2, sideLength / 2 - 1));
    this.numTilesEachState = new HashMap<>();
    Arrays.stream(CellState.values()).forEach((CellState c) -> numTilesEachState.put(c, 0));

    // initialize board with empty Lists
    horizontalRows = new ArrayList<>();
    verticalRows = new ArrayList<>();
    downRightRows = new ArrayList<>();
    downLeftRows = new ArrayList<>();

    // create 2d lists to the right size for h and v
    for (int row = 0; row < sideLength; row++) {
      this.horizontalRows.add(new ArrayList<>());
      this.verticalRows.add(new ArrayList<>());
      for (int col = 0; col < sideLength; col++) {
        this.horizontalRows.get(row).add(null);
        this.verticalRows.get(row).add(null);
      }
    }
    // create diamond shaped 2d lists for diagonals
    int rowLen = 1;
    for (int row = 0; row < 2 * sideLength - 1; row++) {
      this.downRightRows.add(new ArrayList<>());
      this.downLeftRows.add(new ArrayList<>());
      for (int col = 0; col < rowLen; col++) {
        this.downRightRows.get(row).add(null);
        this.downLeftRows.get(row).add(null);
      }
      // rows get bigger until middle row then get smaller from middle row on
      rowLen += row < sideLength - 1 ? 1 : -1;
    }

    // fill lists with empty cells and fill diagonal lists at the same time.
    for (int rowNum = 0; rowNum < sideLength; rowNum++) {
      for (int col = 0; col < sideLength; col++) {
        setEmptyCellAt(rowNum, col);
      }
    }

    starterBlackTiles.forEach((Location l) -> placeDisc(l, CellState.BLACK));
    starterWhiteTiles.forEach((Location l) -> placeDisc(l, CellState.WHITE));
  }

  /**
   * Constructor for custom start position of square reversi board.
   * Note: the set of black tiles and the set of white tiles must be disjoint,
   * if they contain the same cells that is undefined behavior.
   * @param sideLength the size of the board
   * @param blackTiles A list of locations to put the black tiles
   * @param whiteTiles A list of locations to put the white tiles
   *
   */
  SquareBoard(int sideLength, List<Location> blackTiles, List<Location> whiteTiles) {
    this(sideLength);
    // empty starter tiles to allow placing custom starter board
    starterBlackTiles.forEach((Location l) -> placeDisc(l, CellState.EMPTY));
    starterWhiteTiles.forEach((Location l) -> placeDisc(l, CellState.EMPTY));

    //Place player discs
    blackTiles.forEach((Location l) -> placeDisc(l, CellState.BLACK));
    whiteTiles.forEach((Location l) -> placeDisc(l, CellState.WHITE));
  }

  public SquareBoard(SquareBoard base) {
    this.starterBlackTiles = base.starterBlackTiles;
    this.starterWhiteTiles = base.starterWhiteTiles;
    this.sideLength = base.sideLength;
    this.horizontalRows = dirCopy(base.horizontalRows);
    this.verticalRows = dirCopy(base.horizontalRows);
    this.downLeftRows = dirCopy(base.downLeftRows);
    this.downRightRows = dirCopy(base.downRightRows);
    this.numTilesEachState = new HashMap<>();
    numTilesEachState.putAll(base.numTilesEachState);
  }

  // deep copy a list of list of cells
  private List<List<Cell>> dirCopy(List<List<Cell>> dir) {
    return dir.stream()
        .map((List<Cell> cells) -> cells.stream()
            .map(Cell::new)
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  /*
    4x4 grid with each coordinate for each point
    key -> (hrow,hcol;vrow,vcol;drr,drc;dlr,dlc)
    (0,0;0,0;3,0;0,0) (0,1;1,0;4,0;1,0) (0,2;2,0;5,0;2,0) (0,3;3,0;6,0;3,0)

    (1,0;0,1;2,0;1,1) (1,1;1,1;3,1;2,1) (1,2;2,1;4,1;3,1) (1,3;3,1;5,1;4,0)

    (2,0;0,2;1,0;2,2) (2,1;1,2;2,1;3,2) (2,2;2,2;3,2;4,1) (2,3;3,2;4,2;5,0)

    (3,0;0,3;0,0;3,3) (3,1;1,3;1,1;4,2) (3,2;2,3;2,2;5,1) (3,3;3,3;3,3;6,0)
   */


  // set the given location in both lists as an empty cell
  private void setEmptyCellAt(int row, int col) {
    Cell c = new Cell(new Location(row, col));
    horizontalRows.get(row).set(col, c);
    verticalRows.get(col).set(row, c);
    downRightRows.get(drr(row, col)).set(drc(row, col), c);
    downLeftRows.get(dlr(row, col)).set(dlc(row, col), c);
    incrementTileCount(CellState.EMPTY);
  }

  // coordinate conversion functions from horizontal row coordinate to down right and down left
  // row coordinates
  int drr(int row, int col) {
    return sideLength - row - 1 + col;
  }

  int drc(int row, int col) {
    return drr(row, col) < sideLength ? col : row;
  }

  int dlr(int row, int col) {
    return col + row;
  }

  int dlc(int row, int col) {
    return dlr(row, col) < sideLength ? row : sideLength - col - 1;
  }

  private void incrementTileCount(CellState player) {
    numTilesEachState.put(player, numTilesEachState.get(player) + 1);
  }

  private void decrementTileCount(CellState player) {
    numTilesEachState.put(player, numTilesEachState.get(player) - 1);
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
    return new SquareBoard(this);
  }

  @Override
  public int numTiles(CellState state) {
    return numTilesEachState.get(state);
  }

  @Override
  public List<List<List<Cell>>> getBoardInAllDirections() {
    return List.of(horizontalRows,
        verticalRows,
        downRightRows,
        downLeftRows);
  }

  @Override
  public List<Location> getLocationInAllDirections(Location location) {
    return List.of(location,
        new Location(location.getColumn(), location.getRow()),
        new Location(drr(location.getRow(), location.getColumn()),
            drc(location.getRow(), location.getColumn())),
        new Location(dlr(location.getRow(), location.getColumn()),
            dlc(location.getRow(), location.getColumn())));
  }

  @Override
  public int sideLength() {
    return this.sideLength;
  }

  @Override
  public void reset() {
    // set all cells to empty
    numTilesEachState.put(CellState.EMPTY, 0);
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
    return BoardType.SQUARE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SquareBoard that = (SquareBoard) o;
    return sideLength == that.sideLength
        && Objects.equals(horizontalRows, that.horizontalRows)
        && Objects.equals(verticalRows, that.verticalRows)
        && Objects.equals(numTilesEachState, that.numTilesEachState)
        && Objects.equals(starterBlackTiles, that.starterBlackTiles)
        && Objects.equals(starterWhiteTiles, that.starterWhiteTiles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(horizontalRows, verticalRows, numTilesEachState, sideLength,
        starterBlackTiles, starterWhiteTiles);
  }

  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    for (int rowNum = 0; rowNum < sideLength; rowNum++) {
      StringBuilder rowStr = new StringBuilder();
      for (int col = 0; col < sideLength; col++) {
        Cell cell = horizontalRows.get(rowNum).get(col);
        output.append(cell.toString()).append(" ");
      }
      output.append("\n");
    }
    return output.toString();
  }
}
