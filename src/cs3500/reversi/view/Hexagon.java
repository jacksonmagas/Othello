package cs3500.reversi.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;

/**
 * Creates the Hexagon view.
 */
public class Hexagon extends Polygon {

  private static final long serialVersionUID = 1L;

  public static final int SIDES = 6;

  private Point center;

  private int radius;

  private int rotation = 90;

  /**
   * Constructor for Hexagon class.
   */
  public Hexagon(Point center, int radius) {
    npoints = SIDES;
    xpoints = new int[SIDES];
    ypoints = new int[SIDES];

    this.center = center;
    this.radius = radius;

    updatePoints();
  }

  /**
   * Constructor for Hexagon class.
   */
  public Hexagon(int x, int y, int radius) {
    this(new Point(x, y), radius);
  }

  // gets the radius
  public int getRadius() {
    return radius;
  }

  /**
   * Sets the radius.
   */
  public void setRadius(int radius) {
    this.radius = radius;

    updatePoints();
  }

  // gets the rotation
  public int getRotation() {
    return rotation;
  }

  /**
   * Sets the rotation of the hexagon.
   */
  public void setRotation(int rotation) {
    this.rotation = rotation;

    updatePoints();
  }

  /**
   * Sets the center in the hexagon.
   */
  public void setCenter(Point center) {
    this.center = center;

    updatePoints();
  }

  // sets the center
  public void setCenter(int x, int y) {
    setCenter(new Point(x, y));
  }

  // finds the angle
  private double findAngle(double fraction) {
    return fraction * Math.PI * 2 + Math.toRadians((rotation + 180) % 360);
  }

  // finds the point
  private Point findPoint(double angle) {
    int x = (int) (center.x + Math.cos(angle) * radius);
    int y = (int) (center.y + Math.sin(angle) * radius);

    return new Point(x, y);
  }

  // updates the point
  protected void updatePoints() {
    for (int p = 0; p < SIDES; p++) {
      double angle = findAngle((double) p / SIDES);
      Point point = findPoint(angle);
      xpoints[p] = point.x;
      ypoints[p] = point.y;
    }
  }

  /**
   * Draws the hexagon.
   */
  public void draw(Graphics2D g, int lineThickness,
                   Color colorValue, boolean filled) {
    // Store before changing.
    Stroke tmpS = g.getStroke();
    Color tmpC = g.getColor();

    g.setColor(colorValue);
    g.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

    if (filled) {
      g.fillPolygon(xpoints, ypoints, npoints);
    }
    else {
      g.drawPolygon(xpoints, ypoints, npoints);
    }

    // Set values to previous when done.
    g.setColor(tmpC);
    g.setStroke(tmpS);
  }
}
