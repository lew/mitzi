package mitzi;

public enum Side {
	BLACK, WHITE;

	public static Side getOppositeSide(Side side) {
		switch (side) {
		case BLACK:
			return WHITE;
		default:
			return BLACK;
		}
	}

	public static int getSideSign(Side side) {
		switch (side) {
		case BLACK:
			return -1;
		default:
			return +1;
		}
	}
}
