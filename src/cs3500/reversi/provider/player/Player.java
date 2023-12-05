package cs3500.reversi.provider.player;

import cs3500.reversi.provider.controller.IViewFeatures;
import cs3500.reversi.provider.model.PlayerDisc;

public class Player implements IPlayer {

  private final PlayerType playerType;

  /**
   * Constructor for Player class.
   */
  public Player(PlayerType playerType) {
    this.playerType = playerType;
  }

  @Override
  public void addFeatures(IViewFeatures features) {

  }

  @Override
  public void chooseAction() {

  }

  @Override
  public PlayerDisc getPlayerDisc() {
    return null;
  }
}
