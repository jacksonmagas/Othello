package cs3500.reversi;

import java.io.InputStreamReader;

import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.controller.ReversiTextualController;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.ReversiModel;

/**
 * Represent a Reversi game class.
 */
public class Reversi {

  public static void main(String[] args) {
    // defaults to 6
    int noOfCells = 6;

    if (args.length > 0) {
      try {
        noOfCells = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.err.println("Argument " + args[0] + " must be an integer.");
        throw new IllegalArgumentException("Argument " + args[0] + " must be an integer.");
      }
    }
    ReversiModel reversi = new BasicReversi();
    Readable rd = new InputStreamReader(System.in);
    Appendable ap = System.out;
    ReversiController controller = new ReversiTextualController(rd, ap);
    controller.playGame(reversi, noOfCells);
  }

}
