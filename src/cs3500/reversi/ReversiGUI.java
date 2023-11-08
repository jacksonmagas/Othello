package cs3500.reversi;

import cs3500.reversi.controller.ReverseHexGridController;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.ReversiModel;

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
    int WIDTH = 1200;
    int HEIGHT = 800;

    if (args.length > 0) {
      try {
        noOfCells = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.err.println("Argument " + args[0] + " must be an integer.");
        throw new IllegalArgumentException("Argument " + args[0] + " must be an integer.");
      }
    }

    ReversiModel reversi = new BasicReversi(noOfCells);
    ReversiController controller = new ReverseHexGridController(reversi);
    controller.playGame(reversi);
  }

}
