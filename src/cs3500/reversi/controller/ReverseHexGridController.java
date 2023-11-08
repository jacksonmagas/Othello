package cs3500.reversi.controller;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import cs3500.reversi.model.ReadonlyReversiModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.view.BasicReversiView;
import cs3500.reversi.view.ReversiFrame;


/**
 * This controller updates the model and view according to user input.
 *
 * In order to ease drawing of the blobs in the view, this controller enforces the following
 * constraint: no part of any blob will be in the -ve X and -ve Y space. That is, the smallest
 * region that contains all the blobs has a lower left corner of (0,0) at the least.
 */
public class ReverseHexGridController implements ReversiController {

  private ReversiModel model;
  private ReversiFrame view;
  private final int WIDTH = 1200;
  private final int HEIGHT = 800;
  public ReverseHexGridController(ReversiModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model is null");
    }
    this.model = model;
  }

  @Override
  public void playGame(ReversiModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model is null");
    }
    this.view = new BasicReversiView(WIDTH, HEIGHT, model);
    setMouseListener();
  }

  private void setMouseListener() {

    view.setMouseListener(new MyMouseListener(model, view));
  }

  class MyMouseListener extends MouseAdapter {

    ReversiModel model;
    ReversiFrame view;

    public MyMouseListener(ReversiModel model, ReversiFrame view) {
      super();
      this.model = model;
      this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();

      // Determine row, col using x, y
      HashMap<Point, Point> keyMap = view.getMap();
      //Point rowCol = (Point) keyMap.get(new Point(x, y));
      // 0,0=467,170 0,1=555,170 0,2=644,170
      // 1,0=423,247 1,1=511,247 1,2=600,247
      // horizontal cell distance 44
      // vertical cell distance 33
      Point rowCol = findRowCols(keyMap, new Point(x, y));
      int[][] board = this.model.getBoard();
      //board[x][y] = (int)'X';
      //System.out.println("Controller mouse click event - x "+x+" y "+y+" target row "+row+" col "+col);
      //System.out.println("Component "+e.getComponent().toString());
      //System.out.println("Source "+e.getSource());
      if (rowCol != null) {
        System.out.println("row " + rowCol.x + " col " + rowCol.y);
        int row = rowCol.x;
        int col = rowCol.y;
        try {
          this.model.makeMove(row, col);
          System.out.println(model.toString());
          view.setModel(this.model);
          view.repaint();
        } catch (IllegalArgumentException | IllegalStateException ex) {
          System.err.println("Error: " + ex.getMessage() + System.lineSeparator());
        }
      }
    }

    private Point findRowCols(HashMap<Point, Point> keyMap, Point mouseClickedPoint) {
      Point rowColPoint = null;
      if (keyMap != null && mouseClickedPoint != null) {
        for (Point key : keyMap.keySet()) {
          if (key != null
                  && isRowMatch(key.x, mouseClickedPoint.x)
                  && isColMatch(key.y, mouseClickedPoint.y)) {
            rowColPoint = keyMap.get(key);
            break;
          }
        }
      }
      return rowColPoint;
    }

    boolean isRowMatch(int x1, int x2) {
      return (Math.abs(x1 - x2) <= 44);
    }

    boolean isColMatch(int y1, int y2) {
      return (Math.abs(y1 - y2) <= 33);
    }
  }
}

