package mitzi;

import java.util.Locale;
import java.util.Set;

public final class Move implements IMove {

	private final int src;

	private final int dest;

	private final Piece promotion;

	/**
	 * Move constructor
	 * 
	 * @param src
	 *            Source
	 * @param dest
	 *            Destination
	 * @param promotion
	 *            Promotion (if no, then omit)
	 */
	public Move(int src, int dest, Piece promotion) {
		this.src = src;
		this.dest = dest;
		this.promotion = promotion;
	}

	public Move(int src, int dest) {
		this(src, dest, null);
	}

	public Move(String notation) {
		String[] squares = new String[2];

		squares[0] = notation.substring(0, 2);
		squares[1] = notation.substring(2, 4);

		src = SquareHelper.fromString(squares[0]);
		dest = SquareHelper.fromString(squares[1]);

		if (notation.length() > 4) {
			String promo_string = notation.substring(4, 5).toLowerCase(
					Locale.ENGLISH);
			if (promo_string.equals("q")) {
				promotion = Piece.QUEEN;
			} else if (promo_string.equals("r")) {
				promotion = Piece.ROOK;
			} else if (promo_string.equals("n")) {
				promotion = Piece.KNIGHT;
			} else if (promo_string.equals("b")) {
				promotion = Piece.BISHOP;
			} else {
				promotion = null;
			}
		} else {
			promotion = null;
		}
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
	public Piece getPromotion() {
		return promotion;
	}

	@Override
	public String toString() {
		String promote_to;
		if (getPromotion() != null) {
			promote_to = PieceHelper.toString(Side.WHITE, getPromotion());
		} else {
			promote_to = "";
		}
		return SquareHelper.toString(getFromSquare())
				+ SquareHelper.toString(getToSquare()) + promote_to;
	}

	/**
	 * 
	 * Checks if a move is in a given List of moves
	 * 
	 * @param moves
	 *            List of moves
	 * @param move
	 *            the move to be searched
	 * @return true if move is in moves, else false
	 */
	public static boolean MovesListIncludesMove(Set<Move> moves, Move move) {
		return moves.contains(move);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dest;
		result = prime * result
				+ ((promotion == null) ? 0 : promotion.hashCode());
		result = prime * result + src;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Move other = (Move) obj;
		if (dest != other.dest || promotion != other.promotion
				|| src != other.src) {
			return false;
		}
		return true;
	}
}
