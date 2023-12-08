package cs3500.reversi.provider.player;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.provider.model.Hex;
import cs3500.reversi.provider.model.IROModel;

/**
 * A function-object class whose sole purpose is to provide more information about the corners of
 * a Reversi board. This class was created as there is more than one strategy that requires
 * information about corners.
 */
public class CornerInfo {
  private final IROModel model;

  /**
   * Constructs a {@code CornerInfo} object. This constructor is given an {@code IROModel} in which
   * it will then compose a list of all the corners of the model's board.
   * @param model the model of the game
   */
  protected CornerInfo(IROModel model) {
    this.model = model;
  }

  /**
   * Builds a list of all the corners in the model's board.
   * @return a list of all the corners in the model's board
   */
  protected List<Hex> getAllCorners() {
    List<Hex> corners = new ArrayList<>();

    int radius = model.getRadius();
    for (int x = radius * -1; x <= radius; x += radius) {
      for (int y = radius * -1; y <= radius; y += radius) {
        if (x != y) {
          corners.add(new Hex(y, x));
        }
      }
    }
    return corners;
  }
}
