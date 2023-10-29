package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the primary model interface for playing a game of Reversi.
 * Moves in this model are made using the coordinate system (row, index) where the row is the
 * horizontal row number 0 indexed from top and the index is the location in the row 0 indexed from left.
 */
public interface ReversiModel {

  /**
   * Starts the Reversi game.
   * It builds the hexagonal grid using the number of cells provided.
   * @param noOfCells number of cells to be used for this game
   * @throws IllegalArgumentException if input parameter is invalid
   * @throws IllegalStateException if the game is already started
   */
  void startGame(int noOfCells);

  /**
   * Return the score of the person player, which is the sum of the values of the disc cards.
   * @return the score
   * @throws IllegalStateException if the game hasn't been started yet
   */
  public int getYourScore();

  /**
   * Return the score of the computer player, which is the sum of the values of the disc cards.
   * @return the score
   * @throws IllegalStateException if the game hasn't been started yet
   */
  public int getComputerScore();

  /**
   * Allow current player to make move disc to given row and column if allowable by the rules of the game.
   * @param row the 0-based indexed from top i.e. the horizontal row number
   * @param column the 0-based indexed from left i.e. vertical column number
   * @throws IllegalArgumentException if either input parameters are invalid
   * @throws IllegalStateException if the game hasn't been started yet or if the move is not allowable or if game is over!
   */
  void makeMove(int row, int column);

  /**
   * Allow current player to pass the move.
   * If both players does their own passes then game will be over.
   * @throws IllegalStateException if the game hasn't been started yet or if game is over!
   */
  void passTurn();
}
