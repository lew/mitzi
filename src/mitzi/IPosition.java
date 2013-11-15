package mitzi;

import java.util.Set;

public interface IPosition {

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

	public class MoveApplication {
		IPosition new_position;
		boolean resets_half_move_clock = false;
	}

	public MoveApplication doMove(IMove move);

	public Side getActiveColor();

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
	
	public Boolean colorCanCastle(Side color);
	
	public Set<Integer> getOccupiedSquaresByColor(Side color);

	public Set<Integer> getOccupiedSquaresByType(Piece type);

	public Set<Integer> getOccupiedSquaresByColorAndType(Side color, Piece type);

	public int getNumberOfPiecesByColor(Side color);

	public int getNumberOfPiecesByType(Piece type);

	public int getNumberOfPiecesByColorAndType(Side color, Piece type);

	public Set<IMove> getPossibleMoves();

	public Set<IMove> getPossibleMovesFrom(int square);

	public Set<IMove> getPossibleMovesTo(int square);
	
	public Side getSideFromBoard(int square);
	
	public Piece getPieceFromBoard(int square);

	public boolean isCheckPosition();

	public boolean isMatePosition();
	
	public boolean isStaleMatePosition();

	public boolean isPossibleMove(IMove move);

	public String toFEN();

}
