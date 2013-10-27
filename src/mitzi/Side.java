package mitzi;

public enum Side {
	BLACK, WHITE;

	public static Side getOppositeSide(Side side) {
		switch (side) {
		case BLACK:
			return WHITE;
		case WHITE:
			return BLACK;
		default:
			return null;
		}
	}
}
