package cs3500.reversi.view.adaptors;

import cs3500.reversi.model.adaptors.CoordinateConverter;
import cs3500.reversi.provider.view.IGraphicalReversiView;
import cs3500.reversi.view.MoveListener;
import cs3500.reversi.view.ReversiFrame;
import java.awt.event.KeyListener;

public class IGraphicalReversiViewToReversiFrame implements ReversiFrame {
  private final IGraphicalReversiView base;
  private final MoveListenerFromProviderFeatures providerFeatureListener;

  public IGraphicalReversiViewToReversiFrame(IGraphicalReversiView base, int sideLength) {
    this.base = base;
    CoordinateConverter converter = new CoordinateConverter(sideLength);
    this.providerFeatureListener = new MoveListenerFromProviderFeatures(converter);
    providerFeatureListener.setView(base);
    base.addFeatures(providerFeatureListener);
    base.displayWho("WHITE");
  }

  @Override
  public void addMoveListener(MoveListener listener) {
    providerFeatureListener.register(listener);
  }

  @Override
  public void repaint() {
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
