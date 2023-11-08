# CS3500-Reversi
Overview:
Reversi is a two-player game played on a regular grid of cells. Each player has a color—black or white—and
the game pieces are discs colored black on one side and white on the other. Game play begins with equal
numbers of both colors of discs arranged in the middle of the grid. In our game, our grid will be made up of
hexagons.

Player Black moves first. On each turn, a player may do one of two things:
They may pass, and let the other player move.
They may select a legal empty cell and play a disc in that cell.

A play is legal for player A if the disc being played is adjacent
1 (in at least one direction) to a straight line of the opponent player B’s discs,
at the far end of which is another player A disc. The result of a legal move is
that all player B’s discs in all directions that are sandwiched between two discs
of player A get flipped to player A. We say that player A has captured player B’s discs.

If a player has no legal moves, they are required to pass.
If both players pass in a row, the game ends.

Modeling game:
The board is represented by 3 2d arrays of cells representing the cells of the game ordered along all three axis of a hexagon.

Visualizing game:
You are not required in this assignment to create a GUI view of your game. Instead, you will start with a
simpler textual view.
<br/>


Textual view:<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*\_ _ _*   <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*\_ _ _ _*   <br/>
&nbsp;&nbsp;*\_ _ X O _ _* <br/>
*\_ _ O _ X _ _*<br/>
&nbsp;&nbsp;*\_ _ X O _ _*<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*\_ _ _ _*<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*\_ _ _*   <br/>
Player one Score: 3<br/>
Player two Score: 3<br/>
Player one turn (Black)!<br/>
<br/>
where <br/>
X - Player one with Black colored Disc<br/>
O - Player two with White colored Disc

<br/><br/>
Source details:
1. BasicReversi model implements the Reversi game.
   This model uses 2 players Player One and Player Two. It uses Hex coordinates based grid.
   It has horizontal, down left and down right rows.
   Each cell extends Location (x,y) coordinates and may have Player's state if it is used by player.
2. ReversiTextualController to take Player One moves from user input, play move and display the output.
3. ReversiTextualView to display the textual output.
4. Classes are implemented for future use:<br/>
   Player.java, BlackPlayer.java, WhitePlayer.java, HexGrid.java, Point.java
5. Functionalities need to be updated for future work such as down left or right rows to support those directional moves.

<br/><br/>
Rule details:
1. Reversi game is implemented with below rules to ensure the move is valid:<br/>
a. The cell to move in is empty.<br/>
b. The cell to move in is adjacent to the one of the opponent player's cells.<br/>
c. When moving in that direction there is a friendly player cell before empty cell/end of list.<br/>
2. Horizontal moves are implemented. It also flips opposite player's discs if move is valid as per the rules of the game.
3. Game pass turns are implemented. If both players pass their turns in a row then game will be over.


# Changes for part 2
Added:
1. isGameOver();<br/>
a. The interface did not expose a way to check if the game was over, so we added a method to do that.<br/>
2. getTileAt(hRow, hIndex);<br/>
a. The interface did not expose a way to see the state of a specific tile, so we added a
method to do that.<br/>
3. getGameBoard();<br/>
a. The only way to get the state of the game before was to parse the string from the toString method,
so we added a method to get the state of each cell.
4. ReadonlyReversiModel interface is created to only support views.
5. ReversiHexGridController is implemented to support GUI based mouse click game actions and views.
6. ReversiFrame interface is created to support GUI based view. BasicReversiView is implemented to support this interface.
7. Hexagon is created to build the Hexagon based grid.
8. Reversi and ReversiGUI Main classes allows one to play this Reversi game in textual and GUI views.
9. Make move mouse clicks, Pass Turns button clicks are implemented as part of Hexagonal Grid GUI based game.

<br/><br/>
New constructor:
1. We did not have a way to set up scenarios to test the model in specific situations,
so we added a new constructor that takes in a list of locations for white tiles and for black tiles
as well as other information about the state of the game.<br/>
2. We also added a copy constructor to be able to make copies of a game in progress.<br/>
a. This required also adding a copy constructor to the cell class.<br/>