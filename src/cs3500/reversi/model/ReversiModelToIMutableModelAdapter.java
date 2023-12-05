package cs3500.reversi.model;

import cs3500.reversi.provider.controller.IModelFeatures;
import cs3500.reversi.provider.model.Board;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.IMutableModel;
import cs3500.reversi.provider.model.IROModel;
import cs3500.reversi.provider.model.PlayerDisc;
import cs3500.reversi.provider.model.Status;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * This class is an adaptor for a model that implements ReversiModel to allow it to be used
 * in IROModel based APIs.
 */
public class ReversiModelToIMutableModelAdapter implements IMutableModel {
  private final ReversiModel base;
  private Hex latestMove;
  private final Map<CellState, PlayerDisc> stateMap;
  private final Map<PlayerDisc, CellState> discMap;
  private final Map<cs3500.reversi.model.Status, cs3500.reversi.provider.model.Status> statusMap;

  /**
   * Create a new IROModel which adapts the given ReversiModel to implement the IROModel interface.
   * @param base the reversi game to adapt
   */
  public ReversiModelToIMutableModelAdapter(ReversiModel base) {
    this.base = base;
    this.stateMap = new HashMap<>();
    this.discMap = new HashMap<>();
    this.statusMap = new HashMap<>();
    initEnumConversionLookup();
  }

  /**
   * Initialize maps storing the bidirectional correspondence between CellStates and PlayerDiscs.
   */
  private void initEnumConversionLookup() {
    this.stateMap.put(CellState.BLACK, PlayerDisc.BLACK);
    this.stateMap.put(CellState.WHITE, PlayerDisc.WHITE);
    this.stateMap.put(CellState.EMPTY, PlayerDisc.EMPTY);
    this.discMap.put(PlayerDisc.EMPTY, CellState.EMPTY);
    this.discMap.put(PlayerDisc.WHITE, CellState.WHITE);
    this.discMap.put(PlayerDisc.BLACK, CellState.BLACK);
    this.statusMap.put(cs3500.reversi.model.Status.Playing, Status.Playing);
    this.statusMap.put(cs3500.reversi.model.Status.Won, Status.Won);
    this.statusMap.put(cs3500.reversi.model.Status.Tied, Status.Stalemate);
  }

  /**
   * Convert a CellState to the equivalent PlayerDisc.
   * @param state The CellState to convert
   * @return The corresponding PlayerDisc
   */
  private PlayerDisc disc(CellState state) {
    return stateMap.get(state);
  }

  /**
   * Convert a PlayerDisc to the equivalent CellState.
   * @param disc The PlayerDisc to convert
   * @return The corresponding CellState
   */
  private CellState state(PlayerDisc disc) {
    return discMap.get(disc);
  }

  /**
   * Convert a ReversiModel implementation of status to a provider implementation of status.
   * When the status is not started an exception is thrown because the provider implementation of
   * status does not include a not started status, and expects exceptions instead.
   * @param status the status to convert
   * @return the converted status
   * @throws IllegalStateException if the status is Status.NotStarted
   */
  private Status status(cs3500.reversi.model.Status status) {
    if (status == cs3500.reversi.model.Status.NotStarted) {
      throw new IllegalStateException("Game not started");
    } else {
      return statusMap.get(status);
    }
  }

  // TODO: write coordinate conversion function
  private int row(int q, int r) {
    return 0;
  }

  private int col(int q, int r) {
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return base.isGameOver();
  }

  @Override
  public int getScore(PlayerDisc p) {
    return base.getPlayerScore(state(p));
  }

  @Override
  public PlayerDisc getWinner() {
    Supplier<IllegalStateException> illegalStateNotWon =
        () -> new IllegalStateException("Game is not won.");
    return disc(base.getWinner().orElseThrow(illegalStateNotWon));
  }

  @Override
  public Status getStatus() {
    return status(base.getStatus());
  }

  @Override
  public int getRadius() {
    // the radius is the number of rings around the center hex, which is 1 less than the side
    // length
    return base.sideLength() - 1;
  }

  @Override
  public boolean isBoardFull() {
    return base.getGameBoard().stream()
        .flatMap(Collection::stream)
        .allMatch((CellState c) -> c != CellState.EMPTY);
  }

  @Override
  public Board getBoard() {
    //TODO
    return null;
  }

  @Override
  public PlayerDisc getNextPlayerTurn() {
    return disc(base.getCurrentPlayer().opposite());
  }

  @Override
  public int countDiscs(PlayerDisc playerDisc) {
    return base.getPlayerScore(state(playerDisc));
  }

  @Override
  public PlayerDisc getDiscAt(int coordQ, int coordR) {
    return disc(base.getPieceAt(row(coordQ, coordR), col(coordQ, coordR)));
  }

  @Override
  public int getNumOfLegalMovesForCurrentPlayer() {
    return base.getLegalMoves().size();
  }

  @Override
  public boolean isLegal(int q, int r) {
    return base.getLegalMoves().stream()
        .anyMatch((Move m) -> m.getPosn().row == row(q, r) && m.getPosn().col == col(q, r));
  }

  @Override
  public List<Hex> getInboundNeighbors(int q, int r) {
    //TODO
    return null;
  }

  @Override
  public List<List<Hex>> getAllValidPaths(Hex startHex) {
    //TODO
    return null;
  }

  @Override
  public Optional<Hex> getLatestMove() {
    return Optional.ofNullable(latestMove);
  }

  @Override
  public void addFeatures(IModelFeatures features) {
    //TODO: this is either a stub implementation that can stay empty or it will need to add
    //TODO: YourTurnListeners to the base.
  }

  @Override
  public IMutableModel getPreviousModel(IROModel currentModel) {
    // todo
    return null;
  }

  @Override
  public boolean isGameStarted() {
    return base.getStatus() != cs3500.reversi.model.Status.NotStarted;
  }

  @Override
  public void startGame(boolean initializeGrid) {
    // todo
  }

  @Override
  public void makeMove(int coordQ, int coordR, PlayerDisc who) {
    if (state(who) != base.getCurrentPlayer()) {
      throw new IllegalStateException("Not this player's turn");
    }
    try {
      base.makeMove(row(coordQ, coordR), col(coordQ, coordR));
      latestMove = new Hex(coordQ, coordR);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("No legal move in the given hex.", e);
    }
  }

  @Override
  public void skipTurn(PlayerDisc who) {
    if (state(who) != base.getCurrentPlayer()) {
      throw new IllegalStateException("Not this player's turn");
    }
    base.passTurn();
  }

  @Override
  public void notifyPlayerTurn(PlayerDisc player) {
    // stub implementation does nothing because the base takes care of notifying players
  }

  @Override
  public void notifyGameOver(String winner) {
    // stub implementation does nothing because the base takes care of notifying players
    // TODO: this might actually be needed at some point for their view
  }
}
