package mitzi;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static mitzi.MateScores.*;
import mitzi.UCIReporter.InfoType;

public class MitziBrain implements IBrain {

	private GameState game_state;

	private long eval_counter;

	private long table_counter = 0;

	private IPositionAnalyzer board_analyzer = new BoardAnalyzer();

	@Override
	public void set(GameState game_state) {
		this.game_state = game_state;
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
						/ time_span);
			}

			old_mtime = mtime;
			old_eval_counter += eval_span;

		}
	}

	/**
	 * NegaMax with Alpha Beta Pruning and Transposition Tables
	 * 
	 * @see <a
	 *      href="http://en.wikipedia.org/wiki/Negamax#NegaMax_with_Alpha_Beta_Pruning_and_Transposition_Tables">NegaMax
	 *      with Alpha Beta Pruning and Transposition Tables</a>
	 * @param position
	 *            the position to evaluate
	 * @param total_depth
	 *            the total depth to search
	 * @param depth
	 *            the remaining depth to search
	 * @param alpha
	 *            the alpha value
	 * @param beta
	 *            the beta value
	 * @return returns the result of the evaluation
	 */
	private AnalysisResult evalBoard(IPosition position, int total_depth,
			int depth, int alpha, int beta) {

		int alpha_old = alpha;

		// Cache lookup
		position = IPositionCache.getPosition(position);

		AnalysisResult old_result = position.getAnalysisResult();
		if (old_result != null && old_result.plys_to_eval0 >= depth) {
			table_counter++;
			if (old_result.flag == Flag.EXACT) {
				return old_result;
			} else if (old_result.flag == Flag.LOWERBOUND)
				alpha = Math.max(alpha, old_result.score);
			else if (old_result.flag == Flag.UPPERBOUND)
				beta = Math.min(beta, old_result.score);

			if (alpha >= beta) {
				return old_result;
			}
		}

		// whose move is it?
		Side side = position.getActiveColor();
		int side_sign = Side.getSideSign(side);

		// generate moves
		Set<IMove> moves = position.getPossibleMoves();

		// check for mate and stalemate (the side should alternate)
		if (moves.isEmpty()) {
			eval_counter++;
			if (position.isCheckPosition()) {
				return new AnalysisResult(NEG_INF * side_sign, false, false, 0,
						0, Flag.EXACT);
			} else {
				return new AnalysisResult(0, true, false, 0, 0, Flag.EXACT);
			}
		}

		// base case
		if (depth == 0) {
			AnalysisResult result = board_analyzer.evalBoard(position, alpha,beta);
			//AnalysisResult result = board_analyzer.eval0(position);
			return result;
		}

		LinkedList<IMove> ordered_moves = new LinkedList<IMove>();
		BasicMoveComparator move_comparator = new BasicMoveComparator(position);
		// Sort the moves:
		if (old_result != null) {
			ordered_moves.addAll(position.getBestMoves());
			Collections.reverse(ordered_moves);

			LinkedList<IMove> remaining_moves = new LinkedList<IMove>();
			for (IMove move : moves)
				if (!ordered_moves.contains(move))
					remaining_moves.add(move);

			Collections.sort(remaining_moves,
					Collections.reverseOrder(move_comparator));

			ordered_moves.addAll(remaining_moves);

		} else {
			// no previous computation given, use basic heuristic
			ordered_moves.addAll(moves);
			Collections.sort(ordered_moves,
					Collections.reverseOrder(move_comparator));
		}

		//Delete the move, to recieve a better ordering (with larger depth)
		if (old_result != null && old_result.plys_to_eval0 < depth)
			position.resetBestMoves();

		// create parent AnalysisResult
		AnalysisResult parent = null;

		int best_value = NEG_INF; // this starts always at negative!

		int i = 0;
		// alpha beta search
		for (IMove move : ordered_moves) {

			if (depth == total_depth && total_depth >= 6) {
				// output currently searched move to UCI
				UCIReporter.sendInfoCurrMove(move, i + 1);
			}

			IPosition child_pos = position.doMove(move).new_position;
			AnalysisResult result = evalBoard(child_pos, total_depth,
					depth - 1, -beta, -alpha);
			eval_counter++;
			child_pos.updateAnalysisResult(result);

			int negaval = result.score * side_sign;

			// Add move for orderring
			if (negaval >= best_value - 50 && old_result != null && old_result.plys_to_eval0 < depth)
				position.addBetterMove(move);
			
			// better variation found
			if (negaval >= best_value) {
				boolean truly_better = negaval > best_value;
				best_value = negaval;

				// update AnalysisResult
				parent = result.tinyCopy();
				parent.best_move = move;
				parent.best_child = child_pos;
				parent.plys_to_eval0++;
				parent.plys_to_seldepth++;

				// output to UCI
				if (depth == total_depth && truly_better) {
					UCIReporter.sendInfoPV(game_state.getPosition());
				}
			}

			// alpha beta cutoff
			alpha = Math.max(alpha, negaval);
			if (alpha >= beta)
				break;

			i++; // keep ordered_moves and ordered_variations in sync
		}

		// Transposition Table Store; game_state is the lookup key for parent
		if (parent.score <= alpha_old)
			parent.flag = Flag.UPPERBOUND;
		else if (parent.score >= beta)
			parent.flag = Flag.LOWERBOUND;
		else
			parent.flag = Flag.EXACT;

		return parent;

	}

	@Override
	public IMove search(int movetime, int maxMoveTime, int searchDepth,
			boolean infinite, Set<IMove> searchMoves) {

		// first of all, ignoring the timings and restriction to certain
		// moves...

		IPosition position = game_state.getPosition();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new UCIUpdater(), 1000, 5000);

		// iterative deepening
		AnalysisResult result = null;

		// Parameters for aspiration windows
		int alpha = NEG_INF; // initial value
		int beta = POS_INF; // initial value
		int asp_window = 50; // often 50 or 25 is used
		int factor = 2; // factor for increasing if out of bounds

		for (int current_depth = 1; current_depth <= searchDepth; current_depth++) {
			table_counter = 0;
			result = evalBoard(position, current_depth, current_depth, alpha,
					beta);
			position.updateAnalysisResult(result);

			if (result.score == POS_INF || result.score == NEG_INF) {
				break;
			}

			// If Value is out of bounds, redo search with larger bounds, but
			// with the same variation tree
			if (result.score <= alpha) {
				alpha -= factor * asp_window;
				current_depth--;
				UCIReporter.sendInfoString("Boards found: " + table_counter);
				continue;
			} else if (result.score >= beta) {
				beta += factor * asp_window;
				current_depth--;
				UCIReporter.sendInfoString("Boards found: " + table_counter);
				continue;
			}

			alpha = result.score - asp_window;
			beta = result.score + asp_window;
			UCIReporter.sendInfoString("Table size: " + IPositionCache.getSize());
			UCIReporter.sendInfoString("Boards found: " + table_counter);
		}

		timer.cancel();
		UCIReporter.sendInfoPV(position);
		return result.best_move;
	}

	@Override
	public IMove stop() {
		// TODO Auto-generated method stub
		return null;
	}

}
