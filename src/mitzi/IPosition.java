package mitzi;

import java.util.LinkedList;
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

	/**
	 * 
	 * This class represents the result of the doMove function and, recognizes
	 * if the half_move_clock should be set.
	 * 
	 */
	public class MoveApplication {
		IPosition new_position;
		boolean resets_half_move_clock = false;
	}

	/**
	 * Performs the given move and returns a new position.
	 * 
	 * @param move
	 *            the move, which should be performed. Please note, that the
	 *            move must be valid, no checking is done.
	 * @return the new board and a boolean, if the half_move_clock should be
	 *         reseted.
	 */
	public MoveApplication doMove(IMove move);

	/**
	 * Returns, which side has to move.
	 * 
	 * @return the active Side of the actual position
	 */
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
	 * @see <a href="http://www.fide.com/fide/handbook?id=124&view=article">FIDE
	 *      Rule 3.8</a>
	 */
	public boolean canCastle(int king_to);

	/**
	 * The position stores also an eventual analysis result from board
	 * evaluation.
	 * 
	 * @return the analysis result of the board.
	 */
	public AnalysisResult getAnalysisResult();

	/**
	 * Sets/update the actual analysis result.
	 * 
	 * @param new_result
	 *            the new analysis result.
	 */
	public void updateAnalysisResult(AnalysisResult new_result);

	/**
	 * Checks if a given side, can still castle.
	 * 
	 * @param color
	 *            the given side
	 * @return true, if the given side can castle, false else.
	 */
	public Boolean colorCanCastle(Side color);

	/**
	 * Returns all squares, occupied by a given side.
	 * 
	 * @param color
	 *            the given side
	 * @return a set of integers, containing all squares, where a piece of this
	 *         side is placed.
	 */
	public Set<Integer> getOccupiedSquaresByColor(Side color);

	/**
	 * Returns all squares, occupied by a given piece.
	 * 
	 * @param type
	 *            the given piece
	 * @return a set of integers, containing all squares, where this piece is
	 *         placed.
	 */
	public Set<Integer> getOccupiedSquaresByType(Piece type);

	/**
	 * Returns all squares, occupied by a given piece and side.
	 * 
	 * @param color
	 *            the given side
	 * @param type
	 *            the given piece
	 * @return a set of integers, containing all squares, where the piece of
	 *         this side is placed.
	 */
	public Set<Integer> getOccupiedSquaresByColorAndType(Side color, Piece type);

	/**
	 * Returns the number of occupied squares by a given side.
	 * 
	 * @param color
	 *            the given side
	 * @return the number of squares, where a piece of the given side is placed.
	 */
	public int getNumberOfPiecesByColor(Side color);

	/**
	 * Returns the number of occupied squares by a given piece.
	 * 
	 * @param type
	 *            the given piece
	 * @return the number of squares, where the piece is placed.
	 */
	public int getNumberOfPiecesByType(Piece type);

	/**
	 * Returns the number of occupied squares by a given piece and side.
	 * 
	 * @param color
	 *            the given side
	 * @param type
	 *            the given piece
	 * @return the number of squares, where the piece of this side is placed.
	 */
	public int getNumberOfPiecesByColorAndType(Side color, Piece type);

	/**
	 * Computes all possible moves for the active side. Moves, where the active
	 * color is check, are invalid and got deleted.
	 * 
	 * @return a set of all valid and possible moves.
	 */
	public Set<IMove> getPossibleMoves();

	/**
	 * Computes all possible moves for the active side from a specific square.
	 * Moves, where the active color is check, are invalid and got deleted.
	 * 
	 * @param square
	 *            the given square
	 * @return a set of all valid and possible moves from the given square.
	 */
	public Set<IMove> getPossibleMovesFrom(int square);

	/**
	 * Computes all possible moves for the active side to a specific square.
	 * Moves, where the active color is check, are invalid and got deleted.
	 * Please note, that this functions calls getPossibleMoves() and extracts
	 * the desired ones.
	 * 
	 * @param square
	 *            the given square
	 * @return a set of all valid and possible moves to the given square.
	 */
	public Set<IMove> getPossibleMovesTo(int square);

	/**
	 * returns the side of the piece on a given square
	 * 
	 * @param square
	 *            the given square
	 * @return the side, if this square is occupied by a side and null if it is
	 *         empty.
	 */
	public Side getSideFromBoard(int square);

	/**
	 * returns the piece on a given square
	 * 
	 * @param square
	 *            the given square
	 * @return the piece, if this square is occupied and null if it is empty.
	 */
	public Piece getPieceFromBoard(int square);

	/**
	 * checks if the actual position is a check position.
	 * 
	 * @return true if the position is a check position
	 */
	public boolean isCheckPosition();

	/**
	 * checks if the actual position is a mate position.
	 * 
	 * @return true if the position is a mate position
	 */
	public boolean isMatePosition();

	/**
	 * checks if the actual position is a stalemate position.
	 * 
	 * @return true if the position is a stalemate position
	 */
	public boolean isStaleMatePosition();

	/**
	 * checks if a given move is a valid move. Note, that this function calls
	 * first getPossibleMoves() and then searches the given move in all possible
	 * moves
	 * 
	 * @param move the move to be checked
	 * @return true, if the move is possible
	 */
	public boolean isPossibleMove(IMove move);

	/**
	 * converts the given position in fen notation
	 * @return a string of the actual position in fen notation
	 */
	public String toFEN();

	/**
	 * searches all moves, which are a capture and promotions
	 * @return the desired set of moves of all captures and promotions.
	 */
	public Set<IMove> generateCaptures();

	public LinkedList<IMove> getBestMoves();
	
	public void addBetterMove(IMove move);
	
	public void resetBestMoves();
	
}
