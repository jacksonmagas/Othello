package cs3500.reversi.provider.player;

import cs3500.reversi.provider.controller.IViewFeatures;
import cs3500.reversi.provider.model.PlayerDisc;

/**
 * The {@code IPlayer} interface represents a player in a game of Reversi. A player can be a human
 * or a machine. A player can be asked to choose an action, and can be given features to interact
 * with. This features will be what the player uses to interact with the model and to be updated
 * with the current state of the game. All implementations of this interface will be given a
 * {@code PlayerDisc} that will represent itself in the model. This will be determined in the
 * constructor of the implementing class.
 */
public interface IPlayer {

  /**
   * Adds the given features to this player. {@code IViewFeatures} will be added to this player
   * so that it has the ability to {@code skipTurn} and {@code makeMove} when it is asked to
   * choose an action.
   * @param features the features to be added to this player.
   */
  void addFeatures(IViewFeatures features);

  /**
   * Asks the player to choose an action. This action will be either to skip its turn or to make
   * a move. If the player is a machine, this will be determined by the Strategy that the machine
   * is using. If the player is a human, this will be determined by the user's interaction
   * with the view.
   */
  void chooseAction();

  /**
   * Returns the {@code PlayerDisc} that this player is using.
   * @return the {@code PlayerDisc} that represents this player in the model.
   */
  PlayerDisc getPlayerDisc();
}
