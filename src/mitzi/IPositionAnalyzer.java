package mitzi;

public interface IPositionAnalyzer {

	/**
	 * Evaluates the given board and returns a value in centipawns, this
	 * function should not include further increase of search depth.
	 * 
	 * @param board
	 *            the board to be analyzed
	 * @return a analysisResult, containing the value in centipawns
	 */
	public AnalysisResult eval0(IPosition board);

	/**
	 * Evaluates the given board and returns a value in centipawns, this
	 * function should/can include further increase of search depth.
	 * 
	 * @param board
	 *            the board to be analyzed
	 * @param alpha
	 *            the alpha value of the alpha-beta algorithm
	 * @param beta
	 *            the beta value of the alpha-beta algorithm
	 * @return a analysisResult, containing the value in centipawns and the
	 *         selective depth
	 * @throws InterruptedException 
	 */
	public AnalysisResult evalBoard(IPosition board, int alpha, int beta) throws InterruptedException;

	/**
	 * stores the castling rights
	 * @param position the actual position
	 */
	public void setCastling(IPosition position);

}
