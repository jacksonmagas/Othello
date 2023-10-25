package cs3500.reversi.model;

public class Cell extends Point {
  private Player player;

  public Cell(Point point) {
    super(point.x, point.y);
  }

  public Cell(Point point, Player player) {
    super(point.x, point.y);
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  @Override
  public String toString() {
    String output = "-";
    if (this.player != null) {
      output = this.player.toString();
    }
    return output;
  }
}
