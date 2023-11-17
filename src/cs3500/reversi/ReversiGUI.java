package cs3500.reversi;

import java.util.Scanner;
import cs3500.reversi.controller.PlayerImpl;
import cs3500.reversi.controller.ReverseHexGridController;
import cs3500.reversi.controller.ReversiPlayerStrategyController;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.CellState;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.strategy.FirstAvailableOpening;
import cs3500.reversi.strategy.PromptUser;

/**
 * Represent a Reversi game class.
 */
public class ReversiGUI {

  /**
   * Constructor for Reversi public class.
   */
  public static void main(String[] args) {
    // defaults to 4
    int noOfCells = 4;
    int width = 1200;
    int height = 800;

    if (args.length > 0) {
      try {
        noOfCells = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.err.println("Argument " + args[0] + " must be an integer.");
        throw new IllegalArgumentException("Argument " + args[0] + " must be an integer.");
      }
    }

    ReversiModel reversi = new BasicReversi(noOfCells);
    ReversiPlayerStrategyController controller = new ReverseHexGridController(reversi);
    controller.addPlayer(new PlayerImpl(CellState.BLACK, new PromptUser()));
    controller.addPlayer(new PlayerImpl(CellState.WHITE, new FirstAvailableOpening()));
    controller.play();
  }
}
