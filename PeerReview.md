# Provider Code Review
# Design Critique:
Overall the design of their code was quite good, but there were a few things that could have been 
slightly better.
- The theming and use of sprites and a pixel art background image made the view a pleasure to use.
- The representation of the board as a map from hexagon on the board to cell in the 
hexagon made sense and was easy to work with.
- The strategies required the model to provide significant extra information, which 
meant that the IROModel and IMutableModel had several public methods with very limited use.
- The view requires external calling of methods to display the end of the game, even though
it has access to the model, which potentially means that more code needs access to the model and 
the view to call the end of game method.

# Implementation Critique:
The implementation quality was very high, during all of part 8 we didn't run into a single bug that
was fully internal to their implementation.

The only issue we had was that when implementing their 
model interface in a mock we returned the opposite of the current player in the getNextPlayer() method,
however this caused the view to display the wrong player's turn. This was fixed by making getNextPlayer()
return the current player, which seems backwards.

Another minor implementation issue was that in the end of the game the view expects a string
representing who won, or the empty cell if no player won, however the getWinner() method in IROModel
specifies that if the game state is not won the method should throw. This means that instead of directly
passing getWinner() into the displayGameOver() method a try-catch is required to cover the case of a tie.
The model interface and view would work better together if getWinner returned the empty cell when no player won.

There are also a few places where the view expects enum.name() as an argument to a function that
switches on the argument where the function could have just taken the enum directly and switched on it.

# Documentation Critique:
The documentation was overall excellent. Readme was adequate in understanding 
provider implementation of the Reversi game. Comments for interface and method explanations were 
very descriptive and made code easy to work with and understand. Examples would have been helpful 
when documenting difference between Model vs View features. In addition, it would have been nice to
specify which directions were used for +/-q and +/-r in the coordinate system. This was not a 
deal-breaker as they linked to https://www.redblobgames.com/grids/hexagons/, and used the same directions
as that page.
# Design/Code Limitations:
The following functionalities were missing from provider code: While pass-turn action in UI was a necessary actions missing 
from the game, the restart option in UI is purely optional and just would have been nice to have.