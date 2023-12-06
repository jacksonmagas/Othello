package cs3500.reversi.controller;

import cs3500.reversi.provider.controller.IViewFeatures;
import cs3500.reversi.provider.model.PlayerDisc;
import cs3500.reversi.provider.player.IPlayer;
import cs3500.reversi.provider.player.PlayerType;

public class PlayerToIPlayerAdapter implements IPlayer {

  private final PlayerType playerType;

  /**
   * Constructor for Player class.
   */
  public PlayerToIPlayerAdapter(PlayerType playerType) {
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
