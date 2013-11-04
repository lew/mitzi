package mitzi;

public final class AnalysisResult {

	/**
	 * The boards score in centipawns.
	 */
	public final int score;

	/**
	 * If true, the board is a stalemate position. I. e. no moves are possible
	 * but there is no check. If null, then it has not been analyzed.
	 */
	public final Boolean is_stalemate;

	/**
	 * If the evaluation method considers this board to be in an unstable state
	 * and recommends a deeper evalutation or is simply not sure, this is set to
	 * true.
	 */
	public final boolean needs_deeper;

	private int search_depth;

	private int sel_depth;

	public AnalysisResult(int score, Boolean is_stalemate, boolean needs_deeper) {
		this.score = score;
		this.is_stalemate = is_stalemate;
		this.needs_deeper = needs_deeper;
	}

	/**
	 * Gives the (complete) search depth at which this result was obtained.
	 * 
	 * @return the search depth
	 */
	public int getSearchDepth() {
		return search_depth;
	}

	public void setSearchDepth(int search_depth) {
		this.search_depth = search_depth;
	}

	/**
	 * Gives the selective search depth at which this result was obtained.
	 * 
	 * @return the selective search depth
	 */
	public int getSelDepth() {
		return sel_depth;
	}

	public void setSelDepth(int sel_depth) {
		this.sel_depth = sel_depth;
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
