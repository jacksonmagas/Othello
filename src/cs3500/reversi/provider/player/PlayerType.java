package cs3500.reversi.provider.player;

/**
 * The enum {@code PlayerType} represents the different types of players that can play a game of
 * Reversi. This enum is used to determine what type of player a player is. If the player is human,
 * it is simply a human. The rest are the different types of strategies that an AI player can use.
 */
public enum PlayerType {
  HUMAN(),
  CAPTURE(),
  AVOID(),
  CORNER(),
  MINIMAX(),
  COMBO();

  /**
   * Constructs a PlayerType with the specified player type.
   */
  PlayerType() {
  }

}
