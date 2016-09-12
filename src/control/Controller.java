/**
 * 
 */
package control;

import java.util.Scanner;

import model.Bishop;
import model.Knight;
import model.Pawn;
import model.Piece;
import model.Place;
import model.Player;
import model.Queen;
import model.Rook;
import model.totalData;

/**
 * this is the controller 
 * @author Jasmine and Risham
 *
 */
public class Controller implements controllerInterface{
	
	/**
	 * all global needed to set the 
	 * en passant move and prior moves
	 * */
	//for en passant, all for pawn
	public static int last_color;
	public static boolean last_pawn = false;
	public static char last_x;
	public static char last_y;
	private boolean promo = false;
	
	Scanner scanner = new Scanner(System.in);
	String input = "";
	/**
	 * constructor
	 * */
	public Controller(){
		
	}
	/**
	 * For running the program
	 * */
	public void play(){
		//WHITE IS PLAYER 1
		//this just controls the printing of white and black turns 
		//while allowing for error inputs
		//0 for black and 1 for white
		int round = 1; 
		boolean play = true;
		boolean success = true;
		while(play){
			if(round%2 !=0){ //odd rounds are white
				System.out.print("White's move: ");
				input = scanner.nextLine();
				if(input.equals("resign")){
					//System.out.println("Player2 Wins!");
					return;
				}
				if(input.equals("draw") && totalData.draw == true){
					System.out.println("draw");
					return;
				}
				else if(totalData.draw == true){
					totalData.draw = false;
				}
				System.out.println("");
				success = runTurn(input, totalData.getPlayer1(), totalData.getPlayer2());
				if(!success){
					continue;
				}else{
					round++;
					//if(totalData.draw!=true)
						totalData.printboard();
				}
			}
			else{ //even rounds are black
				System.out.print("Black's move: ");
				input = scanner.nextLine();
				if(input.equals("resign")){
					//System.out.println("Player1 Wins!");
					return;
				}
				if(input.equals("draw") && totalData.draw == true){
					System.out.println("draw");
					return;
				}
				else if(totalData.draw == true){
					totalData.draw = false;
				}
				System.out.println("");
				success = runTurn(input, totalData.getPlayer2(), totalData.getPlayer1());
				if(!success){
					continue;
				}else{
					round++;
					//if(totalData.draw!=true)
						totalData.printboard();
					}
			
			}
		}
		
	}
	/**
	 * @param input
	 * @param player
	 * @param otherplayer
	 * @return boolean for true or false 
	 * */
	public boolean runTurn(String input, Player player, Player otherPlayer){
		if(!player.inCheck && isStalemate(player, otherPlayer)){
			System.out.println("stalemate");
			System.exit(0);
		}
		String[] inputs = input.split(" ");
		if(inputs.length == 3 && inputs[2].equals("draw?")){
			//the player has requested a draw
			totalData.draw = true;
		}
		else if(inputs.length == 3 && (inputs[2].equals("Q") || inputs[2].equals("N") || inputs[2].equals("P") || inputs[2].equals("B") || inputs[2].equals("R")) ){
			promo = true;
		}
		else if(inputs.length != 2){
			System.out.println("bad input, please enter again");
			totalData.errorInputTurn = true;
			return false;
		}
		if(inputs[0].length() != 2 || inputs[1].length() !=2){
			System.out.println("bad input, please enter again");
			totalData.errorInputTurn = true;
			return false;
		}
		
		Place start = convertInputToPlace(inputs[0]);
		Place end = convertInputToPlace(inputs[1]);
		//Piece startPiece = start.getPiece();
		
		if(start == null || end == null){
			System.out.println("out of bounds, please enter again");
			totalData.errorInputTurn = true;
			return false;
		}
		
		if(!validateMove(start, end)){
			
			System.out.println("Illegal move, try again");
			totalData.errorInputTurn = true;
			return false;
		}else{
			if(start.getPiece().getColor()!=player.getColor()){
				System.out.println("illegal move, you cannot move other player's pieces");
				totalData.errorInputTurn = true;
				return false;
			}
			Player tempPlayer= new Player("temp", -1);
			Place[][] tempBoard = new Place[8][8];
			saveState(player, tempPlayer, tempBoard);
			//call model to move
			int ret = start.getPiece().move(end.getX(), end.getY());
			//System.out.println(ret);
			//get the return for move method
			if(ret>=0 && ret <3){
				//check if last move was of pawn, if it was first time
				if(end.getPiece()!=null && end.getPiece() instanceof Pawn ){
					last_pawn = true;
					}
				
				else{
					last_pawn = false;
				}
				
				//set last color, x and y
				last_color = player.getColor();
				//set last x and y
				if(end.getPiece()!=null){
					last_x = end.getX();
					last_y = end.getY();
				}
				
				
				if(player.inCheck==false){
					//first time it checks and its not true, so their move made it to check if anything. 
					player.inCheck = isCheck(end, otherPlayer, player);
					//System.out.println("in check: " +player.inCheck);
					
				}
				else{
					//check to make sure you are not in check anymore
					player.inCheck = isCheck(end, otherPlayer, player);
					//System.out.println("in check: " +player.inCheck);
					if(player.inCheck){
						System.out.println("Error: your player was in check, make a move that would protect your king");
						undo(player, tempPlayer, tempBoard);
						return false;
					}
									
				}
				//see if your move has placed the other player in check
				otherPlayer.inCheck = isCheck(end, player, otherPlayer);
				if(otherPlayer.inCheck){
					System.out.println("check");
				}
			
				return true;
			}
			else if(ret == 3){
				//for pawn its the replacement
				int revived = player.getDead().size();
				String find="";
				
				while(promo==true && inputs.length==3){
					if(inputs[2].equals("N")){
						Piece knight;
						if(player.getColor()==1){
							knight = new Knight("wN", start.getX(), start.getY(), player.getColor());
							totalData.getPlayer1().getLiving().add(knight);
							totalData.getPlayer1().getDead().add(end.getPiece());
							for(int i=0; i< totalData.getPlayer1().getLiving().size(); i++){
								if(end.getPiece().equals(totalData.getPlayer1().getLiving().get(i))){
									totalData.getPlayer1().getLiving().remove(i);
									break;
								}
							}
						}else{
							knight = new Knight("bN", ' ', ' ', player.getColor());
							totalData.getPlayer2().getLiving().add(knight);
							totalData.getPlayer2().getDead().add(end.getPiece());
							for(int i=0; i< totalData.getPlayer2().getLiving().size(); i++){
								if(end.getPiece().equals(totalData.getPlayer2().getLiving().get(i))){
									totalData.getPlayer2().getLiving().remove(i);
									break;
								}
							}
						}
						
						end.setPiece(knight);
						System.out.println(end.getPiece().getName() + " " + end.getPiece().getType());
						
						find = "knight";
						break;
					}
					else if(inputs[2].equals("R")){
						Piece rook;
						if(player.getColor()==1){
							rook = new Rook("wR", start.getX(), start.getY(), player.getColor());
							totalData.getPlayer1().getLiving().add(rook);
							totalData.getPlayer1().getDead().add(end.getPiece());
							for(int i=0; i< totalData.getPlayer1().getLiving().size(); i++){
								if(end.getPiece().equals(totalData.getPlayer1().getLiving().get(i))){
									totalData.getPlayer1().getLiving().remove(i);
									break;
								}
							}
						}else{
							rook = new Rook("bR", ' ', ' ', player.getColor());
							totalData.getPlayer2().getLiving().add(rook);
							totalData.getPlayer2().getDead().add(end.getPiece());
							for(int i=0; i< totalData.getPlayer2().getLiving().size(); i++){
								if(end.getPiece().equals(totalData.getPlayer2().getLiving().get(i))){
									totalData.getPlayer2().getLiving().remove(i);
									break;
								}
							}
						}
						
						end.setPiece(rook);
						find = "rook";
						break;
					}
					else if(inputs[2].equals("B")){
						Piece bishop;
						if(player.getColor()==1){
							bishop = new Bishop("wB", start.getX(), start.getY(), player.getColor());
							totalData.getPlayer1().getLiving().add(bishop);
							totalData.getPlayer1().getDead().add(end.getPiece());
							for(int i=0; i< totalData.getPlayer1().getLiving().size(); i++){
								if(end.getPiece().equals(totalData.getPlayer1().getLiving().get(i))){
									totalData.getPlayer1().getLiving().remove(i);
									break;
								}
							}
						}else{
							bishop = new Bishop("bB", ' ', ' ', player.getColor());
							totalData.getPlayer2().getLiving().add(bishop);
							totalData.getPlayer2().getDead().add(end.getPiece());
							for(int i=0; i< totalData.getPlayer2().getLiving().size(); i++){
								if(end.getPiece().equals(totalData.getPlayer2().getLiving().get(i))){
									totalData.getPlayer2().getLiving().remove(i);
									break;
								}
							}
						}
						
						end.setPiece(bishop);
						find = "bishop";
						break;
					}
					else if(inputs[2].equals("Q")){
						Piece queen;
						if(player.getColor()==1){
							queen = new Queen("wQ", start.getX(), start.getY(), player.getColor());
							totalData.getPlayer1().getLiving().add(queen);
							totalData.getPlayer1().getDead().add(end.getPiece());
							for(int i=0; i< totalData.getPlayer1().getLiving().size(); i++){
								if(end.getPiece().equals(totalData.getPlayer1().getLiving().get(i))){
									totalData.getPlayer1().getLiving().remove(i);
									break;
								}
							}
							
						}else{
							queen = new Queen("bQ", ' ', ' ', player.getColor());
							totalData.getPlayer2().getLiving().add(queen);
							totalData.getPlayer2().getDead().add(end.getPiece());
							for(int i=0; i< totalData.getPlayer2().getLiving().size(); i++){
								if(end.getPiece().equals(totalData.getPlayer2().getLiving().get(i))){
									totalData.getPlayer2().getLiving().remove(i);
									break;
								}
							}
						}
						
						end.setPiece(queen);
						find = "queen";
						break;
					}
					else{
						System.out.println("Error: invalid selection, enter again from {N, Q, R, P, B}");
						inputs[2] = scanner.nextLine();
					}
					
				}
				if(inputs.length==2){
					//no input given
					Piece queen;
					if(player.getColor()==1){
						queen = new Queen("wQ", start.getX(), start.getY(), player.getColor());
						totalData.getPlayer1().getLiving().add(queen);
						totalData.getPlayer1().getDead().add(end.getPiece());
						for(int i=0; i< totalData.getPlayer1().getLiving().size(); i++){
							if(end.getPiece().equals(totalData.getPlayer1().getLiving().get(i))){
								totalData.getPlayer1().getLiving().remove(i);
								break;
							}
						}
						
					}else{
						queen = new Queen("bQ", ' ', ' ', player.getColor());
						totalData.getPlayer2().getLiving().add(queen);
						totalData.getPlayer2().getDead().add(end.getPiece());
						for(int i=0; i< totalData.getPlayer2().getLiving().size(); i++){
							if(end.getPiece().equals(totalData.getPlayer2().getLiving().get(i))){
								totalData.getPlayer2().getLiving().remove(i);
								break;
							}
						}
					}
					
					end.setPiece(queen);
					find = "queen";
				}
				
				int i;
				for(i=0; player.getDead().isEmpty()!=true && i<player.getDead().size(); i++){
					if(player.getDead().get(i).getType().equals(find)){
						break;
					}
				}
				
				if(i == player.getDead().size()){
					/*System.out.println("Error: invalid replacement, this piece was not captured");
					while(revived >= player.getDead().size()){
						System.out.println("choose a captured piece to promote your pawn to");
						for( i=0; i<player.getDead().size(); i++){
							System.out.println(i + ": " + player.getDead().get(i).getType());
						}
						System.out.println("enter the number of the piece you want");
						revived= scanner.nextInt();
						if(revived >= player.getDead().size()){
							System.out.println("invalid selection");
						}
					}
					
					player.getDead().add(end.getPiece());
					end.setPiece(player.getDead().get(revived));*/
				}
				else{
					/*player.getDead().add(end.getPiece());
					end.setPiece(player.getDead().get(i));*/
					
				}
				
				if(player.inCheck==false){
					//first time it checks and its not true, so their move made it to check if anything. 
					player.inCheck = isCheck(end, otherPlayer, player);
					//System.out.println("in check: " +player.inCheck);
					
				}
				else{
					//check to make sure you are not in check anymore
					player.inCheck = isCheck(end, otherPlayer, player);
					//System.out.println("in check: " +player.inCheck);
					if(player.inCheck){
						System.out.println("Error: your player was in check, make a move that would protect your king");
						undo(player, tempPlayer, tempBoard);
						return false;
					}
									
				}
				
				//see if your move has placed the other player in check
				otherPlayer.inCheck = isCheck(end, player, otherPlayer);
				if(otherPlayer.inCheck){
					System.out.println("check");
				}

				return true;
			}
			
		}
		return false;
		
	}
	/**
	 * @param start
	 * @param end
	 * @param player
	 * @param otherPlayer
	 * @return boolean if the player is out of check or not
	 * */
	public boolean outOfCheck(Place start, Place end, Player player, Player otherPlayer){
		Piece piece = start.getPiece();
		piece.checkMove(piece.getX(), piece.getY());
		return true;
	}
	/**
	 * @param start
	 * @param end
	 * @return boolean for valid move or not
	 * */
	public boolean validateMove(Place start, Place end){
		
		if(start.getPiece() == null){
			//if there is no piece at start, error
			return false;
		}
		
		return true;
	}
	
	public void undo(Player player, Player tempPlayer, Place[][] tempBoard){
		//does piece store a pointer to the Place?? TODO
		/*end.setPiece(null);
		oldPlace.setPiece(piece);
		piece.setX(oldX);
		piece.setY(oldY);
		*/
		revertState(player, tempPlayer, tempBoard);
		
		totalData.printboard();
		return;
	}
	/**
	 * @param end
	 * @param player
	 * @param otherPlayer
	 * @return boolean for true or false if other player in check or not
	 * */
	//is the player in check after their move?
	public static boolean isCheck(Place end, Player player, Player otherPlayer){
		
		/*for(int i=0; i<player.getLiving().size(); i++){
			//check if the piece can move to the king spot
			if(player.getLiving().get(i).checkMove(otherPlayer.king.getX(), otherPlayer.king.getY()) ==2 ){
				//System.out.println("if statement");
				otherPlayer.inCheck = true;
				//isCheckmate(end, player, otherPlayer);
				break;
			}
			otherPlayer.inCheck = false;
		}

		return false;*/
		
		for(int i=0; i<player.getLiving().size(); i++){
			//check if the piece can move to the king spot
			//System.out.println(player.getLiving().get(i).getType() + ": ");
			//System.out.println(player.getLiving().get(i).checkMove(otherPlayer.king.getX(), otherPlayer.king.getY()));
			if(player.getLiving().get(i).checkMove(otherPlayer.king.getX(), otherPlayer.king.getY()) ==2 ){
				//System.out.println("if statement");
				otherPlayer.inCheck = true;
				if(isCheckmate(end, player, otherPlayer)){
					System.out.println("checkmate");
					if(player.getColor() == 1){
						System.out.println("White wins");
					}
					else {
						System.out.println("Black wins");
					}
					System.exit(0);
				}
				//System.out.println("check");
				otherPlayer.inCheck =true;
				return true;
			}
			otherPlayer.inCheck = false;
		}
		
		if(!isCheckmate(end, player, otherPlayer) && otherPlayer.inCheck == true){
			//System.out.println("check");
			return true;
		}
		
		return false;
		
	}
	
	private boolean isStalemate(Player player, Player otherPlayer){
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				for(int v=0; v<player.getLiving().size(); v++){
					if(player.getLiving().get(v).checkMove(totalData.arrayBoard[i][j].getX(), totalData.arrayBoard[i][j].getY()) != -1){
						//player has a move
						return false;
					}
					//if(totalData.arrayBoard[i][j];
				}
				
			}
		}
		return true;
	}
	
	private static void revertState(Player player, Player tempPlayer, Place[][] tempBoard){
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				totalData.arrayBoard[i][j].setPiece(tempBoard[i][j].getPiece());
				
				if(totalData.arrayBoard[i][j].getPiece() != null){
					totalData.arrayBoard[i][j].getPiece().setX(totalData.arrayBoard[i][j].getX());
					totalData.arrayBoard[i][j].getPiece().setY(totalData.arrayBoard[i][j].getY());
				}
				
			}
		}
		player.setLiving(tempPlayer.getLiving());
		for(int i=0; i<player.getLiving().size(); i++){
			player.getLiving().get(i).setLiving(true);
		}
		player.setDead(tempPlayer.getDead());
		for(int i=0; i<player.getDead().size(); i++){
			player.getDead().get(i).setLiving(false);
		}
		player.setKing(tempPlayer.king);
	}
	
	private static void saveState(Player player, Player tempPlayer, Place[][] tempBoard){
		for(int i=0; i<8; i++){//colomn
			for(int j=0; j<8; j++){//row
				tempBoard[i][j] = new Place(totalData.arrayBoard[i][j].getX(),totalData.arrayBoard[i][j].getY() , totalData.arrayBoard[i][j].getPiece(), "null");//this doesnt matter
			}
		}
		
		for(int i=0; i<player.getLiving().size();i++){
			tempPlayer.getLiving().add(player.getLiving().get(i));
		}
		
		for(int i=0; i<player.getDead().size();i++){
			tempPlayer.getDead().add(player.getDead().get(i));
		}
		
		tempPlayer.setKing(player.king);
	}
	
	//used only in stillInCheck, difference is not to call checkmate 
	private static boolean isCheckHelper(Player player, Player otherPlayer){
		
		for(int i=0; i<player.getLiving().size(); i++){
			//check if the piece can move to the king spot
			if(player.getLiving().get(i).checkMove(otherPlayer.king.getX(), otherPlayer.king.getY()) ==2 ){
				//System.out.println("if statement");
				
				return true;
			}
			//otherPlayer.inCheck = false;
		}
		return false;
		
	}
	
	private static boolean stillInCheck(Place end, Player player, Player otherPlayer, Piece piece){
		//see what happens when you move the piece to the Place end.
		if(piece.move(end.getX(), end.getY()) == -1){
			return true;//still in check bc you could not make the move
		}
		else{
			//either you capture the other player's king or you promote a pawn or you move normally.
			//call isCheckHelper to see if we are still in check
			if(!isCheckHelper(player, otherPlayer)){
				//no longer in check can return false. you are not in checkmate
				return false;
			}
			else{
				return true;
			}
		}
	}
	
	private static boolean InCheck(Player player, Player otherPlayer){
		//see what happens when you move the piece to the Place end.
		for(int i=0; i<player.getLiving().size(); i++){
			//check if the piece can move to the king spot
			//System.out.println(player.getLiving().get(i).getType() + ": ");
			//System.out.println(player.getLiving().get(i).checkMove(otherPlayer.king.getX(), otherPlayer.king.getY()));
			if(player.getLiving().get(i).checkMove(otherPlayer.king.getX(), otherPlayer.king.getY()) ==2 ){
				//System.out.println("if statement");
				return true;
			}
			
		}
		
		
		return false;
	}
	
	
	//is the game at checkmate?? as in, can otherPlayer move any of its pieces to save king from checkmate?
	
	/**
	 * @param end
	 * @param player
	 * @param otherPlayer
	 * @return boolean if its checkmate or not
	 * */
	public static boolean isCheckmate(Place end, Player player, Player otherPlayer){
		
		
			//you would need to check for all the possible moves that can get them out of the check
			//check if otherplayer is in check mate
			
			for(int i=0; i<otherPlayer.getLiving().size(); i++){
				//check if the other player can have different moves to the other parts of the board
				boolean doub = otherPlayer.getLiving().get(i).isDoub();
				int rep = otherPlayer.getLiving().get(i).getRep();
				for(int row=0; row<8;row++){
					for(int col =0; col<8; col++){
						//System.out.println(otherPlayer.getLiving().get(i).getName()+ " " + otherPlayer.getLiving().get(i).getX() + otherPlayer.getLiving().get(i).getY()+ " "+(char)('0'+col+49)+""+ (char)('0'+row+1));
						//check every possible move on the board and then check if its in check, also make a temp board
						if(totalData.pieceCheckMove(otherPlayer.getLiving().get(i), otherPlayer.getLiving().get(i).getX(), otherPlayer.getLiving().get(i).getY(), (char)('0'+col+49), (char)('0'+row+1)) !=-1){
							//move is valid at that spot
							//check if its in going to be check or not
							Player tempPlayer= new Player("temp", -1);
							Place[][] tempBoard = new Place[8][8];
							saveState(otherPlayer, tempPlayer, tempBoard);
							//now move the piece
							
							int ret = otherPlayer.getLiving().get(i).move((char)('0'+col+49), (char)('0'+row+1));
							boolean in_check = true;
							if(ret==2){
								//revert it back
								in_check = false; //thats others king, so its all good
							}
							else if(ret!=-1){
								//check if it will be still be in check or not
								in_check = InCheck(player,otherPlayer);
							}
							else{
								//not a valid move
								in_check = true;
							}
							
							//revert to normal
							revertState(otherPlayer, tempPlayer, tempBoard);
							otherPlayer.getLiving().get(i).setDoub(doub);
							otherPlayer.getLiving().get(i).setRep(rep);
							if(in_check==false){
								return false; //it is not in checkmate
							}
						}
					}
				
				}
		
		
			}
		System.out.println("checkmate");
		if(player.getColor()==0)
			System.out.println("black wins");
		else
			System.out.println("white wins");
		
		System.exit(0);
		
		return true;
		/*for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				//for every place on board
				for(int v=0; v<otherPlayer.getLiving().size(); v++){
					//iterate through living to see what happens if they move there
					Player tempPlayer= new Player("temp", -1);
					Place[][] tempBoard = new Place[8][8];
					saveState(otherPlayer, tempPlayer, tempBoard);
					if(!stillInCheck(totalData.arrayBoard[i][j], player, otherPlayer, otherPlayer.getLiving().get(v))){
						//found a move to escape check, game is not in checkmate
						revertState(otherPlayer, tempPlayer, tempBoard);
						return false;
					}else{
						revertState(otherPlayer, tempPlayer, tempBoard);
					}
					//if(totalData.arrayBoard[i][j];
				}
				
			}
		}

		if(player.getColor()==0)
			System.out.println("black wins");
		else
			System.out.println("white wins");
		
		System.exit(0);
		return true;*/
	}
	
	//calls findplace
	//returns pointer to a Place on the board
	//if not a place, returns null
	private Place convertInputToPlace(String input){
		//at 0 it will be x and at 1 it will be y
		if(input.charAt(0)>='a' && input.charAt(0)<='h' && input.charAt(1)>='1' && input.charAt(1)<='8')
		return findPlace(input.charAt(0), input.charAt(1));
		else
			return null;
		
	}
	
	private Place findPlace(char x, char y){
		Place place = null;
		for(int i=0; i<totalData.getArrayBoard().length; i++){
			for(int j=0; j< totalData.getArrayBoard()[i].length; j++){
				if(totalData.getArrayBoard()[i][j].getX() ==x && totalData.getArrayBoard()[i][j].getY()==y){
					place = totalData.getArrayBoard()[i][j];
				}
			}
		}
		return place;
		
	}
}


