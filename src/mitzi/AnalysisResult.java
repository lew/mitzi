package mitzi;

public class AnalysisResult {

	private int score;

	private boolean needs_deeper;

	public AnalysisResult(int score, boolean needs_deeper) {
		this.score = score;
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
	 * Check if the evaluation method considers this board to be in an unstable
	 * state and recommends a deeper evalutation.
	 * 
	 * @return true iff further evaluation is advised
	 */
	public boolean needsDeeper() {
		return needs_deeper;
	}
}
