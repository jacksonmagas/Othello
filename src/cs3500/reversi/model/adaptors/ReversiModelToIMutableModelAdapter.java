package cs3500.reversi.model.adaptors;

import static cs3500.reversi.model.adaptors.ProviderEnumConverter.disc;
import static cs3500.reversi.model.adaptors.ProviderEnumConverter.state;
import static cs3500.reversi.model.adaptors.ProviderEnumConverter.status;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.provider.controller.IModelFeatures;
import cs3500.reversi.provider.model.Board;
import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.IMutableModel;
import cs3500.reversi.provider.model.IROModel;
import cs3500.reversi.provider.model.PlayerDisc;
import cs3500.reversi.provider.model.Status;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This class is an adaptor for a model that implements ReversiModel to allow it to be used
 * in IROModel based APIs.
 */
public class ReversiModelToIMutableModelAdapter implements IMutableModel {
  private final ReversiModel base;
  private final CoordinateConverter converter;
  private Hex latestMove;


  /**
   * Create a new IROModel which adapts the given ReversiModel to implement the IROModel interface.
   * @param base the reversi game to adapt
   */
  public ReversiModelToIMutableModelAdapter(ReversiModel base) {
    this.base = base;
    converter = new CoordinateConverter(base.sideLength());
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
    HashMap<Hex, PlayerDisc> boardAsMep = new HashMap<>();
    for (int row = 0; row < base.getRows(); row++) {
      for (int col = 0; col < base.getColumns(row); col++) {
        boardAsMep.put(converter.hexFromRowCol(row, col), disc(base.getStateAt(row, col)));
      }
    }
    return new Board(boardAsMep);
  }

  @Override
  public PlayerDisc getNextPlayerTurn() {
    // while logically this would return the opposite of the current player it ends up being called
    // at the start of a player's turn, so it led to the wrong player displayed
    return disc(base.getCurrentPlayer());
  }

  @Override
  public int countDiscs(PlayerDisc playerDisc) {
    return base.getPlayerScore(state(playerDisc));
  }

  @Override
  public PlayerDisc getDiscAt(int coordQ, int coordR) {
    return disc(base.getStateAt(converter.rowFromAxial(coordR),
        converter.colFromAxial(coordQ, coordR)));
  }

  @Override
  public int getNumOfLegalMovesForCurrentPlayer() {
    return base.getLegalMoves().size();
  }

  @Override
  public boolean isLegal(int q, int r) {
    try {
      return base.getLegalMoves().stream()
              .anyMatch((Move m) -> m.getPosn().row == converter.rowFromAxial(r)
                      && m.getPosn().col == converter.colFromAxial(q, r));
    } catch (Exception ex) {
      return false;
    }
  }

  @Override
  public List<Hex> getInboundNeighbors(int q, int r) {
    List<Hex> neighbors = new ArrayList<>();
    neighbors.add(new Hex(q + 1, r));
    neighbors.add(new Hex(q - 1, r));
    neighbors.add(new Hex(q, r + 1));
    neighbors.add(new Hex(q, r - 1));
    neighbors.add(new Hex(q + 1, r - 1));
    neighbors.add(new Hex(q - 1, r + 1));
    return neighbors.stream()
        .filter((Hex h) -> h.isWithinGrid(getRadius()))
        .collect(Collectors.toList());
  }

  @Override
  public List<List<Hex>> getAllValidPaths(Hex startHex) {
    try {
      ReversiModel copy = base.copy();
      copy.makeMove(converter.rowFromAxial(startHex.getR()),
          converter.colFromAxial(startHex.getQ(), startHex.getR()));
      List<Hex> changedHexes = new ArrayList<>();
      for (int row = 0; row < base.getRows(); row++) {
        for (int col = 0; col < base.getColumns(row); col++) {
          if (base.getStateAt(row, col) != copy.getStateAt(row, col)) {
            changedHexes.add(converter.hexFromRowCol(row, col));
          }
        }
      }
      changedHexes.remove(startHex);
      // function classifies a hex based on which axis it shares with the start hex
      // and whether a second axis is greater than that axis of the start hex
      Function<Hex, Integer> dirClassifier = (Hex h) -> {
        if (startHex.getQ() == h.getQ()) {
          return startHex.getR() > h.getR() ? 0 : 1;
        } else if (startHex.getR() == h.getR()) {
          return startHex.getQ() > h.getQ() ? 2 : 3;
        } else {
          return startHex.getQ() > h.getQ() ? 4 : 5;
        }
      };
      return new ArrayList<>(changedHexes.stream()
          .collect(Collectors.groupingBy(dirClassifier))
          .values());
    } catch (IllegalArgumentException e) {
      return List.of(new ArrayList<>());
    }
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
    return new ReversiModelToIMutableModelAdapter(base.copy());
  }

  @Override
  public boolean isGameStarted() {
    return base.getStatus() != cs3500.reversi.model.Status.NotStarted;
  }

  @Override
  public void startGame(boolean initializeGrid) {
    // ignore parameter unless needed for functionality
    base.startGame();
  }

  @Override
  public void makeMove(int coordQ, int coordR, PlayerDisc who) {
    if (state(who) != base.getCurrentPlayer()) {
      throw new IllegalStateException("Not this player's turn");
    }
    try {
      base.makeMove(converter.rowFromAxial(coordR),
          converter.colFromAxial(coordQ, coordR));
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
