package cs3500.reversi.controller;

import java.io.IOException;

import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.view.ReversiTextualView;

/**
 * Public class ReversiTextualController used to create controller.
 * Takes in parameters: Readable in, Appendable out.
 */
public class ReversiTextualController implements ReversiController {

  final Readable in;
  final Appendable out;
  /**
   * Constructor for ReversiTextualController class.
   */
  public ReversiTextualController(Readable in, Appendable out) throws IllegalArgumentException {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable or Appendable is null");
    }
    this.in = in;
    this.out = out;
  }

  @Override
  public void playGame(ReversiModel model, int noOfCells) {
    if (model == null) {
      throw new IllegalArgumentException("Model is null");
    }
    ReversiTextualView view = new ReversiTextualView(model, this.out);
    try {
      model.startGame(noOfCells, 0);
      view.render();
    }
    catch (IllegalArgumentException e) {
      printToOut("Error " + e.getMessage() + "\n");
      throw e;
    }
    catch (Exception e) {
      printToOut("Error " + e.getMessage() + "\n");
      throw new IllegalStateException(e);
    }
  }

  void printToOut(String message) {
    try {
      this.out.append(message);
    }
    catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
