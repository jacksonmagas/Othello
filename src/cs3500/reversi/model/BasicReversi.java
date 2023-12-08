package cs3500.reversi.model;

import cs3500.reversi.controller.YourTurnListener;
import cs3500.reversi.model.Cell.Location;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
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

  private final int center;

  private final int totalNumRows;
  private int currentRow;
  private int currentCol;

  private CellState currentPlayer;

  private final Map<CellState, Integer> playerScores;
  private final Map<CellState, Boolean> playerHints;

  private boolean lastPlayerPassed;

  private String lastErrorMessage;

  private volatile Status gameState;
  private volatile boolean restartGame;

  private CellState winner;
  boolean quitGame;

  private final List<YourTurnListener> listeners = new ArrayList<>();


  /**
   * Detailed constructor for reversi game that allows creating a specified board state.
   * @param sideLength the side length of the board
   * @param lastPlayerPassed whether the last player passed their turn
   * @param currentPlayer the player whose move it is
   * @param blackTiles a list of the locations to put black tiles
   * @param whiteTiles a list of the locations to put white tiles
   */
  public BasicReversi(int sideLength, boolean lastPlayerPassed, CellState currentPlayer,
      List<Cell.Location> blackTiles, List<Cell.Location> whiteTiles) {
    if (sideLength < 3) {
      throw new IllegalArgumentException("sideLength should be at-least 3 or above !");
    }
    this.quitGame = false;
    this.restartGame = false;
    this.horizontalRows = new ArrayList<>();
    this.downRightRows = new ArrayList<>();
    this.downLeftRows = new ArrayList<>();
    this.playerScores = new HashMap<>();
    this.playerHints = new HashMap<>();
    this.lastPlayerPassed = lastPlayerPassed;
    //initialize game
    this.center = sideLength - 1;
    this.gameState = Status.NotStarted;

    int rowSize = sideLength;
    totalNumRows = 2 * sideLength - 1;

    playerScores.put(CellState.BLACK, 0);
    playerScores.put(CellState.WHITE, 0);
    playerScores.put(CellState.EMPTY, 0);

    playerHints.put(CellState.BLACK, false);
    playerHints.put(CellState.WHITE, false);

    //create empty arrayLists to hold cells, and temporarily fills them will nulls
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

    rowSize = sideLength;
    // build grid
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



    //Place player discs
    for (Cell.Location l : blackTiles) {
      horizontalRows.get(l.row).get(l.column).setState(CellState.BLACK);
      incrementScore(CellState.BLACK);
    }

    for (Cell.Location l : whiteTiles) {
      horizontalRows.get(l.row).get(l.column).setState(CellState.WHITE);
      incrementScore(CellState.WHITE);
    }

    //set first move
    this.currentPlayer = currentPlayer;
    System.out.println("Initial model\n" + this);
  }

  /**
   * Constructor for BasicReversi public class in default start game configuration.
   * @param sideLength The length of one side of the hexagon board
   */
  public BasicReversi(int sideLength) {
    this(sideLength, false, CellState.BLACK,
        List.of(new Location(sideLength - 2, sideLength - 2),
            new Location(sideLength - 1, sideLength),
            new Location(sideLength, sideLength - 2)),
        List.of(new Location(sideLength - 2, sideLength - 1),
            new Location(sideLength - 1, sideLength - 2),
            new Location(sideLength, sideLength - 1)));
  }

  /**
   * Construct a new BasicReversi which is a copy of the base.
   */
  public BasicReversi(BasicReversi base) {
    this.center = base.center;
    this.totalNumRows = base.totalNumRows;
    this.lastPlayerPassed = base.lastPlayerPassed;
    this.playerScores = new HashMap<>(base.playerScores);
    this.playerHints = new HashMap<>(base.playerHints);
    this.horizontalRows = copyCells(base.horizontalRows);
    this.downLeftRows = copyCells(base.downLeftRows);
    this.downRightRows = copyCells(base.downRightRows);
    this.quitGame = base.quitGame;
    //this.isGameOver = base.isGameOver;
    this.gameState = base.gameState;
    this.winner = base.winner;
    this.currentPlayer = base.currentPlayer;
    this.lastErrorMessage = base.lastErrorMessage;
    this.currentRow = base.currentRow;
    this.currentCol = base.currentCol;
  }

  @Override
  public void startGame() {
    if (this.gameState == null || this.gameState != Status.NotStarted) {
      throw new IllegalStateException("Game is already started!");
    } else {
      this.gameState = Status.Playing;
      notifyStateChanged();
      notifyPlayer();
    }
  }

  @Override
  public boolean isPlayerHintsEnabled(CellState player) {
    Boolean hints = playerHints.get(player);
    if (hints == null) {
      return false;
    }
    System.out.println("Player "+player.toString()+" hints are present as "+ hints);
    return hints;
  }

  @Override
  public void togglePlayerHints(CellState player) {
    Boolean hints = playerHints.get(player);
    if (hints == null) {
      hints = Boolean.FALSE;
    }
    hints = hints ? Boolean.FALSE : Boolean.TRUE;
    playerHints.put(player, hints);
    System.out.println("Player "+player.toString()+" hints are now "+ hints);
  }

  private void incrementScore(CellState player) {
    playerScores.put(player, playerScores.get(player) + 1);
  }

  private void decrementScore(CellState player) {
    playerScores.put(player, playerScores.get(player) - 1);
  }

  //returns a new list containing copies of the cells
  private ArrayList<ArrayList<Cell>> copyCells(ArrayList<ArrayList<Cell>> original) {
    return original.stream()
        .map((ArrayList<Cell> c) -> c.stream()
            .map(Cell::new)
            .collect(Collectors.toCollection(ArrayList::new)))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  // put the cell with the given horizontal row and index into the correct location in all three
  // lists
  private void setEmptyCellAt(int hRow, int hIndex) {
    Cell c = new Cell(new Location(hRow, hIndex));
    horizontalRows.get(hRow).set(hIndex, c);
    downRightRows.get(getRRow(hRow, hIndex)).set(getRIndex(hRow, hIndex), c);
    downLeftRows.get(getLRow(hRow, hIndex)).set(getLIndex(hRow, hIndex), c);
    incrementScore(CellState.EMPTY);
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

  /**
   * Notify all controllers that the game is over.
   */
  public void notifyStateChanged() {
    // Notify controllers to refresh view
    for (YourTurnListener listener : listeners) {
      if (listener != null) {
        listener.gameOver();
      }
    }
  }

  /**
   * This function is the core game loop, notifying each player of their turn.
   */
  private void notifyPlayer() {
    while (!this.isGameOver() && !listeners.isEmpty()) {
      // Notify controller that has current turn
      listeners.stream()
          .filter((YourTurnListener l) -> l.getPlayer().equals(this.currentPlayer))
          .forEach(YourTurnListener::yourTurn);
      if (restartGame) {
        newGame();
      }
    }
    while (isGameOver() && !quitGame) {
      Thread.onSpinWait();
      if (restartGame) {
        newGame();
        notifyPlayer();
      }
    }
  }

  @Override
  public void addYourTurnListener(YourTurnListener listener) {
    // avoid double notifying
    if(!listeners.contains(listener)) {
      listeners.add(listener);
    }
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
    try {
      CellState oppositeColor = this.currentPlayer.opposite();
      //Is there an adjacent opponent cell in positive direction?
      if (index < rowLen - 1) {
        Cell nextCell = direction.get(row).get(index + 1);
        validInPlus = nextCell != null && nextCell.getState() != null && nextCell.getState()
                .equals(oppositeColor);
      }

      //Is there an ally cell on the other side of opponent cells?
      // set invalid then only set back to valid if there is a current player cell before
      // empty cell or end of the list
      if (validInPlus) {
        validInPlus = allyCellOnEnd(direction, row, index + 1, rowLen);
      }
    } catch (Exception ex) {
      return false;
    }
    return validInPlus;
  }

  // Given a direction grid and coordinates find if pieces of the current color sandwich
  // some number of pieces of the other color along this axis in the decreasing index direction.
  private boolean isValidInThisDirectionMinus(ArrayList<ArrayList<Cell>> direction,
      int row,
      int index) {
    boolean validInMinus = false;
    try {
      CellState oppositeColor = this.currentPlayer.opposite();
      //Is there an adjacent opponent cell in negative direction?
      if (index > 0 && !direction.get(row).isEmpty()) {
        Cell prevCell = direction.get(row).get(index - 1);
        validInMinus = prevCell.getState().equals(oppositeColor);
      }
      //If is there an ally cell on the other side of opponent cells?
      if (validInMinus) {
        validInMinus = allyCellOnEnd(direction, row, index - 1, 0);
      }
    } catch (Exception ex) {
      return false;
    }
    return validInMinus;
  }

  private boolean allyCellOnEnd(ArrayList<ArrayList<Cell>> direction, int row,
      int from, int to) {
    boolean valid;
    valid = false;
    boolean startGreater = from > to;
    Predicate<Integer> endCondition =
        (Integer i) -> (startGreater && i >= to) || (!startGreater && i < to);
    int step = startGreater ? -1 : 1;
    for (int newIndex = from; endCondition.test(newIndex); newIndex += step) {
      CellState curCellState = direction.get(row).get(newIndex).getState();
      if (curCellState == CellState.EMPTY
              || (newIndex == 0 && curCellState != this.currentPlayer)) {
        break;
      } else if (curCellState == this.currentPlayer) {
        valid = true;
        break;
      }
    }
    return valid;
  }

  // A move is valid if all the following are true:
  // - the cell to move in is empty
  // - the cell to move in is adjacent to the one of the opponent player's cells
  // - in that direction there is a friendly player cell before empty cell/end of list
  private boolean isValidMove(int hRow, int hIndex) {
    try {
      return getCellAt(hRow, hIndex).isEmpty()
              && (isValidMoveInThisDirection(this.horizontalRows, hRow, hIndex)
              || isValidMoveInThisDirection(this.downRightRows,
              getRRow(hRow, hIndex),
              getRIndex(hRow, hIndex))
              || isValidMoveInThisDirection(this.downLeftRows,
              getLRow(hRow, hIndex),
              getLIndex(hRow, hIndex)));
    //} catch (IndexOutOfBoundsException ex) {
    } catch (Exception ex) {
      return false;
    }
  }

  //Overload that works with moves
  private boolean isValidMove(Move m) {
    return isValidMove(m.getPosn().row, m.getPosn().col);
  }

  // flips the player disc if the move is valid
  private void flipTilesIfValid(ArrayList<ArrayList<Cell>> direction,
                                             int row, int index) {
    CellState oppositeColor = this.currentPlayer.opposite();
    // valid so flip tile now
    if (isValidInThisDirectionMinus(direction, row, index)) {
      int newIndex = index - 1;
      CellState curCellState = direction.get(row).get(newIndex).getState();
      while (curCellState == oppositeColor && newIndex >= 0) {
        flipTileToCurrentPlayer(direction, row, newIndex);
        newIndex--;
        if (newIndex >= 0) {
          curCellState = direction.get(row).get(newIndex).getState();
        }
      }
    }

    // valid so flip tile now
    if (isValidInThisDirectionPlus(direction, row, index)) {
      int newIndex = index + 1;
      CellState curCellState = direction.get(row).get(newIndex).getState();
      while (curCellState == oppositeColor && direction.get(row).size() > newIndex) {
        flipTileToCurrentPlayer(direction, row, newIndex);
        newIndex++;
        if (direction.get(row).size() > newIndex) {
          curCellState = direction.get(row).get(newIndex).getState();
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
    incrementScore(this.currentPlayer);
    setWinOrTieGame(false);
  }

  // sets the game to win or tie
  private void setWinOrTieGame(boolean passTurnsReached) {
    if (passTurnsReached) {
      if (playerScores.get(CellState.BLACK).equals(playerScores.get(CellState.WHITE))) {
        this.gameState = Status.Tied;
      } else {
        this.gameState = Status.Won;
        this.winner = (playerScores.get(CellState.BLACK) > playerScores.get(CellState.WHITE))
                ? CellState.BLACK : CellState.WHITE;
      }
    }
  }

  // flips the tile to the current player
  private void flipTileToCurrentPlayer(ArrayList<ArrayList<Cell>> direction, int row, int index) {
    direction.get(row).get(index).setState(this.currentPlayer);
    incrementScore(this.currentPlayer);
    decrementScore(this.currentPlayer.opposite());
    setWinOrTieGame(false);
  }

  /**
   * Gets the piece at a row and col location.
   */
  @Override
  public CellState getStateAt(int row, int col) {
    if (row < 0 || row >= this.horizontalRows.size()) {
      lastErrorMessage = "Bad row: " + row;
      throw new IllegalArgumentException("Bad row: " + row);
    }
    if (col < 0 || col >= horizontalRows.get(row).size()) {
      lastErrorMessage = "Bad column: " + col;
      throw new IllegalArgumentException("Bad col: " + col);
    }
    return horizontalRows.get(row).get(col).getState();
  }

  @Override
  public int getColumns(int row) {
    int colsCount = 0;
    if (row < this.horizontalRows.size()) {
      colsCount = horizontalRows.get(row).size();
    }
    return colsCount;
  }

  @Override
  public int getRows() {
    return this.horizontalRows.size();
  }

  // makes the move a player prompted
  @Override
  public void makeMove(int row, int index) {
    if (row < 0 || index < 0) {
      //lastErrorMessage = "Input parameters either row or column is invalid!";
      //throw new IllegalArgumentException("Input parameters either row or column is invalid!");
      lastErrorMessage = "There is no legal move at " + row + ", " + index + ".";
      throw new IllegalArgumentException("There is no legal move at " + row + ", " + index + ".");
    }
    if (isGameOver()) {
      lastErrorMessage = "Game is over!";
      throw new IllegalStateException("Game is over!");
    }
    if (isValidMove(row, index)) {
      setEmptyTileToCurrentPlayer(row, index);
      flipTiles(row, index);
      this.lastPlayerPassed = false;
      switchTurn();
      lastErrorMessage = "";
    } else {
      lastErrorMessage = "There is no legal move at " + row + ", " + index + ".";
      throw new IllegalArgumentException("There is no legal move at " + row + ", " + index + ".");
    }
  }

  @Override
  public void makeMove(Move move) {
    if (move.isPassTurn()) {
      passTurn();
    } else if (move.isRestartGame()) {
      //in single threaded running we can call new game from this thread
      if (this.listeners.isEmpty()) {
        newGame();
      }
      //otherwise we have to get the current move to be restart game so the main thread
      //can restart the game
      restartGame = true;
      // end the current player's turn to allow for restart
      listeners.stream()
          .filter((YourTurnListener l) -> l.getPlayer() == this.getCurrentPlayer())
          .forEach(YourTurnListener::endTurn);
    } else if (move.isQuitGame()) {
      quitGame = true;
    } else if (move.getPosn() != null) {
      makeMove(move.getPosn().row, move.getPosn().col);
    }
  }

  /**
   * Passes the turn of the player if there are no legal moves left for that player.
   */
  @Override
  public void passTurn() {
    if (isGameOver()) {
      lastErrorMessage = "Game is over!";
      throw new IllegalStateException("Game is over!");
    }
    if (lastPlayerPassed) {
      this.setWinOrTieGame(true);
    } else {
      lastPlayerPassed = true;
      switchTurn();
    }
    lastErrorMessage = "";
  }

  // gets the score of the game for the given player
  @Override
  public int getPlayerScore(CellState player) {
    return this.playerScores.get(player);
  }

  @Override
  public CellState getCurrentPlayer() {
    return this.currentPlayer;
  }

  /**
   * Checks if the game is over.
   */
  @Override
  public boolean isGameOver() {
    return this.gameState == Status.Won || this.gameState == Status.Tied;
  }

  /**
   * Gets the game board.
   */
  @Override
  public List<List<CellState>> getGameBoard() {
    return this.horizontalRows.stream().map((ArrayList<Cell> c) ->
            c.stream().map(Cell::getState).collect(
        Collectors.toList())).collect(Collectors.toList());
  }


  /**
   * Gets the board as an int array.
   */
  @Override
  public int[][] getBoard() {
    //todo: this should be folded into board()
    int bSize = this.totalNumRows; //this.totalNumRows;
    int[][] board = new int[bSize][bSize];
    // initialize with 0s
    for (int i = 0; i < this.horizontalRows.size(); i++) {
      for (int j = 0; j < this.horizontalRows.size(); j++) {
        board[i][j] = 0;
      }
    }

    int rowSize = center + 1;
    for (int rowNum = 0, row2 = 0; rowNum < totalNumRows; rowNum++) {
      int col2 = 0;
      for (int col = 0; col < rowSize; col++) {
        Cell cell = horizontalRows.get(rowNum).get(col);
        int ch = cell.toString().charAt(0);
        if (ch == (int)'_') {
          ch = ' ';
        }
        board[row2][col2++] = ch;
      }

      if (rowNum < center) {
        rowSize++;
      } else {
        rowSize--;
      }
      row2++;
    }

    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board.length; j++) {
        if (board[i][j] == (int)'_') {
          board[i][j] = ' ';
        }
      }
    }
    return board;
  }

  /**
   * Gets the side length of a game board.
   */
  @Override
  public int sideLength() {
    //center is sideLength - 1
    return this.center + 1;
  }

  /**
   * Checks to see if there are any legal moves.
   */
  @Override
  public boolean anyLegalMoves() {
    int rowSize = center + 1;
    for (int hRow = 0; hRow < this.totalNumRows; hRow++) {
      for (int hIndex = 0; hIndex < rowSize; hIndex++) {
        if (isValidMove(hRow, hIndex)) {
          return true;
        }
      }
      if (hRow < center) {
        rowSize++;
      } else {
        rowSize--;
      }
    }
    return false;
  }


  @Override
  public String getNextStepInstructions() {
    StringBuilder instructions = new StringBuilder();
    if (!this.isGameOver()) {
      if (this.currentPlayer.equals(CellState.BLACK)) {
        instructions.append("Player one turn (Black)!\n");
      } else {
        instructions.append("Player two turn (White)!\n");
      }
    } else {
      instructions.append("Game is over!\n");
      if (getStatus() == Status.Won) {
        instructions.append(" Player ").append(getWinnerText()).append(" won");
      }
      else {
        instructions.append(" Tie game");
      }
    }
    return instructions.toString();
  }

  /**
   * Gets the last error message displayed.
   */
  @Override
  public String getLastErrorMessage() {
    return lastErrorMessage != null ? lastErrorMessage : "" ;
  }

  @Override
  public Status getStatus() {
    return this.gameState;
  }

  // gets the winner of the game
  private String getWinnerText() {
    if (!isGameOver()) {
      throw new IllegalStateException("Game isn't over");
    }
    String winnerName;
    if (this.winner == CellState.BLACK) {
      winnerName = "one (Back)";
    } else {
      winnerName = "two (White)";
    }
    return winnerName;
  }

  @Override
  public Optional<CellState> getWinner() {
    return getStatus() == Status.Won ? Optional.of(this.winner) : Optional.empty();
  }

  public void setHighlightedCell(int row, int col) {
    this.currentRow = row;
    this.currentCol = col;
    System.out.println("currentRow " + row + " currentCol " + col);
  }

  public Cell.Location getHighlightedCell() {
    return new Cell.Location(this.currentRow, this.currentCol);
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
    output.append("Player one Score: ").append(playerScores.get(CellState.BLACK)).append("\n");
    output.append("Player two Score: ").append(playerScores.get(CellState.WHITE)).append("\n");
    if (!isGameOver()) {
      if (this.currentPlayer.equals(CellState.BLACK)) {
        output.append("Player one turn (Black)!\n");
      } else {
        output.append("Player two turn (White)!\n");
      }
    } else {
      output.append("Game is over!\n");
      if (getStatus() == Status.Won) {
        output.append(" Player ").append(getWinnerText()).append(" won");
      }
      else {
        output.append(" Tie game");
      }
    }
    //printHorizontalRows();
    return output.toString();
  }

  @Override
  public ReversiModel copy() {
    return new BasicReversi(this);
  }

  @Override
  public void newGame() {
    this.restartGame = false;
    // Keep model reference same but reset all params to start new game
    lastErrorMessage = "";

    //initialize game
    int sideLength = center + 1;
    this.gameState = Status.Playing;

    this.lastPlayerPassed = false;

    int rowSize = sideLength;

    //empty the arrayLists holding cells, and temporarily fills them will nulls
    this.horizontalRows.clear();
    this.downRightRows.clear();
    this.downLeftRows.clear();
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

    rowSize = sideLength;
    // build grid
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

    playerScores.put(CellState.BLACK, 0);
    playerScores.put(CellState.WHITE, 0);

    playerHints.put(CellState.BLACK, false);
    playerHints.put(CellState.WHITE, false);

    //Place player discs
    List<Cell.Location> blackTiles = List.of(new Location(sideLength - 2, sideLength - 2),
            new Location(sideLength - 1, sideLength),
            new Location(sideLength, sideLength - 2));
    for (Cell.Location l : blackTiles) {
      horizontalRows.get(l.row).get(l.column).setState(CellState.BLACK);
      incrementScore(CellState.BLACK);
    }

    List<Cell.Location> whiteTiles = List.of(new Location(sideLength - 2, sideLength - 1),
            new Location(sideLength - 1, sideLength - 2),
            new Location(sideLength, sideLength - 1));
    for (Cell.Location l : whiteTiles) {
      horizontalRows.get(l.row).get(l.column).setState(CellState.WHITE);
      incrementScore(CellState.WHITE);
    }
    this.currentPlayer = CellState.BLACK;
    notifyStateChanged();
  }

  //take a cell and get the move that would go in that cell for that cell
  private Move cellToMove(Cell c) {
    return new Move(c.getLocation().getRow(), c.getLocation().getColumn());
  }

  @Override
  public List<Move> getLegalMoves() {
    BinaryOperator<ArrayList<Cell>> collapseNestedList;
    collapseNestedList = (ArrayList<Cell> row1, ArrayList<Cell> row2) -> {
      row1.addAll(row2);
      return row1;
    };

    List<Move> result = this.horizontalRows.stream()
        .reduce(new ArrayList<>(), collapseNestedList).stream()
        .map(this::cellToMove)
        .filter(this::isValidMove)
        .collect(Collectors.toList());
    result.add(new Move(true, false, false, false));

    return result;
  }
}
