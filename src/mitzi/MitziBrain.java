package mitzi;

import java.util.Set;

//import mitzi.UCIReporter.InfoType;

public class MitziBrain implements IBrain {

	private IBoard board;

	private IMove best_move;

	@Override
	public void set(IBoard board) {
		this.board = board;
	}

	/**
	 * NegaMax with Alpha Beta Pruning. Furthermore the function sets the
	 * variable best_move.
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Negamax">Negamax</a>
	 * @param board
	 *            the current board
	 * @param depth
	 *            the search depth
	 * @param alpha
	 * @param beta
	 * @param side_sign
	 *            white's turn +1, black's turn -1
	 * @return sets the best move and returns the value in centipawns
	 */
	private int evalBoard(IBoard board, int total_depth, int depth, int alpha,
			int beta, int side_sign) {

		// generate moves
		Set<IMove> moves = board.getPossibleMoves();

		// check for mate and stalemate
		if (moves.isEmpty()) {
			if (board.isCheckPosition()) {
				if (board.getActiveColor() == Side.WHITE) {
					return Integer.MIN_VALUE * side_sign;
				} else {
					return Integer.MAX_VALUE * side_sign;
				}
			} else {
				return 0;
			}
		}

		// base case
		if (depth == 0) {
			return side_sign * evalBoard0(board);
		}

		int best_value = Integer.MIN_VALUE;
		// TODO: order moves for better alpha beta effect

		// alpha beta search
		for (IMove move : moves) {
			int val = -evalBoard(board.doMove(move), total_depth, depth - 1,
					-beta, -alpha, -side_sign);
			if (val >= best_value) {
				best_value = val;
				if (total_depth == depth) {
					best_move = move;
				}
			}
			alpha = Math.max(alpha, val);
			if (alpha >= beta)
				break;
		}

		return best_value;

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
		int[] fig_value = { 100, 500, 330, 330, 900 };

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

		int side_sign = -1;
		if (board.getActiveColor() == Side.WHITE) {
			side_sign = 1;
		}

		int value = side_sign
				* evalBoard(board, searchDepth, searchDepth, Integer.MIN_VALUE,
						Integer.MAX_VALUE, side_sign);

		return best_move;
	}

	@Override
	public IMove stop() {
		// TODO Auto-generated method stub
		return null;
	}

}
