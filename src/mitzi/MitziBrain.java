package mitzi;

import java.util.Set;

public class MitziBrain implements IBrain {

	private int POS_INF = +1000000000;
	private int NEG_INF = -1000000000;

	private IBoard board;

	@Override
	public void set(IBoard board) {
		this.board = board;
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

		// TODO: order moves for better alpha beta effect
		// maybe use variation subtree from previous computation?!
		// is this even allowed in UCI? as if we would care :)

		// create new parent Variation
		Variation parent = new Variation(null, NEG_INF,
				Side.getOppositeSide(side));

		// alpha beta search
		for (IMove move : moves) {
			Variation variation = evalBoard(board.doMove(move), total_depth,
					depth - 1, -beta, -alpha);
			int negaval = variation.getValue() * side_sign;

			// better variation found
			if (negaval >= best_value) {
				best_value = variation.getValue() * side_sign;
				parent.update(null, variation.getValue());
				variation.update(move, variation.getValue());
				parent.addSubVariation(variation);
			}

			// alpha beta cutoff
			/*alpha = Math.max(alpha, negaval);
			if (alpha >= beta)
				break;*/

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
