package mitzi;

import java.util.Locale;

public final class PieceHelper {

	/**
	 * A String for the algebraic names of the pieces. P... Pawn, R... Rook,
	 * etc.
	 */
	public static final String[] ALGEBRAIC_NAMES = { "P", "R", "N", "B", "Q",
			"K" };

	private PieceHelper() {
	};

	/**
	 * Converts a Piece of a given Side into string. Capital letters are white,
	 * lower case letters are black.
	 * 
	 * @param side
	 *            the gives side
	 * @param piece
	 *            the given piece
	 * @return the string representation of the piece.
	 */
	public static String toString(final Side side, final Piece piece) {
		return toString(side, piece, false);
	}

	/**
	 * Converts a Piece of a given Side into string. Capital letters are white,
	 * lower case letters are black. Additionally, you have the choice to omit
	 * writing a P for pawn.
	 * 
	 * @param side
	 *            the gives side
	 * @param piece
	 *            the given piece
	 * @param omitPawnLetter
	 *            if the pawnletter should be omitted or not.
	 * @return the string representation of the piece.
	 */
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

	/**
	 * converts a given piece into a string, no distinction which side.
	 * 
	 * @param piece
	 *            the given piece
	 * @return the string representation.
	 */
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
