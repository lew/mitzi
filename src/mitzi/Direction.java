package mitzi;

public enum Direction {
	EAST(10, 21), NORTHEAST(11, 12), NORTH(1, -8), NORTHWEST(-9, -19), WEST(
			-10, -21), SOUTHWEST(-11, -12), SOUTH(-1, 8), SOUTHEAST(9, 19);

	public final int offset;
	public final int knight_offset;

	Direction(int offset, int knight_offset) {
		this.offset = offset;
		this.knight_offset = knight_offset;
	}

}
