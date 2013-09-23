package mitzi;

public final class FigureHelper {

	public static final int WHITE = 0;
	public static final int BLACK = 10;

	public static final int PAWN = 1;
	public static final int ROOK = 2;
	public static final int KNIGHT = 3;
	public static final int BISHOP = 4;
	public static final int QUEEN = 5;
	public static final int KING = 6;

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

}
