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
  private final ReversiBoard board;
  private final int totalNumRows;

  private CellState currentPlayer;

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
  public BasicReversi(BoardType type, int sideLength, boolean lastPlayerPassed, CellState currentPlayer,
      List<Cell.Location> blackTiles, List<Cell.Location> whiteTiles) {
    if (sideLength < 3) {
      throw new IllegalArgumentException("sideLength should be at-least 3 or above !");
    }
    this.quitGame = false;
    this.restartGame = false;
    this.playerHints = new HashMap<>();
    this.lastPlayerPassed = lastPlayerPassed;
    this.gameState = Status.NotStarted;

    // setup correct board
    switch (type) {
      case HEXAGON:
        this.board = new HexagonBoard(sideLength, blackTiles, whiteTiles);
        break;
      case SQUARE:
        this.board = new SquareBoard(sideLength, blackTiles, whiteTiles);
        break;
      default:
        throw new IllegalArgumentException("Unsupported Board Type");
    }

    totalNumRows = 2 * sideLength - 1;

    playerHints.put(CellState.BLACK, false);
    playerHints.put(CellState.WHITE, false);

    //set first move
    this.currentPlayer = currentPlayer;
  }

  /**
   * Constructor for BasicReversi public class in default start game configuration.
   * @param type the board type of the reversi game, either square or hexagonal
   * @param sideLength The length of one side of the hexagon board
   */
  public BasicReversi(BoardType type, int sideLength) {
    if (sideLength < 3) {
      throw new IllegalArgumentException("sideLength should be at-least 3 or above !");
    }
    this.quitGame = false;
    this.restartGame = false;
    this.playerHints = new HashMap<>();
    this.lastPlayerPassed = false;
    this.gameState = Status.NotStarted;

    // setup correct board
    switch (type) {
      case HEXAGON:
        this.board = new HexagonBoard(sideLength);
        break;
      case SQUARE:
        this.board = new SquareBoard(sideLength);
        break;
      default:
        throw new IllegalArgumentException("Unsupported Board Type");
    }

    totalNumRows = 2 * sideLength - 1;

    playerHints.put(CellState.BLACK, false);
    playerHints.put(CellState.WHITE, false);

    //set first move
    this.currentPlayer = CellState.BLACK;
  }

  /**
   * Construct a new BasicReversi which is a copy of the base.
   */
  private BasicReversi(BasicReversi base) {
    this.board = base.board.copy();
    this.restartGame = base.restartGame;
    this.totalNumRows = base.totalNumRows;
    this.lastPlayerPassed = base.lastPlayerPassed;
    this.playerHints = new HashMap<>(base.playerHints);
    this.quitGame = base.quitGame;
    this.gameState = base.gameState;
    this.winner = base.winner;
    this.currentPlayer = base.currentPlayer;
    this.lastErrorMessage = base.lastErrorMessage;
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
    //System.out.println("Player "+player.toString()+" hints are present as "+ hints);
    return hints;
  }

  @Override
  public BoardType getBoardType() {
    return board.getType();
  }

  @Override
  public void togglePlayerHints(CellState player) {
    Boolean hints = playerHints.get(player);
    if (hints == null) {
      hints = Boolean.FALSE;
    }
    hints = hints ? Boolean.FALSE : Boolean.TRUE;
    playerHints.put(player, hints);
    //System.out.println("Player "+player.toString()+" hints are now "+ hints);
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
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  // Given a direction grid and coordinates find if pieces of the current color sandwich
  // some number of pieces of the other color along this axis.
  private boolean isValidMoveInThisDirection(List<List<Cell>> direction,
      Location location) {
    return isValidInThisDirectionMinus(direction, location.getRow(), location.getColumn())
        || isValidInThisDirectionPlus(direction, location.getRow(), location.getColumn());
  }

  // Given a direction grid and coordinates find if pieces of the current color sandwich
  // some number of pieces of the other color along this axis in the increasing index direction.
  private boolean isValidInThisDirectionPlus(List<List<Cell>> direction,
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
  private boolean isValidInThisDirectionMinus(List<List<Cell>> direction,
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

  private boolean allyCellOnEnd(List<List<Cell>> direction, int row,
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
      // I would have liked to do this functionally but the stream interface has no zip method
      boolean isValid = false;
      List<List<List<Cell>>> dirs = board.getBoardInAllDirections();
      List<Location> location = board.getLocationInAllDirections(new Location(hRow, hIndex));
      for (int i = 0; i < dirs.size(); i++) {
        isValid |= isValidMoveInThisDirection(dirs.get(i), location.get(i));
      }
      // location to move in is empty and there is a valid direction to flip
      return board.getCellAt(new Location(hRow, hIndex)).isEmpty()
              && isValid;
    } catch (Exception ex) {
      return false;
    }
  }

  //Overload that works with moves
  private boolean isValidMove(Move m) {
    return isValidMove(m.getPosn().row, m.getPosn().col);
  }

  // flips the player disc if the move is valid
  private void flipTilesIfValid(List<List<Cell>> direction,
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
    List<List<List<Cell>>> dirs = board.getBoardInAllDirections();
    List<Location> location = board.getLocationInAllDirections(new Location(hRow, hIndex));
    for (int i = 0; i < dirs.size(); i++) {
      flipTilesIfValid(dirs.get(i), location.get(i).getRow(), location.get(i).getColumn());
    }
  }

  // gives an empty tile space to the current player
  private void setEmptyTileToCurrentPlayer(int row, int index) {
    board.placeDisc(new Location(row, index), currentPlayer);
    setWinOrTieGame(false);
  }

  // sets the game to win or tie
  private void setWinOrTieGame(boolean passTurnsReached) {
    if (passTurnsReached) {
      if (board.numTiles(CellState.BLACK) == board.numTiles(CellState.WHITE)) {
        this.gameState = Status.Tied;
      } else {
        this.gameState = Status.Won;
        this.winner = board.numTiles(CellState.BLACK) > board.numTiles(CellState.WHITE)
                ? CellState.BLACK : CellState.WHITE;
      }
    }
  }

  // flips the tile to the current player
  private void flipTileToCurrentPlayer(List<List<Cell>> direction, int row, int index) {
    board.placeDisc(direction.get(row).get(index).getLocation(), this.currentPlayer);
    setWinOrTieGame(false);
  }

  /**
   * Gets the piece at a row and col location.
   */
  @Override
  public CellState getStateAt(int row, int col) {
    return board.getCellAt(new Location(row, col)).getState();
  }

  @Override
  public int getColumns(int row) {
    return board.getBoardInAllDirections().get(0).get(row).size();
  }

  @Override
  public int getRows() {
    return board.getBoardInAllDirections().get(0).size();
  }

  // makes the move a player prompted
  @Override
  public void makeMove(int row, int index) {
    if (row < 0 || index < 0) {
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
    return board.numTiles(player);
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
    return this.board.getBoardInAllDirections().get(0).stream()
        .map((List<Cell> l) -> l.stream()
            .map(Cell::getState)
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  /**
   * Gets the side length of a game board.
   */
  @Override
  public int sideLength() {
    //center is sideLength - 1
    return this.board.sideLength();
  }

  /**
   * Checks to see if there are any legal moves.
   */
  @Override
  public boolean anyLegalMoves() {
    int rowSize = board.sideLength();
    for (int hRow = 0; hRow < this.totalNumRows; hRow++) {
      for (int hIndex = 0; hIndex < rowSize; hIndex++) {
        if (isValidMove(hRow, hIndex)) {
          return true;
        }
      }
      if (hRow < board.sideLength() - 1) {
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

  // returns the output in a string format
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    output.append(board.toString());

    output.append("Player one Score: ").append(board.numTiles(CellState.BLACK)).append("\n");
    output.append("Player two Score: ").append(board.numTiles(CellState.WHITE)).append("\n");
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
    this.gameState = Status.Playing;
    this.lastPlayerPassed = false;
    this.board.reset();
    playerHints.put(CellState.BLACK, false);
    playerHints.put(CellState.WHITE, false);
    this.currentPlayer = CellState.BLACK;
    notifyStateChanged();
  }

  //take a cell and get the move that would go in that cell for that cell
  private Move cellToMove(Cell c) {
    return new Move(c.getLocation().getRow(), c.getLocation().getColumn());
  }

  @Override
  public List<Move> getLegalMoves() {
    BinaryOperator<List<Cell>> collapseNestedList;
    collapseNestedList = (List<Cell> row1, List<Cell> row2) -> {
      row1.addAll(row2);
      return row1;
    };

    // get a list of all possible moves then filter by valid moves
    List<Move> result = board.getBoardInAllDirections().get(0).stream()
        .reduce(new ArrayList<>(), collapseNestedList).stream()
        .map(this::cellToMove)
        .filter(this::isValidMove)
        .collect(Collectors.toList());
    // add passing as an option
    result.add(new Move(true, false, false, false));

    return result;
  }
}
