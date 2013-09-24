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
	 * @return the promotion of the pawn. 0 if no promotion.
	 */
	public int getPromotion();

	public String toString();

}
