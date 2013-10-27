package mitzi;

public enum Side {
	BLACK, WHITE, EMPTY;

	public static Side getOppositeSide(Side side) {
		switch (side) {
		case BLACK:
			return WHITE;
		case WHITE:
			return BLACK;
		default:
			return EMPTY;
		}
	}
}
