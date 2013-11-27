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
	private Map<IMove, Integer> move_values = new HashMap<IMove, Integer>(35, 1);

	/**
	 * contains values for move comparison
	 */
	private static final int[] piece_values = { 100, 500, 325, 325, 975, 000 };

	public BasicMoveComparator(IPosition board) {
		this.board = board;
	}

	/**
	 * Grades an IMove by some heuristics.
	 * 
	 * Ignoring special situations like en passant and castling.
	 * 
	 * @param move
	 */
	private void computeValue(IMove move) {
		int value = 0;

		// moved figure
		Piece src_piece = board.getPieceFromBoard(move.getFromSquare());

		// captured figure
		Piece dest_piece = board.getPieceFromBoard(move.getToSquare());

		if (dest_piece != null) {
			// try to get advantage in exchange
			value += (piece_values[dest_piece.ordinal()] - piece_values[src_piece
					.ordinal()] + 1) * 1024;
		}
		// move with more powerful pieces
		value += piece_values[src_piece.ordinal()];

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
