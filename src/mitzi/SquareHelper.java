package mitzi;

import java.util.ArrayList;
import java.util.List;

/**
 * In brief, each square of the chessboard has a two-digit designation. The
 * first digit is the number of the column, from left to right from White's
 * point of view. The second digit is the row from the edge near White to the
 * other edge.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/ICCF_numeric_notation">ICCF
 *      numeric notation</a>
 */
public final class SquareHelper {

	/**
	 * the letters of the columns of the chessboard
	 */
	private static final String[] letters = { "a", "b", "c", "d", "e", "f",
			"g", "h" };

	private SquareHelper() {
	};

	private static ArrayList<ArrayList<List<Integer>>> squares_direction = new ArrayList<ArrayList<List<Integer>>>();
	private static ArrayList<List<Integer>> squares_direction_knight = new ArrayList<List<Integer>>();

	static {
		for(int i=0;i<89;i++){
			squares_direction.add(null);
			squares_direction_knight.add(null);
		}
		for (int i = 1; i < 9; i++)
			for (int j = 1; j < 9; j++) {
				int source_square = getSquare(i, j);
				ArrayList<List<Integer>> dir_list = new ArrayList<List<Integer>>();
				for(int k=0;k<9;k++)
					dir_list.add(null);
				ArrayList<Integer> dir_list_knight = new ArrayList<Integer>();

				// compute squares for pieces except the knight
				for (Direction dir : Direction.values()) {
					ArrayList<Integer> square_list = new ArrayList<Integer>();

					int square = source_square + dir.offset;
					while (isValidSquare(square)) {
						square_list.add(square);
						square += dir.offset;
					}
					dir_list.set(dir.ordinal(), square_list);
				}

				// squares for Knight
				for (Direction dir : Direction.values()) {

					int square = source_square + dir.knight_offset;
					if (isValidSquare(square)) {
						dir_list_knight.add(square);
					}
				}
				squares_direction.set(source_square, dir_list);
				squares_direction_knight.set(source_square, dir_list_knight);
			}

	}

	/**
	 * Returns the integer value of the square's column. Starting with 1 at
	 * column a and ending with 8 at column h.
	 * 
	 * @return the integer value of the square's column.
	 */
	public static int getColumn(int square) {
		return square / 10;
	}

	/**
	 * Returns the integer value of the square's row. Where row 1 is row 1 and
	 * so forth, obviously.
	 * 
	 * @return the integer value of the square's row.
	 */
	public static int getRow(int square) {
		return square % 10;
	}

	/**
	 * Returns the square-number for a given row and column. Row 1 and column 2
	 * results in 12.
	 * 
	 * @return the integer value of the square
	 */
	public static int getSquare(int row, int column) {
		return 10 * column + row;
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
		return (square / 10 + square % 10) % 2 != 0;
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

	
	public static ArrayList<List<Integer>> getSquaresAllDirections(int square){
		return squares_direction.get(square);
	}
	
	public static List<Integer> getAllSquaresInDirection(ArrayList<List<Integer>> squares, Direction direction) {
		return squares.get(direction.ordinal());
	}
	/**
	 * Gives an ordered List of squares going in a straight line from the source
	 * square.
	 * 
	 * @param source_square
	 *            the square from where to start
	 * @param direction
	 *            one of the values SquareHelper.EAST, SquareHelper.NORTHEAST,
	 *            SquareHelper.NORTH, …
	 * @return the list of squares ordered from the source_square to the boards
	 *         edge
	 */
	public static List<Integer> getAllSquaresInDirection(int source_square,
			Direction direction) {

		return squares_direction.get(source_square).get(direction.ordinal());
	}

	/**
	 * Gives a List of squares reached by a knight from the source square (in no
	 * specific order).
	 * 
	 * @param source_square
	 *            the square from where to start
	 * @return the list of squares a knight can reach
	 */
	public static List<Integer> getAllSquaresByKnightStep(int source_square) {

		return squares_direction_knight.get(source_square);
	}

	/**
	 * Checks if the integer value of the square is inside the board's borders.
	 * 
	 * @param square
	 *            the square to be checked
	 * @return true if the square is on the board
	 */
	public static boolean isValidSquare(int square) {
		int row = getRow(square);
		int column = getColumn(square);
		return (row >= 1 && row <= 8 && column >= 1 && column <= 8);
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
		return letters[getColumn(square) - 1]
				+ Integer.toString(getRow(square));
	}

	/**
	 * converts the string representation of a square into a the ICCF notation.
	 * 
	 * @param notation
	 *            the given square in string notation
	 * @return the square in integer representation.
	 */
	public static int fromString(String notation) {
		int i = 0;
		while (letters[i].charAt(0) != notation.charAt(0)) {
			i++;
		}
		return (i + 1) * 10 + Character.getNumericValue(notation.charAt(1));
	}

	/**
	 * returns the number for the i_th row seen from a given side. i.e. the last
	 * row for black is 1, the 3rd row for white is 3, the 3rd row for black is
	 * 6;
	 * 
	 * @param side
	 *            the given side
	 * @param i_th
	 *            the i_th row, where the (global) row number is wanted.
	 * @return the (global) row number.
	 */
	public static int getRowForSide(Side side, int i_th) {
		if (side == Side.BLACK)
			return 9 - i_th;
		else
			return i_th;
	}

	
	
}
