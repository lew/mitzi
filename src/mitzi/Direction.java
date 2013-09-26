package mitzi;

public enum Direction {
	EAST(10), NORTHEAST(11), NORTH(1), NORTHWEST(-9), WEST(-10), SOUTHWEST(-11), SOUTH(
			-1), SOUTHEAST(9);

	public final int value;

	Direction(int value) {
		this.value = value;
	}

}
