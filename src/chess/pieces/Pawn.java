package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece{
	
	private ChessMatch chessmatch;

	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessmatch = chessMatch;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean [][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p =new Position (0,0);
		
		if(getColor() == Color.white) {
			p.setValues(position.getRow() -1 ,position.getColunm());
			if (getBoard().positionExists(p)&&!getBoard().thereIsAPiece(p)) {
				mat [p.getRow()][p.getColunm()]= true;
			}
			p.setValues(position.getRow() -2 ,position.getColunm());
			Position p2 = new Position(position.getRow() -1 ,position.getColunm());
			if (getBoard().positionExists(p)&&!getBoard().thereIsAPiece(p)&&getBoard().positionExists(p2)&&!getBoard().thereIsAPiece(p2)&&getMoveCount()==0) {
				mat [p.getRow()][p.getColunm()]= true;
			}
			p.setValues(position.getRow() -1 ,position.getColunm()-1);
			if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
				mat [p.getRow()][p.getColunm()]= true;
			}
			p.setValues(position.getRow() -1 ,position.getColunm()+1);
			if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
				mat [p.getRow()][p.getColunm()]= true;
			}
			
			//#specialmove em passant white
			if(position.getRow()== 3) {
				Position left = new Position(position.getRow(), position.getColunm()-1);
				if(getBoard().positionExists(left) && isThereOpponentPiece(left)&& getBoard().piece(left)== chessmatch.getenPassantVunerable()) {
					mat[left.getRow()-1][left.getColunm()]=true;
				}
			}
			if(position.getRow()== 3) {
				Position right = new Position(position.getRow(), position.getColunm()+1);
				if(getBoard().positionExists(right) && isThereOpponentPiece(right)&& getBoard().piece(right)== chessmatch.getenPassantVunerable()) {
					mat[right.getRow()-1][right.getColunm()]=true;
				}
			}
		}
		else {
			p.setValues(position.getRow() +1 ,position.getColunm());
			if (getBoard().positionExists(p)&&!getBoard().thereIsAPiece(p)) {
				mat [p.getRow()][p.getColunm()]= true;
			}
			p.setValues(position.getRow() +2 ,position.getColunm());
			Position p2 = new Position(position.getRow() +1 ,position.getColunm());
			if (getBoard().positionExists(p)&&!getBoard().thereIsAPiece(p)&&getBoard().positionExists(p2)&&!getBoard().thereIsAPiece(p2)&&getMoveCount()==0) {
				mat [p.getRow()][p.getColunm()]= true;
			}
			p.setValues(position.getRow() +1 ,position.getColunm()-1);
			if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
				mat [p.getRow()][p.getColunm()]= true;
			}
			p.setValues(position.getRow() +1 ,position.getColunm()+1);
			if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
				mat [p.getRow()][p.getColunm()]= true;
			}
			
		}
		//#specialmove em passant black
		if(position.getRow()== 4) {
			Position left = new Position(position.getRow(), position.getColunm()-1);
			if(getBoard().positionExists(left) && isThereOpponentPiece(left)&& getBoard().piece(left)== chessmatch.getenPassantVunerable()) {
				mat[left.getRow()+1][left.getColunm()]=true;
			}
		}
		if(position.getRow()== 4) {
			Position right = new Position(position.getRow(), position.getColunm()+1);
			if(getBoard().positionExists(right) && isThereOpponentPiece(right)&& getBoard().piece(right)== chessmatch.getenPassantVunerable()) {
				mat[right.getRow()+1][right.getColunm()]=true;
			}
		}
		
		
		return mat;
	

	}
	@Override
	public String toString () {
		return "P";
	}
}
