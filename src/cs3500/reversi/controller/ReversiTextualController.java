package cs3500.reversi.controller;

import java.io.IOException;
import java.util.Scanner;

import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.view.ReversiTextualView;

/**
 * Public class ReversiTextualController used to create controller.
 * Takes in parameters: Readable in, Appendable out.
 */
public class ReversiTextualController implements ReversiController {

  final Readable in;
  final Appendable out;

  /**
   * Constructor for ReversiTextualController class.
   */
  public ReversiTextualController(Readable in, Appendable out) throws IllegalArgumentException {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable or Appendable is null");
    }
    this.in = in;
    this.out = out;
  }

  // plays the hexagon reversi game.
  @Override
  public void playGame(ReversiModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model is null");
    }
    model.startGame();
    ReversiTextualView view = new ReversiTextualView(model, this.out);
    try {
      view.render();
    }
    catch (IllegalArgumentException e) {
      writeMessage("Error " + e.getMessage() + "\n");
      throw e;
    }
    catch (Exception e) {
      writeMessage("Error " + e.getMessage() + "\n");
      throw new IllegalStateException(e);
    }

    Scanner scan = new Scanner(this.in);
    boolean isQuit = false;
    while (!isQuit && scan.hasNext()) {
      String userCommand = scan.next();
      if (userCommand.equalsIgnoreCase("quit") || userCommand.equalsIgnoreCase("q")) {
        isQuit = true;
      } else {
        processCommand(userCommand, scan, model);
      }
    }
  }

  private void processCommand(String userCommand, Scanner sc, ReversiModel model) {
    int row;
    int col;

    switch (userCommand) {
      case "make-move":
        try {
          row = sc.nextInt(); //get in the row number
          col = sc.nextInt(); //get in the column number
          model.makeMove(row, col);
          writeMessage(model.toString());
        } catch (IllegalArgumentException | IllegalStateException e) {
          writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        }
        break;
      case "pass-turn":
        try {
          model.passTurn();
          writeMessage(model.toString());
        } catch (IllegalArgumentException | IllegalStateException e) {
          writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        }
        break;
      default: //error due to unrecognized command
        writeMessage("Undefined command: " + userCommand + System.lineSeparator());
    }
  }

  private void writeMessage(String message) {
    try {
      this.out.append(message);
    }
    catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}
