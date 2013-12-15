package mitzi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CaptureComparator implements Comparator<IMove> {

	/**
	 * saves the actual board, where the moves should be compared
	 */
	private IPosition board;

	/**
	 * map, which maps a move to its value. Initial size set to 35 to prevent
	 */
	private Map<IMove, Integer> move_values = new HashMap<IMove, Integer>(35, 1);

	private static final int[] piece_values = { 100, 500, 325, 325, 975, 000 };

	private ArrayList<List<IMove>> attackers = new ArrayList<List<IMove>>(2);

	public CaptureComparator(IPosition board) {
		this.board = board.returnCopy();
	}

	private int seeCapture(IMove m) {
		int value = 0;
		int square = m.getToSquare();
		Side side = board.getActiveColor();

		Piece piece = board.getPieceFromBoard(square);
		if (piece == null
				&& SquareHelper.getRow(square) != SquareHelper.getRowForSide(
						board.getActiveColor(), 8))
			piece = Piece.PAWN; // en_passant
		else if (piece == null)
			piece = Piece.KING; // promotion, no capture - value of King is 0

		if (attackers.size() < 2) {
			attackers.add(null);
			attackers.add(null);
		}

		List<IMove> captures = board.getPotentialAttackersTo(square);	
		captures.remove(m);
		attackers.set(side.ordinal(), captures);

		board.doMove(m);

		value = (piece_values[piece.ordinal()] - see(square, m));

		board.undoMove(m);

		attackers.clear();
		move_values.put(m, value);
		return value;
	}

	private int see(int square, IMove m) {

		int value = 0;
		Side side = board.getActiveColor();
		update_attackers(square, m);
		IMove move = get_smallest_attacker(side);
		/* skip if the square isn't attacked anymore by this side */
		if (move != null) {
			Piece piece = board.getPieceFromBoard(move.getToSquare());
			board.doMove(move);

			// Do not consider captures if they lose material, therefore max
			// zero
			value = Math.max(0,
					piece_values[piece.ordinal()] - see(square, move));

			board.undoMove(move);
		}
		return value;
	}

	@Override
	public int compare(IMove m1, IMove m2) {
		if (!move_values.containsKey(m1))
			seeCapture(m1);
		if (!move_values.containsKey(m2))
			seeCapture(m2);

		return Integer.compare(move_values.get(m1), move_values.get(m2));
	}

	public IMove get_smallest_attacker(Side side) {

		Piece p;
		int val = 1001;
		IMove best_move = null;
		List<IMove> captures = attackers.get(side.ordinal());

		piece_values[Piece.KING.ordinal()] = 1000;
		for (IMove move : captures) {
			p = board.getPieceFromBoard(move.getFromSquare());
			if (p == null)
				System.out.println("STOP");
			if (piece_values[p.ordinal()] < val) {
				best_move = move;
				val = piece_values[p.ordinal()];

				if (val == 100) {
					captures.remove(move);
					return best_move;
				}
			}

		}
		piece_values[Piece.KING.ordinal()] = 0;
		captures.remove(best_move);

		return best_move;
	}

	private void update_attackers(int square, IMove last_move) {

		Side just_computed_side = null;
		if(attackers.get(board.getActiveColor().ordinal())==null){
			List<IMove> captures2 = board.getPotentialAttackersTo(square);
			attackers.set(board.getActiveColor().ordinal(), captures2);
			just_computed_side = board.getActiveColor();
		}
		
		if(last_move.getPromotion() != null){
			Iterator<IMove> it = attackers.get(Side.getOppositeSide(board.getActiveColor()).ordinal()).iterator();
			while(it.hasNext())
				if(it.next().getPromotion()!=null)
					it.remove();
		}
					
		
		Piece p = board.getPieceFromBoard(square);

		// find attacking direction
		if (p == Piece.KNIGHT)
			return;
		if (p == Piece.KING)
			return; // should be changed maybe...

		int squ_from = last_move.getFromSquare();

		int diff = squ_from - square;

		Direction direction = null;
		EnumSet<Direction> dir_pos = EnumSet.of(Direction.NORTHEAST,
				Direction.EAST, Direction.SOUTHEAST);
		EnumSet<Direction> dir_neg = EnumSet.of(Direction.SOUTHWEST,
				Direction.WEST, Direction.NORTHWEST);

		if (diff > 0) {
			for (Direction dir : dir_pos) {
				if (diff % dir.offset == 0) {
					direction = dir;
				}
			}
			if (diff < 8)
				direction = Direction.NORTH;
		} else {
			for (Direction dir : dir_neg) {
				if (diff % dir.offset == 0) {
					direction = dir;
				}
			}
			if (diff > -8)
				direction = Direction.SOUTH;
		}
		int new_square = squ_from + direction.offset;
		IMove move;
		EnumSet<Direction> dir_lines = EnumSet.of(Direction.EAST,
				Direction.SOUTH, Direction.NORTH, Direction.WEST);
		EnumSet<Direction> dir_diag = EnumSet.of(Direction.SOUTHEAST,
				Direction.SOUTHWEST, Direction.NORTHEAST, Direction.NORTHWEST);
		while (SquareHelper.isValidSquare(new_square)) {
			Side s = board.getSideFromBoard(new_square);
			if (s != null && s != just_computed_side) {
				p = board.getPieceFromBoard(new_square);
				if (p == Piece.QUEEN) {
					move = new Move(new_square, square);
					if (!board.isCheckAfterMove(move))
						attackers.get(s.ordinal()).add(move);
				} else if (p == Piece.BISHOP) {
					if (dir_diag.contains(direction)) {
						move = new Move(new_square, square);
						if (!board.isCheckAfterMove(move))
							attackers.get(s.ordinal()).add(move);
					}
				} else if (p == Piece.ROOK) {
					if (dir_lines.contains(direction)) {
						move = new Move(new_square, square);
						if (!board.isCheckAfterMove(move))
							attackers.get(s.ordinal()).add(move);
					}
				}
				return;
			}

			new_square += direction.offset;
		}
	}
}
