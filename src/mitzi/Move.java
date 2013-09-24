package mitzi;

public class Move implements IMove {

	private int src;
	
	private int dest;
	
	private int promotion;
	
	/**
	 * Move constructor
	 * 
	 * @param src Source
	 * @param dest Destination
	 * @param promotion Promotion (if no, then omit)
	 */
	public Move(int src, int dest, int promotion)
	{
		this.src=src;
		this.dest=dest;
		this.promotion=promotion;
	}
	public Move(int src, int dest){
		this(src, dest, 0);
	}
	
	@Override
	public int getFromSquare() {
		return src;
	}

	@Override
	public int getToSquare() {
		return dest;
	}

	@Override
	public int getPromotion() {
		return promotion;
	}

}
