package mitzi;

import java.util.Set;

public interface IBoard {

	/**
	 * Sets the board to the initial position at the start of a game.
	 */
	public void setToInitial();

	/**
	 * Sets the board to a position given in Forsyth–Edwards Notation (FEN).
	 * 
	 * @see <a
	 *      href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">Wikipedia
	 *      - Forsyth–Edwards Notation</a>
	 */
	public void setToFEN(String fen);

	public IBoard doMove(IMove move);

	public int getActiveColor();

	/**
	 * This is the number of halfmoves since the last pawn advance or capture.
	 * This is used to determine if a draw can be claimed under the fifty-move
	 * rule.
	 * 
	 * @return number of halfmoves since the last pawn advance or capture
	 */
	public int getHalfMoveClock();

	/**
	 * The number of the full move. It starts at 1, and is incremented after
	 * Black's move.
	 * 
	 * @return number of the full move
	 */
	public int getFullMoveClock();

	/**
	 * En passant target square. If there's no en passant target square, this is
	 * -1. If a pawn has just made a two-square move, this is the position
	 * "behind" the pawn. This is recorded regardless of whether there is a pawn
	 * in position to make an en passant capture.
	 * 
	 * @return the square "behind" the pawn which can be take en passant
	 */
	public int getEnPassant();

	/**
	 * Check if the king can use castling to get to a specified square.
	 * 
	 * @param king_to
	 *            the square to be checked
	 *            
	 * @return true if the king is allowed to move to the square by castling
	 * 
	 * @see <a
	 *      href="http://www.fide.com/fide/handbook?id=124&view=article">FIDE Rule 3.8</a>
	 */
	public boolean canCastle(int king_to);

	public Set<Integer> getOccupiedSquaresByColor(int color);

	public Set<Integer> getOccupiedSquaresByType(int type);

	public Set<Integer> getOccupiedSquaresByColorAndType(int color, int type);

	public int getNumberOfPiecesByColor(int color);

	public int getNumberOfPiecesByType(int type);

	public int getNumberOfPiecesByColorAndType(int color, int type);

	public Set<IMove> getPossibleMoves();

	public Set<IMove> getPossibleMovesFrom();

	public Set<IMove> getPossibleMovesTo();

	public boolean isCheckPosition();

	public boolean isMatePosition();

	public boolean isPossibleMove(IMove move);

	public String toFEN();

}
