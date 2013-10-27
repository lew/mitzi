package mitzi;

import java.util.Locale;

public final class PieceHelper {

	public static final String[] ALGEBRAIC_NAMES = { "P", "R", "N", "B", "Q",
			"K" };

	private PieceHelper() {
	};
	
	

	public static String toString(final Side side, final Piece piece) {
		return toString(side, piece, false);
	}

	public static String toString(final Side side, final Piece piece,
			final boolean omitPawnLetter) {

		if (omitPawnLetter && piece == Piece.PAWN) {
			return "";
		} else if (side == Side.BLACK) {
			return pieceToString(piece).toLowerCase(Locale.ENGLISH);
		} else {
			return pieceToString(piece);
		}
	}

	private static String pieceToString(final Piece piece) {
		switch (piece) {
		case PAWN:
			return "P";
		case ROOK:
			return "R";
		case KNIGHT:
			return "N";
		case BISHOP:
			return "B";
		case QUEEN:
			return "Q";
		default:
			return "K";
		}

	}

}
