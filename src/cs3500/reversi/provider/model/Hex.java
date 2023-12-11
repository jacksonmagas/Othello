package cs3500.reversi.provider.model;

import java.util.Objects;

/**
 * <p>The {@code Hex} class represents an axial coordinate in a hexagonal grid.
 * Axial coordinates use two axes (q and r) to represent positions
 * within a hexagonal grid. Axial coordinates were used for our implementation of the Reversi
 * model as they are more intuitive to use than cube coordinates (i.e., they provide the same
 * amount of information with less data and axial coords more closely resemble the
 * row x column representation of a Cartesian coordinate system, which is generally
 * more widely used and familiarized).</p>
 *
 * <p>In this coordinate system, the following invariants apply:</p>
 * <ul>
 * <li> q + r + s = 0 </li>
 * <li> s = -q - r </li>
 * </ul>
 *
 * <p>Fields of this class are final and private, ensuring thread safety for use
 * in concurrent applications.<\p>
 *
 * <p>Methods in this class are public as they are simply used to get information about
 * this Hex and don't involve any mutation.</p>
 *
 * <p>Information about the hex grid and the axial coordinate system was retrieved from the
 * following site: https://www.redblobgames.com/grids/hexagons/#neighbors</p>
 *
 */
public class Hex {
  private final int q;
  private final int r;

  /**
   * Constructs a new {@code Hex} object with the specified axial coordinates.
   * The coordinates must satisfy the following invariants: q + r + s = 0, and s = -q - r.
   *
   * @param q The q-coordinate in axial coordinates.
   * @param r The r-coordinate in axial coordinates.
   * @throws IllegalArgumentException if the following coordinates do not satisfy the invariants,
   *                                  in which case would indicate an invalid
   *                                  hexagonal grid position.
   */
  public Hex(int q, int r) {
    Objects.requireNonNull(this);
    this.q = q;
    this.r = r;
    int s = -q - r;
    if (-q - r != s || q + r + s != 0) {
      throw new IllegalArgumentException("hex does not follow variants");
    }
  }

  /**
   * Gets the q-coordinate of this hexagon in cube coordinates.
   *
   * @return the q-coordinate.
   */
  public int getQ() {
    return q;
  }

  /**
   * Gets the r-coordinate of this hexagon in cube coordinates.
   *
   * @return the r-coordinate.
   */
  public int getR() {
    return r;
  }

  /**
   * Is this coordinate located within the radius of the grid?
   * For a coordinate to be within a grid, it must be less than the radius,
   * and follow the invariant of q + r + s = 0; q + r = s.
   * @return {@code true} if it within the grid or {@code false}
   *         if it is out of bounds.
   */
  public boolean isWithinGrid(int radius) {
    int s = q + r;
    return this.q < radius && this.r < radius &&
            q + r + s == 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "Hex{"
            + "q=" + q
            + ", r=" + r
            + '}';
  }

  /**
   * Indicates whether some other object is "equal to" this Hex.
   * Two {@code Hex} objects are considered equal if they have the same
   * q, r, and s coordinates.
   *
   * @param obj The object to compare with this {@code Hex}.
   * @return {@code true} if this {@code Hex} is equal to the given object;
   *         {@code false} otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Hex otherHex = (Hex) obj;
    return q == otherHex.getQ()
            && r == otherHex.getR();
  }

  /**
   * Returns a hash code value for this {@code Hex}.
   * The hash code is generated based on the q, r, and s coordinates of the {@code Hex}.
   *
   * @return A hash code value for this {@code Hex}.
   */
  @Override
  public int hashCode() {
    return Objects.hash(q, r);
  }

}
