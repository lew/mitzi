package mitzi;

public class PrimitiveBoardAnalyzer implements IBoardAnalyzer {

	@Override
	public AnalysisResult eval0(IBoard board) {
		
		int score = 0;

		int[] fig_value = { 100, 500, 325, 325, 975, 000 };

		// Maybe not the most efficient way (several runs over the board)
		for (Side c : Side.values()) {
			int side_sign = Side.getSideSign(c);
			for (Piece fig : Piece.values()) {
				score += board.getNumberOfPiecesByColorAndType(c, fig)
						* fig_value[fig.ordinal()] * side_sign;
			}

		}

		AnalysisResult result = new AnalysisResult(score, false, false);
		return result;
	}

}
