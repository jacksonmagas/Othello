package cs3500.reversi.controller.adaptors;

import cs3500.reversi.controller.Player;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.Move;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.provider.player.AvoidNextToCornerStrat;
import cs3500.reversi.provider.player.CaptureMaxStrat;
import cs3500.reversi.provider.player.CompleteStratFromFallible;
import cs3500.reversi.provider.player.GoForCornerStrat;
import cs3500.reversi.provider.player.InfallibleReversiStrat;
import cs3500.reversi.provider.player.MiniMaxStrat;
import cs3500.reversi.provider.player.PlayerType;
import cs3500.reversi.provider.player.TryN;
import cs3500.reversi.strategy.GUIInputStrategy;
import cs3500.reversi.strategy.InfallibleMoveStrategy;
import cs3500.reversi.strategy.adaptors.ProviderStrategyToInfallibleMoveStrategy;

public class PlayerToIPlayerAdapter implements Player {
  private final InfallibleMoveStrategy strat;
  private final CellState player;

  /**
   * Constructor for Player class.
   */
  public PlayerToIPlayerAdapter(PlayerType playerType, CellState player) {
    this.player = player;
    this.strat = getStrategy(playerType);
  }

  private InfallibleMoveStrategy getStrategy(PlayerType playerType) {
    InfallibleReversiStrat strat;
    switch (playerType) {
      case AVOID:
        strat = new CompleteStratFromFallible(new AvoidNextToCornerStrat());
        break;
      case COMBO:
        strat = new CompleteStratFromFallible(new TryN());
        break;
      case CORNER:
        strat = new CompleteStratFromFallible(new GoForCornerStrat());
        break;
      case CAPTURE:
        strat = new CompleteStratFromFallible(new CaptureMaxStrat());
        break;
      case MINIMAX:
        strat = new CompleteStratFromFallible(new MiniMaxStrat());
        break;
      default:
        return new GUIInputStrategy();
    }
    return new ProviderStrategyToInfallibleMoveStrategy(strat);
  }

  @Override
  public Move play(ReversiModel model) {
    return strat.chooseMove(model, this.player);
  }

  @Override
  public CellState getPiece() {
    return this.player;
  }

  @Override
  public void receiveGUIAction(Move m) {
    strat.newGUIMove(m);
  }
}
