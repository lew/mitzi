package mitzi;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.LinkedList;

public class Position {
	private final Side[] side_board;

	private final Piece[] piece_board;

	private final Side to_move;

	// squares c1, g1, c8 and g8 in ICCF numeric notation
	// do not change the squares' order or bad things will happen!
	// set to -1 if castling not allowed
	private final int[] castling;

	private final int en_passant_target;

	public AnalysisResult analysis_result;
	
	public SoftReference<LinkedList<IMove>> possible_moves;

	public Position(Side[] side_board, Piece[] piece_board, Side to_move,
			int[] castling, int en_passant_target) {
		this.side_board = side_board;
		this.piece_board = piece_board;
		this.to_move = to_move;
		this.castling = castling;
		this.en_passant_target = en_passant_target;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(castling);
		result = prime * result + en_passant_target;
		result = prime * result + Arrays.hashCode(piece_board);
		result = prime * result + Arrays.hashCode(side_board);
		result = prime * result + ((to_move == null) ? 0 : to_move.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (!Arrays.equals(castling, other.castling))
			return false;
		if (en_passant_target != other.en_passant_target)
			return false;
		if (!Arrays.equals(piece_board, other.piece_board))
			return false;
		if (!Arrays.equals(side_board, other.side_board))
			return false;
		if (to_move != other.to_move)
			return false;
		return true;
	}

	/**
	 * <p>
	 * The <code>isPossiblyReachableFrom</code> method implements a preorder
	 * relation on non-null <code>Position</code> references:
	 * 
	 * <ul>
	 * <li>It is reflexive: for any non-null reference value <code>x</code>,
	 * <code>x.isPossiblyReachableFrom(x)</code> returns <code>true</code>.
	 * <li>It is transitive: for any non-null reference values <code>x</code>,
	 * <code>y</code>, and <code>z</code>, if
	 * <code>x.isPossiblyReachableFrom(y)</code> returns <code>true</code> and
	 * <code>y.isPossiblyReachableFrom(z)</code> returns <code>true</code>, then
	 * <code>x.isPossiblyReachableFrom(z)</code> returns <code>true</code>.
	 * </ul>
	 * 
	 * <p>
	 * Furthermore, for any non-null reference values <code>x</code>, and
	 * <code>y</code>, <code>x.isPossiblyReachableFrom(y)</code> returns the
	 * same value on every call.
	 * 
	 * @param other
	 * @return If <code>false</code>, then the specified <code>Position</code>
	 *         cannot be reached from this <code>Position</code>. If
	 *         <code>true</code>, a sequence of moves could (but need not) exist
	 *         from the specified <code>Position</code> to this
	 *         <code>Position</code>.
	 * @throws NullPointerException
	 *             if the specified <code>Position</code> is null
	 */
	public boolean isPossiblyReachableFrom(Position other) {
		if (other == null)
			throw new NullPointerException();

		// check castling rights
		for (int i = 0; i < 4; i++) {
			if (other.castling[i] < this.castling[i])
				return false;
		}

		// TODO: implement a pawn check

		return true;
	}
}
