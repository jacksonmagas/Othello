package cs3500.reversi.view;

import java.io.IOException;

import cs3500.reversi.model.ReversiModel;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class ReversiTextualView implements TextualView {
  private ReversiModel model;
  private Appendable out;

  /**
   * Sets constructors for ReversiTextualView class.
   */
  public ReversiTextualView(ReversiModel model) {
    this.model = model;
  }

  /**
   * Constructor for ReversiTextualView class.
   */
  public ReversiTextualView(ReversiModel model, Appendable out) {
    this.model = model;
    this.out = out;
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(this.model.toString());
    return buffer.toString();
  }

  /**
   * Method that renders model.
   */
  public void render() throws IOException {
    this.out.append(this.model.toString());
  }
}
