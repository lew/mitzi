package mitzi;

public interface IMove {

	/**
	 * 
	 * @return the source of the move
	 */
	public int getFromSquare();

	/**
	 * 
	 * @return the destination of the move
	 */
	public int getToSquare();

	/**
	 * 
	 * @return the promotion of the pawn. EMPTY if no promotion.
	 */
	public Piece getPromotion();
	
	public boolean equals(IMove move);

	public String toString();

}
