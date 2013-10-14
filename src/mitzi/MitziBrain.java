package mitzi;

import java.util.Set;

public class MitziBrain implements IBrain {

	private IBoard board;

	private IMove next_move;

	@Override
	public void set(IBoard board) {
		this.board = board;
	}

	/**
	 * Basic minimax Algorithm, like in the lecture notes. Furthermore the
	 * function sets the variable next_move.
	 * 
	 * @param board
	 *            the current board
	 * @param depth
	 *            the search depth
	 * @return searches the best move and returns the value
	 */
	private double evalBoard(IBoard board, int depth) {

		// if the base case is reached
		// TODO: We need a function for checking stalemate
		if (depth == 0 || board.isCheckPosition() || board.isMatePosition()) {
			return evalBoard0(board);

		}// if the best move has to be found
		else if (board.getActiveColor() == PieceHelper.WHITE) {
			double max = -10 ^ 6; // some large negative number....
			double val;
			IMove best_move = null;

			Set<IMove> moves = board.getPossibleMoves();
			for (IMove move : moves) {
				val = evalBoard(board.doMove(move), depth - 1);
				if (max < val) {
					max = val;
					best_move = move;
				}
			}
			next_move = best_move;
			return max;

		} // if the worst move has to be found
		else if (board.getActiveColor() == PieceHelper.BLACK) {
			double min = 10 ^ 6; // some large positive number....
			double val;
			IMove worst_move = null;

			Set<IMove> moves = board.getPossibleMoves();
			for (IMove move : moves) {
				val = evalBoard(board.doMove(move), depth - 1);
				if (min > val) {
					min = val;
					worst_move = move;
				}
			}
			next_move = worst_move;
			return min;
		}

		return 0; // cannot happen anyway.
	}

	/**
	 * returns the value of a board.
	 * 
	 * @param board
	 *            the board to be analyzed
	 * @return the value of a board
	 */
	private double evalBoard0(IBoard board) {
		// TODO: the hard part....
		return 0;
	}

	@Override
	public IMove search(int movetime, int maxMoveTime, int searchDepth,
			boolean infinite, Set<IMove> searchMoves) {

		// first of all, ignoring the timings and restriction to certain
		// moves...

		@SuppressWarnings("unused")
		double value = evalBoard(board, searchDepth); // value might be
														// interesting for
														// debugging

		return next_move;
	}

	@Override
	public IMove stop() {
		// TODO Auto-generated method stub
		return null;
	}

}
