package mitzi;

import java.util.Set;

//import mitzi.UCIReporter.InfoType;

public class MitziBrain implements IBrain {

	private IBoard board;

	private IMove next_move;

	@Override
	public void set(IBoard board) {
		this.board = board;
	}

	/**
	 * NegaMax with Alpha Beta Pruning. Furthermore the function sets the
	 * variable next_move.
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Negamax">Negamax</a>
	 * 
	 * @param board
	 *            the current board
	 * @param depth
	 *            the search depth
	 * @param alpha
	 * @param beta
	 * @param side_sign
	 *            white's turn +1, black's turn -1
	 * @return sets the best move and returns the value
	 */
	private double evalBoard(IBoard board, int depth, double alpha,
			double beta, int side_sign) {
		// TODO: there is still a problem, that the function returns a NULL -
		// move although some are possible... especially for check positions

		// if the base case is reached
		if (depth == 0 || board.isStaleMatePosition() || board.isMatePosition()) {
			return evalBoard0(board) * side_sign;

		}

		double best_value = -10 ^ 6;
		double val = 0;
		IMove poss_next_move = null;
		Set<IMove> moves = board.getPossibleMoves();
		// TODO: order moves for best alpha-beta effect
		for (IMove move : moves) {
			val = -evalBoard(board.doMove(move), depth - 1, -beta, -alpha,
					-side_sign);
			if (val >= best_value) {
				best_value = val;
				poss_next_move = move; // oh gott, denkfehler :D FIXME
			}
			alpha = Math.max(alpha, val);
			//System.out.println("Depth: " + depth + " Next_move: " + move	+ " Value: " + best_value);
			if (alpha >= beta) {
				break;
			}
		}
		next_move = poss_next_move;

		return best_value;

	}

	/**
	 * returns the value of a board.
	 * 
	 * @param board
	 *            the board to be analyzed
	 * @return the value of a board
	 */
	private double evalBoard0(IBoard board) {

		// check for checkmate
		if (board.isMatePosition()) {
			if (board.getActiveColor() == Side.WHITE)
				return -10 ^ 6;
			else
				return 10 ^ 6;
		}

		// A very very simple implementation
		double value = 0;

		// One way to prevent copy and paste
		double[] fig_value = { 1, 5, 3.3, 3.3, 9 };

		// Maybe not the most efficient way (several runs over the board)
		for (Side c : Side.values()) {
			for (Piece fig : Piece.values()) {
				if (fig != Piece.KING) {
					if (c == Side.WHITE)
						value += board.getNumberOfPiecesByColorAndType(c, fig)
								* fig_value[fig.ordinal()];
					else if (c == Side.BLACK)
						value -= board.getNumberOfPiecesByColorAndType(c, fig)
								* fig_value[fig.ordinal()];
				}
			}

		}

		return value;
	}

	@Override
	public IMove search(int movetime, int maxMoveTime, int searchDepth,
			boolean infinite, Set<IMove> searchMoves) {

		// first of all, ignoring the timings and restriction to certain
		// moves...

		int side_color = -1;
		if (board.getActiveColor() == Side.WHITE) {
			side_color = 1;
		}
		@SuppressWarnings("unused")
		double value = evalBoard(board, searchDepth, -10 ^ 6, 10 ^ 6,
				side_color); // value might be interesting for debugging
		System.out.println("");
		return next_move;
	}

	@Override
	public IMove stop() {
		// TODO Auto-generated method stub
		return null;
	}

}
