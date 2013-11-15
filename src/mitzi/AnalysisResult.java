package mitzi;

public final class AnalysisResult {

	/**
	 * The boards score in centipawns.
	 */
	public int score;

	/**
	 * If true, the board is a stalemate position. I. e. no moves are possible
	 * but there is no check. If null, then it has not been analyzed.
	 */
	public Boolean is_stalemate;

	/**
	 * If the evaluation method considers this board to be in an unstable state
	 * and recommends a deeper evalutation or is simply not sure, this is set to
	 * true.
	 */
	public boolean needs_deeper;
	/**
	 * The distance to (complete) search depth at which this result was
	 * obtained.
	 */
	public int plys_to_eval0 = 0;

	/**
	 * The distance to selective search depth at which this result was obtained.
	 */
	public int plys_to_seldepth = 0;

	public Flag flag;

	public IMove best_move;

	public IPosition best_child;

	AnalysisResult(int score, Boolean is_stalemate, boolean needs_deeper,
			int plys_to_eval0, int plys_to_seldepth, Flag flag) {
		this.score = score;
		this.is_stalemate = is_stalemate;
		this.needs_deeper = needs_deeper;
		this.plys_to_eval0 = plys_to_eval0;
		this.plys_to_seldepth = plys_to_seldepth;
		this.flag = flag;
	}

	public AnalysisResult tinyCopy() {
		return new AnalysisResult(score, is_stalemate, needs_deeper,
				plys_to_eval0, plys_to_seldepth, null);
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
		return "cp: " + score + " depth: " + plys_to_eval0
				+ (flag != null ? " flag: " + flag : "");
	}
}
