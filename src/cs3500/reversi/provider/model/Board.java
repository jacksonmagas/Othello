package cs3500.reversi.provider.model;

import java.util.HashMap;
import java.util.Objects;

/**
 * <p>This class represents the Board of our Reversi game. The Board consists of a HashMap that
 * stores the Hex as the Key and the PlayerDisc as the Value. The Board also has a radius that
 * determines the size of the Board. The PlayerDiscs are initialized to EMPTY.</p>
 *
 * <p>The radius of this Board determines the size of the Board. Radius is measured using the
 * distance between the center points of each individual Hex. For instance, for a Board to
 * have a radius of 1 would mean that there is only one more outer layer of Hexagons surrounding
 * the center. This means that our representation of the radius uses a 1-based index rather than
 * a 0-based index.</p>
 *
 * <p>The first or (0,0) coordinate on our board is the center of our board, since we are using
 * axial coordinates. From there, the following "rings" on the board are initialized in a
 * counter-clockwise direction.</p>
 *
 * <p>All fields of this Board are private and final to ensure that unwanted mutation does not
 * occur.</p>
 *
 * <p>The Board follows the invariant that the radius is always a non-zero positive integer.</p>
 *
 */
public class Board {
  private final int radius;
  private final Hex center;
  private HashMap<Hex, PlayerDisc> hexagons;

  /**
   * <p>Constructs a new Board with the given radius.</p>
   *
   * <p>The Board follows the following invariant:
   * <ul>
   * <li>The radius is always a non-zero positive integer.</li>
   * <li>The center of this Board must be within the grid.</li>
   * </ul>
   * @param radius determines the size of this Board.
   * @throws IllegalArgumentException if the given radius is zero not positive or if the
   *                                  center is not within the grid.
   */
  public Board(int radius) {
    Objects.requireNonNull(this);
    ensureValidRadius(radius);
    this.radius = radius;
    this.center = new Hex(0, 0);
    ensureHexIsWithinGrid(this.center);
    Objects.requireNonNull(center);
    this.hexagons = new HashMap<>();
  }

  /**
   * Constructs a new board instance given a board. Allows the user to create copies of existing
   * boards. The Board follows the following invariant:
   * <ul>
   *   <li>The radius is always a non-zero positive integer.</li>
   *   <li>The center of this Board must be within the grid.</li>
   * </ul>
   * @param board represents the board that will be copied.
   * @throws IllegalArgumentException if the given radius is zero not positive or if the
   *                                  center is not within the grid.
   */
  public Board(Board board) {
    Objects.requireNonNull(this);
    Objects.requireNonNull(board);
    this.radius = board.getRadius();
    ensureValidRadius(radius);
    this.center = new Hex(0, 0);
    ensureHexIsWithinGrid(this.center);
    Objects.requireNonNull(this.center);
    this.hexagons = board.getHexagons();
  }

  /**
   * Constructs a new Board with the given HashMap of Hexagons and PlayerDiscs. This constructor
   * is mainly used for testing purposes and to manipulate the game.
   * @param hexagons represents the HashMap of Hexagons and PlayerDiscs.
   */
  public Board(HashMap<Hex, PlayerDisc> hexagons) {
    Objects.requireNonNull(this);
    Objects.requireNonNull(hexagons);
    this.hexagons = hexagons;
    this.radius = getHighestCoordInTheGivenHexagons(hexagons);
    this.center = new Hex(0, 0);
  }

  //GETTERS:
  //(Methods within this section are public because they don't make any mutations to this Board)

  /**
   * Gets the total number of discs on this Board (reagrdless of whether they are empty or not).
   * @return the total number of discs on this Board.
   */
  public int getTotalDiscs() {
    return this.hexagons.size();
  }

  /**
   * Gets the radius of this Board.
   * @return the radius of this Board.
   */
  public int getRadius() {
    return this.radius;
  }

  /**
   * Gets the HashMap of the all hexes in this board, including the state of each hex.
   * @return the HashMap of Hexagons and PlayerDiscs.
   */
  public HashMap<Hex, PlayerDisc> getHexagons() {
    return new HashMap<>(this.hexagons);
  }

  /**
   * Returns the highest coordinate integer in the given hexagons. The highest coord is calculated
   * to know what the radius of the board is.
   * @param hexagons represents the HashMap of Hexagons and PlayerDiscs.
   * @return the highest coordinate integer in the given hexagons.
   */
  public int getHighestCoordInTheGivenHexagons(HashMap<Hex, PlayerDisc> hexagons) {
    int highestCoord = 0;
    for (Hex hex : hexagons.keySet()) {
      if (hex.getQ() > highestCoord) {
        highestCoord = hex.getQ();
      }
    }
    return highestCoord;
  }

  //ENSURERS:

  /**
   * Ensures that the given radius is valid. For a radius to be considered valid, it must be
   * positive and not zero.
   * @param radius represents the radius of this Board.
   */
  protected void ensureValidRadius(int radius) {
    if (radius <= 0) {
      throw new IllegalArgumentException("Radius must be positive.");
    }
  }

  /**
   * Ensures that the given hex is within the boundaries of this Board.
   * @param center represents the hex.
   */
  private void ensureHexIsWithinGrid(Hex center) {
    if (!center.isWithinGrid(this.radius)) {
      throw new IllegalArgumentException("Center is not within grid.");
    }
  }

  //BOARD MUTATION
  //(all methods within this section are set to protected to avoid unwanted mutation
  //from other classes)

  /**
   * <p>Initializes this Board for a game of cs3500.reversi.Reversi.
   * Based on the radius of this Board, Hexes are created and their appropriate coordinates
   * are arranged. The hexagons are placed in a HashMap where the Key represents their coordinate
   * and the Value represents the PlayerDisc. All PlayerDiscs are initialized to EMPTY.</p>
   *
   * <p>One all the hexes are added to the HashMap, the board sets the initial positions of
   * each player on the board. Positions begin with every player having an equal
   * number of their discs on the board. They surround the center hex of the board.
   * In a standard hex grid, each player begins with 3 discs on the board.</p>
   *
   * @throws IllegalArgumentException if the radius is negative
   *         or if this board's center is not within grid.
   */
  protected void initializeGrid() {
    ensureValidRadius(this.radius);
    ensureHexIsWithinGrid(center);

    int startingQ = 0;
    int endingQ = radius;
    for (int r = -radius; r <= radius; r++) {
      for (int q = startingQ; q <= endingQ; q++) {
        setDisc(new Hex(q, r), PlayerDisc.EMPTY);
      }
      if (r < 0) {
        startingQ -= 1;
      } else {
        endingQ -= 1;
      }
    }
    setInitialPositionsForEachPlayer(this.center, PlayerDisc.BLACK);
  }

  /**
   * Based on the center of this Board and the starting player, this method
   * sets the initial positions for each player on the board. Positions begin with every player
   * having an equal number of their discs on the board. They surround the center hex of the board.
   * In a standard hex grid, each player begins with 3 discs on the board.
   *
   * @param center represents the center of this Board.
   * @param startingPlayer represents the starting player of this game.
   * @throws IllegalArgumentException if the center is not within grid.
   */
  private void setInitialPositionsForEachPlayer(Hex center, PlayerDisc startingPlayer) {
    ensureHexIsWithinGrid(center);
    int centerQ = center.getQ();
    int centerR = center.getR();
    setDisc(new Hex(centerQ, centerR - 1), startingPlayer);
    setDisc(new Hex(centerQ + 1, centerR), startingPlayer);
    setDisc(new Hex(centerQ - 1, centerR + 1), startingPlayer);
    setDisc(new Hex(centerQ + 1, centerR - 1), startingPlayer.getOpp());
    setDisc(new Hex(centerQ, centerR + 1), startingPlayer.getOpp());
    setDisc(new Hex(centerQ - 1, centerR), startingPlayer.getOpp());
  }

  /**
   * Sets the given disc on the given Hex coordinate.
   * @param coord represents the coordinate that the disc will be set on
   * @param who represents the type of disc that will be set
   */
  protected void setDisc(Hex coord, PlayerDisc who) {
    hexagons.put(coord, who);
  }

  //OVERRIDDEN METHODS:

  /**
   * Compares this object to another Board object for equality.
   * Returns true if the provided object is also an instance of this class
   * and its fields are equal to the fields of this object.
   *
   * @param o The object to compare for equality.
   * @return true if the Board objects are equal, false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o instanceof Board) {
      return this.radius == ((Board) o).getRadius();
    } else {
      return false;
    }
  }

  /**
   * Returns a string representation of this Board object.
   * The string includes the values of the fields in a human-readable format.
   *
   * @return A string representation of this Board object.
   */
  @Override
  public String toString() {
    return "Board{"
            + "radius=" + radius
            + ", center=" + center
            + ", hexagons=" + hexagons
            + "}";
  }

  /**
   * Returns a hash code value for this Board object. The hash code is based on the values
   * of the object's fields, ensuring that objects that are equal according to the
   * equals() method will have the same hash code.
   *
   * @return A hash code value for this Board object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.radius, this.center, this.hexagons);
  }

}
