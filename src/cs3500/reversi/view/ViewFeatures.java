package cs3500.reversi.view;

/**
 * Contains all the features that the view can do.
 */
public interface ViewFeatures {

  /**
   * A cell can be selected in the view.
   */
  void selectedCell();

  /**
   * A game of reversi can be quit.
   */
  void quit();
}
