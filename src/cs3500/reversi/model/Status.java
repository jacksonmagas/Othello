package cs3500.reversi.model;

/**
 * This enum represents the different states a game of cs3500.reversi.Reversi can be in.
 * The game can be ongoing, or it can be over without or with a winner.
 */
public enum Status {
  Playing,
  Stalemate,
  Won;
}
