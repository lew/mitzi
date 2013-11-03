package mitzi;

public class AnalysisResult {

	private int score;

	private boolean is_stalemate;

	private boolean needs_deeper;

	public AnalysisResult(int score, boolean is_stalemate, boolean needs_deeper) {
		this.score = score;
		this.is_stalemate = is_stalemate;
		this.needs_deeper = needs_deeper;
	}

	/**
	 * The boards score in centipawns.
	 * 
	 * @return
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Check if the board is a stalemate position. I. e. no moves are possible
	 * but there is no check.
	 * 
	 * @return true iff stalemate
	 */
	public boolean isStalemate() {
		return is_stalemate;
	}

	/**
	 * Check if the evaluation method considers this board to be in an unstable
	 * state and recommends a deeper evalutation.
	 * 
	 * @return true iff further evaluation is advised
	 */
	public boolean needsDeeper() {
		return needs_deeper;
	}
}
