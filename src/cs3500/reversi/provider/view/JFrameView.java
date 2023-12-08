package cs3500.reversi.provider.view;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cs3500.reversi.provider.controller.IViewFeatures;
import cs3500.reversi.provider.model.IROModel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

/**
 * The {@code JFrameView} class represents a graphical view for the Reversi game.
 * It extends the {@code JFrame} class and implements the {@code IGraphicalReversiView} interface.
 * It is the main view for the game, and it is the view that the user interacts with.
 * It contains a {@code IReversiPanel} that is the main panel for the game.
 * It also contains a {@code IROModel} that is the model for the game. It specifically contains
 * a read-only model to prevent unwanted mutation of the model by the view. The view is able to
 * render the game, add features to the list of features that this view can perform, and set hot
 * keys to its respective features. Information about listeners or any specific rendering methods
 * are placed within the {@code BoardPanel} class.
 */
public class JFrameView extends JFrame implements IGraphicalReversiView {
  private final IReversiPanel boardPanel;

  /**
   * Constructs a {@code JFrameView} object with the given model. It sets the default close
   * operation to exit on close, sets the layout to a flow layout, and adds the board panel to
   * the frame. It also sets the icon image of the frame to the Kirby icon and sets the title
   * of the frame to the score of each player. It then packs the frame, sets the location of the
   * frame to the center of the screen, and resets the focus of the frame. This constructor
   * disallows users from resizing the window; this is because the window uses images that
   * are of a fixed size, and resizing the window would cause the images to be distorted.
   * The title of the frame is set to the name of the game and whose view it is.
   * Link to the Kirby icon was retrieved from the following link:
   * {<a href="https://tinyurl.com/ykzm6k6e">...</a>}.
   * NOTE: Although the image is not all rights reserved, we are only using it for
   * educational purposes in the context of this class.
   * @param model the model to be used for the game
   */
  public JFrameView(IROModel model) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.boardPanel = new BoardPanel(model, this); //main panel of the game
    ((JPanel) boardPanel).setAlignmentX(Component.CENTER_ALIGNMENT);
    ((JPanel) boardPanel).setAlignmentY(Component.CENTER_ALIGNMENT);
    add((Component) boardPanel);
    setMinimumSize(boardPanel.getDimensions());
    int doubleLen = (int) (boardPanel.getDimensions().getWidth() * 1.5);
    Dimension maxDim = new Dimension(doubleLen, doubleLen);
    setMaximumSize(maxDim);

    URL kirbyPath = ClassLoader.getSystemResource("kirbyIcon.png");
    if (kirbyPath != null) {
      Image icon = new ImageIcon(kirbyPath).getImage();
      setIconImage(icon);
    }
    displayWho(model.getNextPlayerTurn().toString());

    ensureWindowIsProportionate();
    pack();
    setLocationRelativeTo(null);
    render();
    resetFocus();
  }

  @Override
  public void render() {
    this.setVisible(true);
  }

  @Override
  public void addFeatures(IViewFeatures feature) {
    this.boardPanel.addFeatures(feature);
  }

  @Override
  public void updateView() {
    this.boardPanel.updateBoard();
  }

  @Override
  public void displayErrorMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "oopsies", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void displayWho(String playerDisc) {
    setTitle(String.format("Reversi the Kirby! " + "THIS IS " + translatePlayer(playerDisc))
            + "'S VIEW");
  }

  @Override
  public void displayGameOver(String winner) {
    setTitle("Game over for Reversi the Kirby! " + translatePlayer(winner) + " wins!");
    JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Translates the playerDisc to the appropriate player name.
   * @param playerDisc the playerDisc to be translated
   * @return the appropriate player name
   */
  private String translatePlayer(String playerDisc) {
    if (playerDisc.equals("BLACK")) {
      return "KIRBY";
    } else if (playerDisc.equals("WHITE")) {
      return "METAKNIGHT";
    } else {
      return "Nobody";
    }
  }

  /**
   * Ensures that the window is always proportional. This is so that when the user manually
   * expands the size of the window, the window will always be a square.
   */
  private void ensureWindowIsProportionate() {
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        int newSize = Math.min(getWidth(), getHeight());
        setSize(newSize, newSize);
      }
    });
  }

  /**
   * Resets the focus of the JFrameView.
   */
  private void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

}
