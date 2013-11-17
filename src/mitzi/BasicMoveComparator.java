package mitzi;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class BasicMoveComparator implements Comparator<IMove> {

	/**
	 * saves the actual board, where the moves should be compared
	 */
	private IPosition board;
	
	/**
	 * map, which maps a move to its value. Initial size set to 35 to prevent 
	 */
	private Map<IMove, Integer> move_values = new HashMap<IMove, Integer>(35,1);

	/**
	 * contains values for move comparison (for source piece)
	 */
	private static final Map<Piece, Integer> src_values = new HashMap<Piece, Integer>(6,1);
	static {
		// initialize src_values
		src_values.put(Piece.QUEEN, 5);
		src_values.put(Piece.ROOK, 4);
		src_values.put(Piece.BISHOP, 3);
		src_values.put(Piece.KNIGHT, 2);
		src_values.put(Piece.PAWN, 1);
		src_values.put(Piece.KING, 0);
	}

	/**
	 * contains values for move comparison (for dest piece)
	 */
	private static final Map<Piece, Integer> dest_values = new HashMap<Piece, Integer>(6,1);
	static {
		// initialize dest_values
		dest_values.put(Piece.QUEEN, 500);
		dest_values.put(Piece.ROOK, 400);
		dest_values.put(Piece.BISHOP, 300);
		dest_values.put(Piece.KNIGHT, 200);
		dest_values.put(Piece.PAWN, 100);
		dest_values.put(null, 0);
	}

	public BasicMoveComparator(IPosition board) {
		this.board = board;
	}

	/**
	 * Grades an IMove by the following system: Capturing counts most, then
	 * moving a piece.
	 * 
	 * Among all captures, capturing a more valuable piece counts more. Among
	 * moves of pieces, moving a more valuable piece counts more.
	 * 
	 * Ignoring special situations like en passant and castling.
	 * 
	 * @param move
	 */
	private void computeValue(IMove move) {
		int value = 0;

		// moved figure
		Piece src_piece = board.getPieceFromBoard(move.getFromSquare());
		value += src_values.get(src_piece);

		// captured figure
		Piece dest_piece = board.getPieceFromBoard(move.getToSquare());
		value += dest_values.get(dest_piece);

		move_values.put(move, value);
	}

	/**
	 * compares two moves by there value.
	 */
	@Override
	public int compare(IMove m1, IMove m2) {
		if (!move_values.containsKey(m1))
			computeValue(m1);
		if (!move_values.containsKey(m2))
			computeValue(m2);

		return Integer.compare(move_values.get(m1), move_values.get(m2));
	}

}
