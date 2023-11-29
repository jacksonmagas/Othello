package cs3500.reversi.strategy;

import java.util.Scanner;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;

/**
 * A Strategy: ask the user where to play next.
 */
public class PromptUser implements InfallibleMoveStrategy {

  Scanner input;

  public PromptUser() {
    this(new Scanner(System.in));
  }

  /**
   * Constructor for PromptUser class.
   */
  public PromptUser(Scanner input) {
    this.input = input;
  }

  /**
   * Chooses a move for player based on the textual input to this class.
   */
  @Override
  public Move chooseMove(ReversiModel model, CellState player) {
    System.out.println("Enter a move: make-move row col or pass-turn");
    Move move = null;
    while (!model.isGameOver()) {
      String moveCommand = input.next();
      if (moveCommand.equalsIgnoreCase("pass-turn")) {
        move = new Move(true, false, false);
        break;
      } else if (moveCommand.equalsIgnoreCase("make-move")) {
        //System.out.println("Enter a row and column");
        int r = input.nextInt();
        int c = input.nextInt();
        move = new Move(r, c);
        break;
      } else if (moveCommand.equalsIgnoreCase("restart")) {
        move = new Move(false, true, false);
        break;
      } else if (moveCommand.equalsIgnoreCase("quit")) {
        move = new Move(false, false, true);
        break;
      }
      System.out.println("Invalid move, move must be one of: make-move row col or pass-turn");

    }
    return move;
  }
}
