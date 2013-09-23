package mitzi;

import java.util.Locale;

public final class FigureHelper {

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

	private FigureHelper() {
	};

	public static int figureValue(final int figureType, final int figureColor) {
		return (figureType + figureColor);
	}

	public static int figureColor(final int figureValue) {
		return (figureValue / 10) * 10;
	}

	public static int figureType(final int figureValue) {
		return figureValue % 10;
	}

	public static boolean isWhite(final int figureValue) {
		return figureColor(figureValue) == WHITE;
	}

	public static boolean isBlack(final int figureValue) {
		return figureColor(figureValue) == BLACK;
	}

	public static int oppositeFigureColor(final int figureValue) {
		return Math.abs(figureColor(figureValue) - 10);
	}

	public static String toString(final int figureValue) {
		return toString(figureValue, false);
	}

	public static String toString(final int figureValue,
			final boolean omitPawnLetter) {

		int figure_type = figureType(figureValue);
		int figure_color = figureColor(figureValue);

		if (omitPawnLetter && figure_type == PAWN) {
			return "";
		} else if (figure_color == BLACK) {
			return ALGEBRAIC_NAMES[figure_type - 1].toLowerCase(Locale.ENGLISH);
		} else {
			return ALGEBRAIC_NAMES[figure_type - 1];
		}
	}

}
