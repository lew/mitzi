package mitzi;

import java.util.EnumSet;

public enum Direction {
	EAST(10, 21), NORTHEAST(11, 12), NORTH(1, -8), NORTHWEST(-9, -19), WEST(
			-10, -21), SOUTHWEST(-11, -12), SOUTH(-1, 8), SOUTHEAST(9, 19);

	/**
	 * Add to a square value to go one step in the specified direction.
	 * 
	 * White is South, Black is North.
	 */
	public final int offset;

	/**
	 * Add to a square value to go one knight-step in the specified direction.
	 * 
	 * One up and two right is East. Two up one right is Northeast. Basically,
	 * the orientation is shifted a bit counterclockwise.
	 */
	public final int knight_offset;

	Direction(int offset, int knight_offset) {
		this.offset = offset;
		this.knight_offset = knight_offset;
	}

	/**
	 * Returns the direction in which a pawn of the specified color can move
	 * (without capturing).
	 * 
	 * @param color
	 *            the color of the piece
	 * @return NORTH for white and SOUTH for black
	 */
	public static Direction pawnDirection(Side color) {
		if (color == Side.WHITE) {
			return NORTH;
		} else {
			return SOUTH;
		}
	}

	/**
	 * Returns a set of directions in which a pawn of the specified color can
	 * capture other pieces.
	 * 
	 * @param color
	 *            the color of the piece
	 * @return the set of directions allowed
	 */
	public static EnumSet<Direction> pawnCapturingDirections(Side color) {
		if (color == Side.WHITE) {
			return EnumSet.of(NORTHEAST, NORTHWEST);
		} else {
			return EnumSet.of(SOUTHEAST, SOUTHWEST);
		}
	}

}
