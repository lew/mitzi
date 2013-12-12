package mitzi;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CaptureComperator implements Comparator<IMove> {

	/**
	 * saves the actual board, where the moves should be compared
	 */
	private IPosition board;

	/**
	 * map, which maps a move to its value. Initial size set to 35 to prevent
	 */
	private Map<IMove, Integer> move_values = new HashMap<IMove, Integer>(35, 1);

	private static final int[] piece_values = { 100, 500, 325, 325, 975, 000 };

	public CaptureComperator(IPosition board) {
		this.board = board.returnCopy();
	}

	private int seeCapture(IMove m) {
		int value = 0;

		Piece piece = board.getPieceFromBoard(m.getToSquare());
		if (piece == null)
			piece = Piece.PAWN; // en_passant

		board.doMove(m);

		value = (piece_values[piece.ordinal()] - see(m.getToSquare()));

		board.undoMove(m);

		move_values.put(m, value);
		return value;
	}

	private int see(int square) {

		int value = 0;
		IMove move = board.get_smallest_attacker(square);
		/* skip if the square isn't attacked anymore by this side */
		if (move != null) {
			Piece piece = board.getPieceFromBoard(move.getToSquare());
			board.doMove(move);

			// Do not consider captures if they lose material, therefore max
			// zero
			value = Math.max(0, piece_values[piece.ordinal()] - see(square));

			board.undoMove(move);
		}
		return value;
	}

	@Override
	public int compare(IMove m1, IMove m2) {
		if (!move_values.containsKey(m1))
			seeCapture(m1);
		if (!move_values.containsKey(m2))
			seeCapture(m2);

		return Integer.compare(move_values.get(m1), move_values.get(m2));
	}

}
