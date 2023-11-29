package cs3500.reversi.view;

import java.io.IOException;

import cs3500.reversi.model.ReadonlyReversiModel;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class ReversiTextualView implements TextualView {
  private final ReadonlyReversiModel model;
  private Appendable out;

  /**
   * Sets constructors for ReversiTextualView class.
   */
  public ReversiTextualView(ReadonlyReversiModel model) {
    this.model = model;
  }

  /**
   * Constructor for ReversiTextualView class.
   */
  public ReversiTextualView(ReadonlyReversiModel model, Appendable out) {
    this.model = model;
    this.out = out;
  }

  @Override
  public String toString() {
    return this.model.toString();
  }

  /**
   * Method that renders model.
   */
  public void render() throws IOException {
    this.out.append(this.model.toString());
  }
}
