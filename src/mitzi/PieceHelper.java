package mitzi;

import java.util.Locale;

public final class PieceHelper {

	public static final int WHITE = 0;
	public static final int BLACK = 10;

	public static final int PAWN = 1;
	public static final int ROOK = 2;
	public static final int KNIGHT = 3;
	public static final int BISHOP = 4;
	public static final int QUEEN = 5;
	public static final int KING = 6;

	public static final String[] ALGEBRAIC_NAMES = { "P", "R", "N", "B", "Q",
			"K" };

	private PieceHelper() {
	};

	public static int pieceValue(final int pieceType, final int pieceColor) {
		return (pieceType + pieceColor);
	}

	public static int pieceColor(final int pieceValue) {
		return (pieceValue / 10) * 10;
	}

	public static int pieceType(final int pieceValue) {
		return pieceValue % 10;
	}

	public static boolean isWhite(final int pieceValue) {
		return pieceColor(pieceValue) == WHITE;
	}

	public static boolean isBlack(final int pieceValue) {
		return pieceColor(pieceValue) == BLACK;
	}

	public static int pieceOppositeColor(final int pieceValue) {
		return Math.abs(pieceColor(pieceValue) - 10);
	}

	public static String toString(final int pieceValue) {
		return toString(pieceValue, false);
	}

	public static String toString(final int pieceValue,
			final boolean omitPawnLetter) {

		int piece_type = pieceType(pieceValue);
		int piece_color = pieceColor(pieceValue);

		if (omitPawnLetter && piece_type == PAWN) {
			return "";
		} else if (piece_color == BLACK) {
			return ALGEBRAIC_NAMES[piece_type - 1].toLowerCase(Locale.ENGLISH);
		} else {
			return ALGEBRAIC_NAMES[piece_type - 1];
		}
	}

}
