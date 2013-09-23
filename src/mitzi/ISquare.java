package mitzi;

public interface ISquare {

	/**
	 * Returns the integer value of the square's row. Starting with 0 at row 1
	 * and ending with 7 at row 8.
	 * 
	 * @return the integer value of the square's row.
	 */
	public int getRow();

	/**
	 * Returns the integer value of the square's column. Starting with 0 at
	 * column a and ending with 7 at column h.
	 * 
	 * @return the integer value of the square's column.
	 */
	public int getColumn();

	/**
	 * Check if the square is white on a traditional chess board.
	 * 
	 * @return true if the square is white and false otherwise
	 */
	public boolean isWhite();

	/**
	 * Check if the square is black on a traditional chess board.
	 * 
	 * @return true if the square is black and false otherwise
	 */
	public boolean isBlack();

}
