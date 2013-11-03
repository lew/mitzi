package mitzi;

public final class AnalysisResult {

	private final int score;

	private final boolean is_stalemate;

	private final boolean needs_deeper;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (is_stalemate ? 1231 : 1237);
		result = prime * result + (needs_deeper ? 1231 : 1237);
		result = prime * result + score;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AnalysisResult other = (AnalysisResult) obj;
		if (is_stalemate != other.is_stalemate
				|| needs_deeper != other.needs_deeper || score != other.score) {
			return false;
		}
		return true;
	}
}
