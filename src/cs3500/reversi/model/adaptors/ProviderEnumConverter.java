package cs3500.reversi.model.adaptors;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Status;
import cs3500.reversi.provider.model.PlayerDisc;
import java.util.HashMap;
import java.util.Map;

/**
 * ProviderEnumConvert converts provider enums to work with our implementation.
 */
public class ProviderEnumConverter {
  private static final Map<CellState, PlayerDisc> stateMap = initStateMap();
  private static final Map<PlayerDisc, CellState> discMap = initDiscMap();
  private static final Map<cs3500.reversi.model.Status,
      cs3500.reversi.provider.model.Status> statusMap = initStatusMap();

  private static Map<cs3500.reversi.model.Status,
      cs3500.reversi.provider.model.Status> initStatusMap() {
    Map<cs3500.reversi.model.Status, cs3500.reversi.provider.model.Status> statusMap = new
            HashMap<>();
    statusMap.put(Status.Playing, cs3500.reversi.provider.model.Status.Playing);
    statusMap.put(Status.Won, cs3500.reversi.provider.model.Status.Won);
    statusMap.put(Status.Tied, cs3500.reversi.provider.model.Status.Stalemate);
    return statusMap;
  }

  private static Map<PlayerDisc, CellState> initDiscMap() {
    Map<PlayerDisc, CellState> discMap = new HashMap<>();
    discMap.put(PlayerDisc.EMPTY, CellState.EMPTY);
    discMap.put(PlayerDisc.WHITE, CellState.WHITE);
    discMap.put(PlayerDisc.BLACK, CellState.BLACK);
    return discMap;
  }

  private static Map<CellState, PlayerDisc> initStateMap() {
    Map<CellState, PlayerDisc> stateMap = new HashMap<>();
    stateMap.put(CellState.BLACK, PlayerDisc.BLACK);
    stateMap.put(CellState.WHITE, PlayerDisc.WHITE);
    stateMap.put(CellState.EMPTY, PlayerDisc.EMPTY);
    return stateMap;
  }

  /**
   * Convert a CellState to the equivalent PlayerDisc.
   * @param state The CellState to convert
   * @return The corresponding PlayerDisc
   */
  public static PlayerDisc disc(CellState state) {
    return stateMap.get(state);
  }

  /**
   * Convert a PlayerDisc to the equivalent CellState.
   * @param disc The PlayerDisc to convert
   * @return The corresponding CellState
   */
  public static CellState state(PlayerDisc disc) {
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
  public static cs3500.reversi.provider.model.Status status(cs3500.reversi.model.Status status) {
    if (status == cs3500.reversi.model.Status.NotStarted) {
      throw new IllegalStateException("Game not started");
    } else {
      return statusMap.get(status);
    }
  }
}
