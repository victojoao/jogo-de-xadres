package chess;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Rook;

public class ChessMatch {
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {
		board = new Board(8, 8);
		turn =1;
		currentPlayer = Color.white;
		check = false;
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	public boolean [][] possibleMoves(ChessPosition sourcepoPosition){
		Position position = sourcepoPosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source,target);
		Piece capturedPiece = makeMove(source, target);
		
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else 
		{		
		nextTurn();
		}
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;
	}
	private void undoMove(Position source, Position target, Piece captuPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target)	;
		p.deacreseMoveCount();
		board.placePiece(p, source);
		
		if (captuPiece != null) {
			board.placePiece(captuPiece, target);
			capturedPieces.remove(captuPiece);
			piecesOnTheBoard.add(captuPiece);
		}
		}
		
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
		}
		if (!board.piece(position).isThearAnyPossibleMove()) {
			throw new ChessException("There is not possible moves for the chosen piece");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).posssibleMoves(target)) {
			throw new ChessException("The chosen piece can't move to target positon");
		}
		
	}
	
	private void nextTurn() {
		turn ++;
		currentPlayer = (currentPlayer == Color.white) ? Color.black : Color.white;
	}
	
	
	private Color opponent(Color color) {
		return(color == Color.white)? Color.black :Color.white;
	}

	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List <Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColunm()]) {
				return true;
			}	
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List <Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p :list) {
			boolean [][]mat = p.possibleMoves();
			for(int i =0;i<board.getRows();i++) {
				for(int j =0; j< board.getColumns();j++) {
					if(mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() {
		 	placeNewPiece('a', 1, new Rook(board, Color.white));
	        placeNewPiece('e', 1, new King(board, Color.white));
	        placeNewPiece('h', 1, new Rook(board, Color.white));
	        placeNewPiece('c', 1, new Bishop(board, Color.white));
	        placeNewPiece('f', 1, new Bishop(board, Color.white));
	        placeNewPiece('b', 1, new Knight(board, Color.white));
	        placeNewPiece('g', 1, new Knight(board, Color.white)); 
	        
	        placeNewPiece('a', 2, new Pawn(board, Color.white));
	        placeNewPiece('b', 2, new Pawn(board, Color.white));
	        placeNewPiece('c', 2, new Pawn(board, Color.white));
	        placeNewPiece('d', 2, new Pawn(board, Color.white));
	        placeNewPiece('e', 2, new Pawn(board, Color.white));
	        placeNewPiece('f', 2, new Pawn(board, Color.white));
	        placeNewPiece('g', 2, new Pawn(board, Color.white));
	        placeNewPiece('h', 2, new Pawn(board, Color.white));

	        placeNewPiece('a', 8, new Rook(board, Color.black));
	        placeNewPiece('e', 8, new King(board, Color.black));
	        placeNewPiece('h', 8, new Rook(board, Color.black));
	        placeNewPiece('c', 8, new Bishop(board, Color.black));
	        placeNewPiece('f', 8, new Bishop(board, Color.black));
	        placeNewPiece('g', 8, new Knight(board, Color.black));
	        placeNewPiece('b', 8, new Knight(board, Color.black));
	        
	        placeNewPiece('a', 7, new Pawn(board, Color.black));
	        placeNewPiece('b', 7, new Pawn(board, Color.black));
	        placeNewPiece('c', 7, new Pawn(board, Color.black));
	        placeNewPiece('d', 7, new Pawn(board, Color.black));
	        placeNewPiece('e', 7, new Pawn(board, Color.black));
	        placeNewPiece('f', 7, new Pawn(board, Color.black));
	        placeNewPiece('g', 7, new Pawn(board, Color.black));
	        placeNewPiece('h', 7, new Pawn(board, Color.black));
	}
}
