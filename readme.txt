The way to run: 
Our main function is in package chess in Chess.java file.
we have also implemented the extra credit. 


it runs with white player's move first: 
every player has to declare start position and end position. 

Board will be printed out:

bR bN bB bQ bK bB bN bR 8
bp bp bp bp bp bp bp bp 7
   ##    ##    ##    ## 6
##    ##    ##    ##    5
   ##    ##    ##    ## 4
##    ##    ##    ##    3 
wp wp wp wp wp wp wp wp 2
wR wN wB wQ wK wB wN wR 1
 a  b  c  d  e  f  g  h

How to state what position
e2 e4

output:

	Normal, going to the next player's move and printing the board

or, input problems:

	bad input, please enter again
	
or, if no piece exists at the start point:
	
	Illegal move, try again

or, if input given is out of bounce:

	out of bounds, please enter again
	
or, if the move is not legal for that particular piece

	Illegal move, try again

or, if a piece already exists where you are trying to move

	Error: You cannot move where your piece already exists
	
or, in King, you are trying to do castling and it is not possible:

	Error: Castling is not possible

or, if any piece except Knight is jumping over pieces: 
	
	Error: <Piece> cannot jump over pieces

In all these outputs, the same player gets to enter again and player doesn't get switched

For promotion:

expected input:

g7 g8 N  

Instead of N:
N = knight
B = bishop
P = pawn
R = rook
Q = queen
if nothing is provided, it would be queen

Output:

it would be replaced with the dead pieces

or, if the replace piece does not exists, it would print out all the pieces for the person and it would take an integer as an input
for the piece it wants to replace

or, if there are no captured pieces for the player, it would just say no captured pieces and take out the pawn from that site
and print this out:

Error: nothing could replace your pawn, you have no captured pieces

or, if input given does not exists, below error is printed out and asked for input again

Error: invalid selection, enter again{N, Q, R, P, B}

To resign:
input:

resign
	
output:

just exists out

To draw:
input:

g1 f3 draw?

it would not print a board and ask another user for its input, if its 

draw

then it would end or else continue. 

To win:

if a player is in check it needs to move one of its pieces to protect himself. 
if a player is under checkmate, it would end the game with printing:

Checkmate
<Player> wins