package mitzi;

/**
 * An enum containing the two different sides.
 *
 */
public enum Side {
	BLACK, WHITE;

	/**
	 * returns the opposite side of the given side
	 * @param side the given side
	 * @return the opposite side
	 */
	public static Side getOppositeSide(Side side) {
		switch (side) {
		case BLACK:
			return WHITE;
		default:
			return BLACK;
		}
	}

	/**
	 * returns the side sign of the given side
	 * @param side the given side
	 * @return -1 if side == black, 1 otherwise.
	 */
	public static int getSideSign(Side side) {
		switch (side) {
		case BLACK:
			return -1;
		default:
			return +1;
		}
	}
}
