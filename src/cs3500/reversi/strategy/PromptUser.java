package cs3500.reversi.strategy;

import java.util.Scanner;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;

/**
 * A Strategy: ask the user where to play next.
 */
public class PromptUser implements MoveStrategy {
  Scanner input;

  PromptUser() {
    this(new Scanner(System.in));
  }

  /**
   * Constructor for PromptUser class.
   */
  public PromptUser(Scanner input) {
    this.input = input;
  }

  /**
   * Chooses a move for computer player.
   */
  @Override
  public Move chooseMove(ReversiModel model, CellState player) {
    System.out.println("Enter a move: make-move row col or pass-turn");
    String moveCommand = input.next();
    Move move;
    if (moveCommand.equalsIgnoreCase("pass-turn")) {
      move = new Move(true);
    } else {
      int r = input.nextInt();
      int c = input.nextInt();
      move = new Move(r, c);
    }
    return move;
  }
}
