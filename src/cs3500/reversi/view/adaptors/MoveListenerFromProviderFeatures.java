package cs3500.reversi.view.adaptors;

import cs3500.reversi.model.Move;
import cs3500.reversi.model.adaptors.CoordinateConverter;
import cs3500.reversi.provider.controller.IViewFeatures;
import cs3500.reversi.provider.player.IPlayer;
import cs3500.reversi.provider.view.IGraphicalReversiView;
import cs3500.reversi.view.MoveListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Public class MoveListenerFromProviderFeatures moves listener from provider code.
 */
public class MoveListenerFromProviderFeatures implements IViewFeatures {
  private final List<MoveListener> listeners;
  private final CoordinateConverter converter;

  public MoveListenerFromProviderFeatures(CoordinateConverter converter) {
    this.listeners = new ArrayList<>();
    this.converter = converter;
  }

  @Override
  public void skipTurn() {
    notifyListeners(new Move(true, false, false, false));
  }

  /**
   * Register a new MoveListener.
   * @param subscriber the listener to register
   */
  public void register(MoveListener subscriber) {
    listeners.add(subscriber);
  }

  /**
   * Notify all MoveListeners that the view has detected a move.
   * @param m the move to make
   */
  public void notifyListeners(Move m) {
    for (MoveListener l : listeners) {
      l.receiveGUIAction(m);
    }
  }

  @Override
  public void placeDisc(int selectedHexQ, int selectedHexR) {
    notifyListeners(converter.moveFromAxial(selectedHexQ, selectedHexR));
  }

  @Override
  public void setView(IGraphicalReversiView v) {
    // not meaningful in this context
  }

  @Override
  public void setPlayer(IPlayer player) {
    // not meaningful in this context
  }
}
