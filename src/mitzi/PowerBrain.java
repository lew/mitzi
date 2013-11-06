package mitzi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static mitzi.MateScores.*;
import mitzi.UCIReporter.InfoType;

public class PowerBrain implements IBrain {

	private long eval_counter;

	private IBoardAnalyzer board_analyzer = new BasicBoardAnalyzer();

	private HashManager hash_manager;

	@Override
	public void set(IBoard board) {
		Position new_position = new Position(board);

		if (hash_manager == null) {
			hash_manager = new HashManager(new_position);
		} else {
			PositionTreeNode new_root = hash_manager.lookup(new_position);
			hash_manager.rebase(new_root);
		}

		this.eval_counter = 0;
	}

	/**
	 * Sends updates about evaluation status to UCI GUI.
	 * 
	 */
	class UCIUpdater extends TimerTask {

		private long old_mtime;
		private long old_eval_counter;

		@Override
		public void run() {
			long mtime = System.currentTimeMillis();
			long eval_span = eval_counter - old_eval_counter;

			if (old_mtime != 0) {
				long time_span = mtime - old_mtime;
				UCIReporter.sendInfoNum(InfoType.NPS, eval_span * 1000
						/ (time_span + 1));
			}

			old_mtime = mtime;
			old_eval_counter += eval_span;

		}
	}

	/**
	 * NegaMax with Alpha Beta Pruning and Transposition Tables
	 * 
	 * @see <a http://en.wikipedia.org/wiki/Negamax#
	 *      NegaMax_with_Alpha_Beta_Pruning_and_Transposition_Tables">NegaMax
	 *      with Alpha Beta Pruning and Transposition Tables</a>
	 * @param ptn
	 *            the PositionTreeNode to analyze the current board
	 * @param total_depth
	 *            the total depth to search
	 * @param depth
	 *            the remaining depth to search
	 * @param alpha
	 * @param beta
	 */
	private void evalBoard(PositionTreeNode ptn, int total_depth, int depth,
			int alpha, int beta) {

		Position position = ptn.getPosition();
		int old_alpha = alpha;

		// whose move is it?
		Side side = position.board.getActiveColor();
		int side_sign = Side.getSideSign(side);

		// Simple Lookup:
		if (position.getAnalysisResult() != null
				&& position.getAnalysisResult().getPlysToEval0() >= depth) {
			// usable cache score found, compute nothing
			if (position.getAnalysisResult().getFlag() == Flag.EXACT)
				return;
			else if (position.getAnalysisResult().getFlag() == Flag.LOWERBOUND)
				alpha = Math.max(alpha, position.getAnalysisResult().score);
			else if (position.getAnalysisResult().getFlag() == Flag.UPPERBOUND)
				beta = Math.min(beta, position.getAnalysisResult().score);
			if (alpha > beta)
				return;
		}

		// generate moves
		Set<IMove> moves = position.board.getPossibleMoves();

		// check for mate and stalemate (the side should alternate)
		if (moves.isEmpty()) {
			AnalysisResult analysis_result;
			if (position.board.isCheckPosition()) {
				analysis_result = new AnalysisResult(NEG_INF * side_sign,
						false, false, 0, 0, Flag.EXACT);
			} else {
				analysis_result = new AnalysisResult(0, true, false, 0, 0,
						Flag.EXACT);
			}
			position.setAnalysisResult(analysis_result);
			ptn.setAllowsChildren(false);
			eval_counter++;
			return;
		}

		// base case (the side should alternate)
		if (depth == 0) {
			AnalysisResult analysis_result = board_analyzer
					.eval0(position.board);
			analysis_result.setPlysToEval0(0);
			analysis_result.setPlysToSelDepth(0);
			position.setAnalysisResult(analysis_result);
			eval_counter++;
			return;
		}

		int best_value = NEG_INF; // this starts always at negative!

		// Sort the moves:
		BasicMoveComparator move_comparator = new BasicMoveComparator(
				position.board);
		// TODO: order moves using PTN
		List<IMove> ordered_moves = new ArrayList<IMove>(moves.size());
		
		// add remaining moves in basic heuristic order ArrayList<IMove> (moves get changed!)
		moves.removeAll(ordered_moves);
		List<IMove> remaining_moves = new ArrayList<IMove>(moves);

		Collections.sort(remaining_moves,
				Collections.reverseOrder(move_comparator));
		ordered_moves.addAll(remaining_moves);

		int i = 0;
		// alpha beta search
		for (IMove move : ordered_moves) {

			if (depth == total_depth && total_depth >= 6) {
				// output currently searched move to UCI
				UCIReporter.sendInfoCurrMove(move, i + 1);
			}

			IBoard child_board = position.board.doMove(move);
			Position child_position = new Position(child_board);
			PositionTreeNode child = hash_manager.lookup(child_position);

			evalBoard(child, total_depth, depth - 1, -beta, -alpha);

			int negaval = child.getPosition().getAnalysisResult().score
					* side_sign;

			// better variation found
			if (negaval >= best_value) {
				boolean truly_better = negaval > best_value;
				best_value = negaval;

				// build position tree
				if (child.getLevel() >= total_depth - depth) {
					child.setParentMove(move);
					ptn.add(child);
				}

				// update position tree
				AnalysisResult analysis_result = new AnalysisResult(child
						.getPosition().getAnalysisResult().score, child
						.getPosition().getAnalysisResult().is_stalemate, child
						.getPosition().getAnalysisResult().needs_deeper,
						child.getPosition().getAnalysisResult()
								.getPlysToEval0() + 1, child.getPosition()
								.getAnalysisResult().getPlysToSelDepth() + 1,
						null);
				position.setBest_move(move);
				position.setAnalysisResult(analysis_result);

				// output to UCI
				if (depth == total_depth && truly_better) {
					UCIReporter.sendInfoPV(ptn, total_depth,
							hash_manager.getRootNode().getPosition()
									.getAnalysisResult().score, hash_manager
									.getRootNode().getPosition().board
									.getActiveColor());
				}
			}

			// alpha beta cutoff
			alpha = Math.max(alpha, negaval);
			if (alpha >= beta)
				break;

			i++;
		}

		if (best_value <= old_alpha)
			position.getAnalysisResult().setFlag(Flag.UPPERBOUND);
		else if (best_value >= beta)
			position.getAnalysisResult().setFlag(Flag.LOWERBOUND);
		else
			position.getAnalysisResult().setFlag(Flag.EXACT);
	}

	@Override
	public IMove search(int movetime, int maxMoveTime, int searchDepth,
			boolean infinite, Set<IMove> searchMoves) {

		// first of all, ignoring the timings and restriction to certain
		// moves...

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new UCIUpdater(), 1000, 1000);

		// iterative deepening
		PositionTreeNode root_node = hash_manager.getRootNode();

		// Parameters for aspiration windows
		int alpha = NEG_INF; // initial value
		int beta = POS_INF; // initial value
		int asp_window = 150; // often 50 or 25 is used
		int factor = 2; // factor for increasing if out of bounds

		for (int current_depth = 1; current_depth <= searchDepth; current_depth++) {
			evalBoard(root_node, current_depth, current_depth, alpha, beta);

			if (root_node.getPosition().getAnalysisResult().score == NEG_INF
					&& root_node.getPosition().board.getActiveColor() == Side.BLACK
					|| root_node.getPosition().getAnalysisResult().score == POS_INF
					&& root_node.getPosition().board.getActiveColor() == Side.WHITE) {
				searchDepth = current_depth;
				break; // mate found
			}

			// If Value is out of bounds, redo search with larger bounds, but
			// with the same variation tree
			if (root_node.getPosition().getAnalysisResult().score < alpha) {
				alpha -= factor * asp_window;
				current_depth--;
				continue;
			} else if (root_node.getPosition().getAnalysisResult().score > beta) {
				beta += factor * asp_window;
				current_depth--;
				continue;
			}

			alpha = root_node.getPosition().getAnalysisResult().score
					- asp_window;
			beta = root_node.getPosition().getAnalysisResult().score
					+ asp_window;
		}

		timer.cancel();

		UCIReporter.sendInfoPV(root_node, searchDepth, root_node.getPosition()
				.getAnalysisResult().score, root_node.getPosition().board
				.getActiveColor());

		return root_node.getPosition().getBest_move();
	}

	@Override
	public IMove stop() {
		// TODO Auto-generated method stub
		return null;
	}

}
