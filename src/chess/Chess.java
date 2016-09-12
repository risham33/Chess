package chess;

import java.util.Scanner;

import control.Controller;
import model.Bishop;
import model.King;
import model.Knight;
import model.Pawn;
import model.Piece;
import model.Place;
import model.Player;
import model.Queen;
import model.Rook;
import model.totalData;

/**
 * This is the view class
 * @author Jasmine and Risham
 *
 */

public class Chess {
	
	/**
	 * this are the global variables for chess
	 * 
	 * */
	static Scanner scanner = new Scanner(System.in);
	static String input = "";
	public static int[][] test = new int[8][8];
	static int letter = 1;
	static Controller control = new Controller();
	//static char number = '1';
	
	/**
	 * @param args of arguments
	 * main method
	 * 
	 * */
	public static void main(String[] args) {
		setup();
		//printBoard();
		totalData.printboard();
		control.play();
		
	}
	
	private static void setup(){
		setupBoard();
		setupPieces();
		setupPieceXY();
		
	}
	
	private static void setupPieceXY(){
		for(int i=0; i<totalData.getArrayBoard().length; i++){
			for(int y=0; y< totalData.getArrayBoard()[i].length; y++){
				if(totalData.getArrayBoard()[i][y].getPiece() != null){
					//set that piece's x y
					char x = totalData.getArrayBoard()[i][y].getX();
					char Y = totalData.getArrayBoard()[i][y].getY();
					totalData.getArrayBoard()[i][y].getPiece().setX(x);
					totalData.getArrayBoard()[i][y].getPiece().setY(Y);
				}
				
			}
		}
		
	}
	
	private static void setupPieces(){
		/**
		 * black = 0, white =1
		 */
		
		//for white
		
		//8 pawns
		for(int i=0; i<8; i++){
			Piece piece = new Pawn("wp", ' ', ' ', 1);
			totalData.getPlayer1().getLiving().add(piece);
			totalData.getArrayBoard()[1][i].setPiece(piece);
		}
		//2 rooks	
		Piece rook1 = new Rook("wR", ' ', ' ', 1);
		totalData.getPlayer1().getLiving().add(rook1);
		totalData.getArrayBoard()[0][0].setPiece(rook1);
		Piece rook2 = new Rook("wR", ' ', ' ', 1);
		totalData.getPlayer1().getLiving().add(rook2);
		totalData.getArrayBoard()[0][7].setPiece(rook2);

		//2 knights
		Piece knight1 = new Knight("wN", ' ', ' ', 1);
		totalData.getPlayer1().getLiving().add(knight1);
		totalData.getArrayBoard()[0][1].setPiece(knight1);
		Piece knight2 = new Knight("wN", ' ', ' ', 1);
		totalData.getPlayer1().getLiving().add(knight2);
		totalData.getArrayBoard()[0][6].setPiece(knight2);
		
		//2 bishops
		Piece bishop1 = new Bishop("wB", ' ', ' ', 1);
		totalData.getPlayer1().getLiving().add(bishop1);
		totalData.getArrayBoard()[0][2].setPiece(bishop1);
		Piece bishop2 = new Bishop("wB", ' ', ' ', 1);
		totalData.getPlayer1().getLiving().add(bishop2);
		totalData.getArrayBoard()[0][5].setPiece(bishop2);
		
		//1 queen
		Piece queen1 = new Queen("wQ", ' ', ' ', 1);
		totalData.getPlayer1().getLiving().add(queen1);
		totalData.getArrayBoard()[0][3].setPiece(queen1);
		//1 king
		Piece king1 = new King("wK", ' ', ' ', 1);
		totalData.getPlayer1().getLiving().add(king1);
		totalData.getPlayer1().king = king1;
		totalData.getArrayBoard()[0][4].setPiece(king1);
		
		//-----------------
		//for black
		//8 pawns
		for(int i=0; i<8; i++){
			Piece piece = new Pawn("bp", ' ', ' ', 0);
			totalData.getPlayer2().getLiving().add(piece);
			totalData.getArrayBoard()[6][i].setPiece(piece);
		}
		//2 rooks	
		Piece rook3 = new Rook("bR", ' ', ' ', 0);
		totalData.getPlayer2().getLiving().add(rook3);
		totalData.getArrayBoard()[7][0].setPiece(rook3);
		Piece rook4 = new Rook("bR", ' ', ' ', 0);
		totalData.getPlayer2().getLiving().add(rook4);
		totalData.getArrayBoard()[7][7].setPiece(rook4);

		//2 knights
		Piece knight3 = new Knight("bN", ' ', ' ', 0);
		totalData.getPlayer2().getLiving().add(knight3);
		totalData.getArrayBoard()[7][1].setPiece(knight3);
		Piece knight4 = new Knight("bN", ' ', ' ', 0);
		totalData.getPlayer2().getLiving().add(knight4);
		totalData.getArrayBoard()[7][6].setPiece(knight4);
		
		//2 bishops
		Piece bishop3 = new Bishop("bB", ' ', ' ', 0);
		totalData.getPlayer2().getLiving().add(bishop3);
		totalData.getArrayBoard()[7][2].setPiece(bishop3);
		Piece bishop4 = new Bishop("bB", ' ', ' ', 0);
		totalData.getPlayer2().getLiving().add(bishop4);
		totalData.getArrayBoard()[7][5].setPiece(bishop4);
		
		//1 queen
		Piece queen2 = new Queen("bQ", ' ', ' ', 0);
		totalData.getPlayer2().getLiving().add(queen2);
		totalData.getArrayBoard()[7][3].setPiece(queen2);
		//1 king
		Piece king2 = new King("bK", ' ', ' ', 0);
		totalData.getPlayer2().getLiving().add(king2);
		totalData.getPlayer2().king = king2;
		totalData.getArrayBoard()[7][4].setPiece(king2);
		
		
		//set the king location
		totalData.setPlayerKing(king1, totalData.getPlayer1());
		totalData.setPlayerKing(king2, totalData.getPlayer2());
	}
	
	private static void setupBoard(){
		//e 6 null "  "| "##"
		String name = "error";
		char number = '1';
		for(int i=0; i<totalData.getArrayBoard().length; i++){
			//row
			
			char letter = 'a';
			for(int y=0; y<totalData.getArrayBoard().length; y++){
				//column
				if((i%2==0 && y%2==0) || i%2!=0 && y%2 !=0){
					//even, even OR odd, odd is spaces
					name = "  ";
				}
				else{
					name = "##";
				}
				Place place = new Place(letter, number, null, name);
				totalData.getArrayBoard()[i][y] = place;
				letter++;
			}
			number++;
		}
	}
	private static void printBoard(){
		for(int i=0; i<totalData.getArrayBoard().length; i++){
			for(int y=0; y< totalData.getArrayBoard()[i].length; y++){
				System.out.print(" " + totalData.getArrayBoard()[i][y].getX()+ totalData.getArrayBoard()[i][y].getY() + " ");
			}
			System.out.println("");
		}
		
		System.out.println("");
		System.out.println("");
		
		for(int i=0; i<totalData.getArrayBoard().length; i++){
			for(int y=0; y< totalData.getArrayBoard()[i].length; y++){
				System.out.print(" " + totalData.getArrayBoard()[i][y].getName());
			}
			System.out.println("");
		}
	}
	
	
}