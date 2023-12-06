package cs3500.reversi.view.adaptors;

import cs3500.reversi.model.CellState;
import cs3500.reversi.model.adaptors.CoordinateConverter;
import cs3500.reversi.provider.model.IROModel;
import cs3500.reversi.provider.model.PlayerDisc;
import cs3500.reversi.provider.view.IGraphicalReversiView;
import cs3500.reversi.view.MoveListener;
import cs3500.reversi.view.ReversiFrame;
import java.awt.event.KeyListener;

public class IGraphicalReversiViewToReversiFrame implements ReversiFrame {
  private final IGraphicalReversiView base;
  private final IROModel model;
  private final MoveListenerFromProviderFeatures providerFeatureListener;

  public IGraphicalReversiViewToReversiFrame(IGraphicalReversiView base, IROModel model,
      CellState player) {
    this.model = model;
    this.base = base;
    CoordinateConverter converter = new CoordinateConverter(model.getRadius() + 1);
    this.providerFeatureListener = new MoveListenerFromProviderFeatures(converter);
    providerFeatureListener.setView(base);
    base.addFeatures(providerFeatureListener);
    base.displayWho(player.name());
  }

  @Override
  public void addMoveListener(MoveListener listener) {
    providerFeatureListener.register(listener);
  }

  @Override
  public void repaint() {
    if (model.isGameOver()) {
      // this try-catch is nasty and could be removed by making model.getWinner() in the adaptor
      // model use orElse(PlayerDisc.Empty) instead of orElseThrow(IllegalStateException),
      // however that would violate the interface
      try {
        base.displayGameOver(model.getWinner().toString());
      } catch (IllegalStateException e) {
        base.displayGameOver(PlayerDisc.EMPTY.name());
      }
    }
    base.updateView();
  }

  @Override
  public void displayErrorMessage(String message) {
    base.displayErrorMessage(message);
  }

  @Override
  public void setVisibleView(boolean visible) {
    if (visible) {
      base.render();
    } // provider model can't be set invisible once visible
  }

  @Override
  public void setKeyListener(KeyListener listener) {
    // key listeners don't make sense here
  }

  @Override
  public void setHighlightedCell(int row, int col) {
    // also doesn't make sense here
  }
}
