package cs3500.reversi.strategy;

import java.util.Scanner;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;

/**
 * A Strategy: ask the user where to play next.
 */
public class ConsoleInputStrategy implements InfallibleMoveStrategy {

  Scanner input;

  public ConsoleInputStrategy() {
    this(new Scanner(System.in));
  }

  /**
   * Constructor for PromptUser class.
   */
  public ConsoleInputStrategy(Scanner input) {
    this.input = input;
  }

  /**
   * Chooses a move for player based on the textual input to this class.
   */
  @Override
  public Move chooseMove(ReversiModel model, CellState player) {
    System.out.println("Enter a move: make-move row col or pass-turn");
    Move move = null;
    waitForInput: while (!model.isGameOver()) {
      String moveCommand = input.next().toLowerCase();
      switch (moveCommand) {
        case "pass-turn":
          move = new Move(true, false, false);
          break waitForInput;
        case "make-move":
          //System.out.println("Enter a row and column");
          int r = -1;
          int c = -1;
          boolean rAssigned = false;
          while (input.hasNext()) {
            if (input.hasNextInt()) {
              if (!rAssigned) {
                r = input.nextInt();
                rAssigned = true;
              } else {
                c = input.nextInt();
                break;
              }
            } else {
              input.next();
            }
          }
          if (r == -1 || c == -1) {
            throw new IllegalStateException("Scanner out of input");
          }
          move = new Move(r, c);
          break waitForInput;
        case "restart":
          move = new Move(false, true, false);
          break waitForInput;
        case "quit":
          move = new Move(false, false, true);
          break waitForInput;
      }
      System.out.println("Invalid move, move must be one of: make-move row col or pass-turn");

    }
    return move;
  }

  @Override
  public void newGUIMove(Move newMove) {
    //ignore
  }
}
