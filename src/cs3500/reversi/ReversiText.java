package cs3500.reversi;

import cs3500.reversi.model.BoardType;
import java.io.InputStreamReader;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.controller.ReversiTextualController;
import cs3500.reversi.model.BasicReversi;
import cs3500.reversi.model.ReversiModel;

/**
 * Represent a Reversi game class.
 */
public class ReversiText {

  /**
   * Constructor for Reversi public class.
   */
  public static void main(String[] args) {
    // defaults to 4
    int noOfCells = 4;
    BoardType boardType = BoardType.HEXAGON;

    if (args.length > 0) {
      try {
        noOfCells = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.err.println("Argument " + args[0] + " must be an integer.");
        throw new IllegalArgumentException("Argument " + args[0] + " must be an integer.");
      }
      try {
        boardType = BoardType.valueOf(args[1].toUpperCase());
      } catch (IllegalArgumentException | NullPointerException e) {
        System.err.println("Argument " + args[1] + " must be board type. Valid values are HEXAGON or SQUARE.");
        throw new IllegalArgumentException("Argument " + args[1] + " must be board type. Valid values are HEXAGON or SQUARE.");
      }
    }
    try {
      ReversiModel reversi = new BasicReversi(boardType, noOfCells);
      Readable rd = new InputStreamReader(System.in);
      Appendable ap = System.out;
      ReversiController controller = new ReversiTextualController(rd, ap);
      controller.playGame(reversi);
    } catch (IllegalArgumentException e) {
      System.err.println("Argument " + args[0] + " " + e.getMessage());
      throw new IllegalArgumentException("Argument " + args[0] + " " + e.getMessage());
    }
  }
}
