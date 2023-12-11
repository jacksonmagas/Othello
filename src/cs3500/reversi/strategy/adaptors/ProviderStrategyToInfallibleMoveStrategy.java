package cs3500.reversi.strategy.adaptors;

import static cs3500.reversi.model.adaptors.ProviderEnumConverter.disc;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.adaptors.CoordinateConverter;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.adaptors.ReversiModelToIMutableModelAdapter;
import cs3500.reversi.provider.player.InfallibleReversiStrat;
import cs3500.reversi.provider.player.PlayerMoves;
import cs3500.reversi.strategy.InfallibleMoveStrategy;
import java.util.Objects;

/**
 * Adaptor class for using provider strategies in our controller.
 * Wraps the provider strategy and uses it to choose the move to make.
 */
public class ProviderStrategyToInfallibleMoveStrategy implements InfallibleMoveStrategy {
  private final InfallibleReversiStrat base;

  /**
   * Create a new InfallibleMoveStrategy that uses the given InfallibleReversiStrat to choose
   * its move.
   * @param base the reversi strat to use.
   */
  public ProviderStrategyToInfallibleMoveStrategy(InfallibleReversiStrat base) {
    this.base = base;
  }

  private Move playerMovesToMove(PlayerMoves move, CoordinateConverter converter) {
    if (Objects.requireNonNull(move) == PlayerMoves.MAKE_MOVE) {
      int q = move.getSelectedHex().getQ();
      int r = move.getSelectedHex().getR();
      return new Move(converter.rowFromAxial(r), converter.colFromAxial(q, r));
    } else {
      return new Move(true, false, false);
    }
  }

  @Override
  public Move chooseMove(ReversiModel model, CellState player) {
    return playerMovesToMove(base.chooseMove(new ReversiModelToIMutableModelAdapter(model),
            disc(player)), new CoordinateConverter(model.sideLength()));
  }

  @Override
  public void newGUIMove(Move newMove) {
    // provider strategies don't need gui information
  }
}
