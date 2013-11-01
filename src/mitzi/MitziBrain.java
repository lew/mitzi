package mitzi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public class MitziBrain implements IBrain {

	private int POS_INF = +1000000000;
	private int NEG_INF = -1000000000;

	private IBoard board;

	private Variation principal_variation;

	@Override
	public void set(IBoard board) {
		this.board = board;
	}

	/**
	 * 
	 * This Comparator class implements the comparing of two moves. The moves
	 * are saved intern as an ArrayList for a simpler search.
	 * 
	 */
	class MoveComperator implements Comparator<IMove> {

		private double[] board_values;
		private ArrayList<IMove> moves;

		public void compute_values(Set<IMove> moves, IBoard board, int side_sign) {
			this.moves = new ArrayList<IMove>(moves);
			board_values = new double[moves.size()];
			int i = 0;
			for (IMove move : this.moves) {
				board_values[i] = side_sign * evalBoard0(board.doMove(move));
				i++;
			}
		}

		public int compare(IMove m1, IMove m2) {

			int i1 = moves.indexOf(m1);
			int i2 = moves.indexOf(m2);

			return -Double.compare(board_values[i1], board_values[i2]);

		}
	}

	/**
	 * Sorts the moves w.r.t. the value of the next board (base case). If the
	 * depth is 1 (so after one move the base case is reached) no sorting is
	 * done. (preventing double evaluation of board)
	 * 
	 * @param moves
	 *            the set of moves to be sorted
	 * @param board
	 *            the actual board
	 * @param side_sign
	 *            the actual side
	 * @param depth
	 *            the current search depth
	 * @return the sorted set of moves in a TreeSet
	 */
	private ArrayList<IMove> sortMoves(Set<IMove> moves, IBoard board,
			int side_sign, int depth) {

		ArrayList<IMove> ordered_moves;
		ordered_moves = new ArrayList<IMove>(moves);
		if (depth != 1) {
			MoveComperator my_comp = new MoveComperator();
			my_comp.compute_values(moves, board, side_sign);

			Collections.sort(ordered_moves, my_comp);
		}

		return ordered_moves;
	}

	/**
	 * NegaMax with Alpha Beta Pruning
	 * 
	 * @see <a
	 *      href="https://en.wikipedia.org/wiki/Negamax#NegaMax_with_Alpha_Beta_Pruning">NegaMax
	 *      with Alpha Beta Pruning</a>
	 * @param board
	 *            the current board
	 * @param total_depth
	 *            the total depth to search
	 * @param depth
	 *            the remaining depth to search
	 * @param alpha
	 * @param beta
	 * @return returns a Variation tree
	 */
	private Variation evalBoard(IBoard board, int total_depth, int depth,
			int alpha, int beta) {

		// whose move is it?
		Side side = board.getActiveColor();
		int side_sign = Side.getSideSign(side);

		// generate moves
		Set<IMove> moves = board.getPossibleMoves();

		// check for mate and stalemate
		if (moves.isEmpty()) {
			Variation base_variation;
			if (board.isCheckPosition()) {
				base_variation = new Variation(null, NEG_INF * side_sign,
						board.getActiveColor());
			} else {
				base_variation = new Variation(null, 0, board.getActiveColor());
			}
			return base_variation;
		}

		// base case
		if (depth == 0) {
			Variation base_variation = new Variation(null, evalBoard0(board),
					board.getActiveColor());
			return base_variation;
		}

		int best_value = NEG_INF; // this starts always at negative!

		// maybe use variation subtree from previous computation?!
		// is this even allowed in UCI? as if we would care :)
		// Sort the moves:
		ArrayList<IMove> ordered_moves = sortMoves(moves, board, side_sign,
				depth);

		// create new parent Variation
		Variation parent = new Variation(null, NEG_INF,
				Side.getOppositeSide(side));

		// alpha beta search
		for (IMove move : ordered_moves) {
			Variation variation = evalBoard(board.doMove(move), total_depth,
					depth - 1, -beta, -alpha);
			int negaval = variation.getValue() * side_sign;

			// better variation found
			if (negaval >= best_value) {
				boolean truly_better = negaval > best_value;
				// update variation tree
				best_value = variation.getValue() * side_sign;
				parent.update(null, variation.getValue());
				variation.update(move, variation.getValue());
				parent.addSubVariation(variation);

				// output to UCI
				if (depth == total_depth && truly_better) {
					principal_variation = parent.getPrincipalVariation();
					UCIReporter.sendInfoPV(principal_variation, total_depth,
							variation.getValue());
				}
			}

			// alpha beta cutoff
			alpha = Math.max(alpha, negaval);
			if (alpha >= beta)
				break;

		}

		return parent;

	}

	/**
	 * returns the value of a board. Does NOT recognize mate and stalemate!
	 * 
	 * @param board
	 *            the board to be analyzed
	 * @return the value of a board in centipawns
	 */
	private int evalBoard0(IBoard board) {

		// A very very simple implementation
		int value = 0;

		// One way to prevent copy and paste
		int[] fig_value = { 100, 500, 330, 330, 900, 000 };

		// Maybe not the most efficient way (several runs over the board)
		for (Side c : Side.values()) {
			int side_sign = Side.getSideSign(c);
			for (Piece fig : Piece.values()) {
				value += board.getNumberOfPiecesByColorAndType(c, fig)
						* fig_value[fig.ordinal()] * side_sign;
			}

		}

		return value;
	}

	@Override
	public IMove search(int movetime, int maxMoveTime, int searchDepth,
			boolean infinite, Set<IMove> searchMoves) {

		// first of all, ignoring the timings and restriction to certain
		// moves...

		Variation var_tree = evalBoard(board, searchDepth, searchDepth,
				NEG_INF, POS_INF);

		return var_tree.getBestMove();
	}

	@Override
	public IMove stop() {
		// TODO Auto-generated method stub
		return null;
	}

}
