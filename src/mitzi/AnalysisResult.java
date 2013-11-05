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

	private int plys_to_eval0 = 0;

	private int plys_to_seldepth = 0;

	private Flag flag;

	AnalysisResult(int score, Boolean is_stalemate) {
		this.score = score;
		this.is_stalemate = is_stalemate;
		this.needs_deeper = true;
	}

	AnalysisResult(int score, Boolean is_stalemate, boolean needs_deeper,
			int plys_to_eval0, int plys_to_seldepth, Flag flag) {
		this.score = score;
		this.is_stalemate = is_stalemate;
		this.needs_deeper = needs_deeper;
		this.plys_to_eval0 = plys_to_eval0;
		this.plys_to_seldepth = plys_to_seldepth;
		this.flag = flag;
	}

	/**
	 * Gives the distance to (complete) search depth at which this result was
	 * obtained.
	 * 
	 * @return the distance to search depth
	 */
	public int getPlysToEval0() {
		return plys_to_eval0;
	}

	public void setPlysToEval0(int search_depth) {
		this.plys_to_eval0 = search_depth;
	}

	/**
	 * Gives the distance to selective search depth at which this result was
	 * obtained.
	 * 
	 * @return the distance to selective search depth
	 */
	public int getPlysToSelDepth() {
		return plys_to_seldepth;
	}

	public void setPlysToSelDepth(int sel_depth) {
		this.plys_to_seldepth = sel_depth;
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
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

	public int compareQualityTo(AnalysisResult o) {
		if (o == null)
			throw new NullPointerException();

		if (this == o)
			return 0;

		// (deeper results)
		if (this.plys_to_eval0 > o.plys_to_eval0)
			return 1;

		// or (equally deep results) and (deeper or equal selective results)
		if (this.plys_to_eval0 == o.plys_to_eval0
				&& this.plys_to_seldepth > o.plys_to_seldepth)
			return 1;

		if (this.plys_to_eval0 == o.plys_to_eval0
				&& this.plys_to_seldepth == o.plys_to_seldepth)
			return 0;

		return -1;
	}

	@Override
	public String toString() {
		return "cp: " + score + " depth: " + plys_to_eval0 + (flag != null  ? " flag: " + flag : "");
	}
}
