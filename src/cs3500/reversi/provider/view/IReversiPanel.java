package cs3500.reversi.provider.view;

import java.awt.Dimension;

import cs3500.reversi.provider.controller.IViewFeatures;

/**
 * The interface {@code IReversiPanel} represents the actions that the panel is ought to be
 * capable of. The panel is capable of doing several functions. It can paint the component, add
 * information about the board to the panel, add features to the list of features that this view
 * can perform, and set hot keys to its respective features. It is also able to utilize relevant
 * listeners to listen for low-level events from the user and communicate this information with
 * the {code IView} interface and the controller ot connect the low-level events with its
 * respective high-level events. More information about the specific methods our panel of the
 * Reversi game does can found in the classes that implement {@code IReversiPanel}.
 */
public interface IReversiPanel {

  /**
   * Adds the given IFeatures to the list of features that this view can perform.
   * Features in this game represent the multiple callbacks that the view uses to communicate
   * with the controller that an important and interesting event has occurred (such as pressing
   * on the key 's' to tell the controller that the user wants to skip their turn).
   * @param feature the IFeatures to be added to this view.
   */
  void addFeatures(IViewFeatures feature);

  /**
   * Updates the board to reflect the current state of the game.
   */
  void updateBoard();

  /**
   * Gets the dimensions of the panel.
   * @return the dimensions of the panel
   */
  Dimension getDimensions();

}
