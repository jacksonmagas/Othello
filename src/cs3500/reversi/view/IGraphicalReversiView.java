package cs3500.reversi.view;

import cs3500.reversi.controller.IViewFeatures;

/**
 * The interface {@code IGraphicalReversiView} represents the main graphical user interface for
 * our game of Reversi. It extends the previously made {@code IView} interface (that previously
 * used to have one single method to render the textual view) and adds more functionality
 * for the view.
 */
public interface IGraphicalReversiView extends IView {

  /**
   * Adds the given IViewFeatures to the list of features that this view can interact with.
   * The methods used by the {@code IViewFeatures} represents the high-level events that the
   * view can perform.
   * @param feature the IFeatures to be added to this view.
   */
  void addFeatures(IViewFeatures feature);

  /**
   * Updates the view to reflect the current state of the game by interacting with the
   * main board panel.
   */
  void updateView();

  /**
   * Displays the given error message to the user if they are trying to make an illegal move
   * (e.g., it is not the player's turn, the move is not valid, etc.).
   * @param message the error message to be displayed
   */
  void displayErrorMessage(String message);

  /**
   * Displays whose turn it is to the user. This will change the title of the frame so that
   * the user knows whose view each window is.
   * @param playerDisc the player whose represented by the given playerDisc and
   *                  whose controlling this view.
   */
  void displayWho(String playerDisc);

  /**
   * Displays the game over message to the user. If there is a winner, this method will
   * display the winner. If there is a tie, this method will display that there is a tie.
   */
  void displayGameOver(String winner);
}
