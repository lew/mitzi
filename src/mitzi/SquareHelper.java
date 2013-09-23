package mitzi;

public class SquareHelper {

	private static class Holder {
		private static final SquareHelper INSTANCE = new SquareHelper();
	}

	private SquareHelper() {
	};

	public static SquareHelper getInstance() {
		return Holder.INSTANCE;
	}

	public static int getColumn(int square) {
		return square % 8;
	}

	public static int getRow(int square) {
		return square / 8;
	}

	/**
	 * Check if the square is white on a traditional chess board.
	 * 
	 * @param square
	 *            the integer code of the square
	 * 
	 * @return true if the square is white and false otherwise
	 */
	public static boolean isWhite(int square) {
		return (square % 2) == 1;
	}

	/**
	 * Check if the square is black on a traditional chess board.
	 * 
	 * @param square
	 *            the integer code of the square
	 * 
	 * @return true if the square is black and false otherwise
	 */
	public static boolean isBlack(int square) {
		return !isWhite(square);
	}

	/**
	 * Returns a string representation of the square in algebraic notation.
	 * 
	 * Each square is traditionally identified by a unique coordinate pair
	 * consisting of a letter and a number. The vertical columns from White's
	 * left (the queenside) to his right (the kingside) are labeled a through h.
	 * The horizontal rows are numbered 1 to 8 starting from White's side of the
	 * board. Thus, each square has a unique identification of a letter followed
	 * by a number. For example, the white king starts the game on square e1,
	 * while the black knight on b8 can move to open squares a6 or c6.
	 * 
	 * @return a string representation of the square in algebraic notation.
	 */
	public static String toString(int square) {
		String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h" };
		return letters[getColumn(square)] + Integer.toString(getRow(square) + 1);
	}

}
