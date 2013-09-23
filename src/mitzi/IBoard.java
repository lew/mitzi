package mitzi;

import java.util.Set;

public interface IBoard {

	public void setToInitial();

	public void setToFEN(String fen);

	public IBoard doMove(IMove move);
	
	public boolean canEnPassant(int hit_field);
	
	public boolean canRochade(int king_to);

	public Set<Integer> getOccupiedSquaresByColor(int color);

	public Set<Integer> getOccupiedSquaresByType(int type);

	public Set<Integer> getOccupiedSquaresByColorAndType(int color, int type);

	public int getNumberOfFiguresByColor(int color);

	public int getNumberOfFiguresByType(int type);

	public int getNumberOfFiguresByColorAndType(int color, int type);

	public Set<IMove> getPossibleMoves();

	public Set<IMove> getPossibleMovesFrom();

	public Set<IMove> getPossibleMovesTo();

	public boolean isCheckPosition();

	public boolean isMatePosition();

	public boolean isPossibleMove(IMove move);

	public String toFEN();

}
