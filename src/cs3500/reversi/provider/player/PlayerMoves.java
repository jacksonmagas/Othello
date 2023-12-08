package cs3500.reversi.provider.player;

import cs3500.reversi.provider.model.Hex;

/**
 * The enum {@code PlayerMoves} represents the different types of moves a player can make in a game
 * of Reversi. It also stores the selected hex of the move if the move is a {@code MAKE_MOVE}.
 */
public enum PlayerMoves {
  // Enum constants representing different types of moves
  MAKE_MOVE("Make Move"),
  SKIP_TURN("Skip Turn");

  /**
   * The description of the move.
   */
  private final String description;
  /**
   * The selected hex of the move if the move is a {@code MAKE_MOVE}.
   */
  private Hex selectedHex;

  /**
   * Constructs a {@code PlayerMoves} object with the given description.
   * @param description the description of the move
   */
  PlayerMoves(String description) {
    this.description = description;
  }

  /**
   * Returns the description of the move. This move is the move that the strategies recommend take.
   * @return the description of the move
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the selected hex of the move if the move is a {@code MAKE_MOVE}.
   * @return the selected hex of the move if the move is a {@code MAKE_MOVE}
   */
  public Hex getSelectedHex() {
    return selectedHex;
  }

  /**
   * Sets the selected hex of the move if the move is a {@code MAKE_MOVE}.
   * @param selectedHex the selected hex of the move if the move is a {@code MAKE_MOVE}
   */
  protected void setSelectedHex(Hex selectedHex) {
    this.selectedHex = selectedHex;
  }
}