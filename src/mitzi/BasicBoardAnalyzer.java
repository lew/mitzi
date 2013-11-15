package mitzi;

public class BasicBoardAnalyzer implements IPositionAnalyzer {

	@Override
	public int eval0(IPosition board) {

		int score = 0;

		int[] piece_values = { 100, 500, 325, 325, 975, 000 };
		int bishop_pair_value = 50;

		// basic evaluation
		for (Side side : Side.values()) {
			int side_sign = Side.getSideSign(side);

			// piece values
			for (Piece piece : Piece.values()) {
				score += board.getNumberOfPiecesByColorAndType(side, piece)
						* piece_values[piece.ordinal()] * side_sign;
			}

			// bishop pair gives bonus
			if (board.getNumberOfPiecesByColorAndType(side, Piece.BISHOP) == 2) {
				score += bishop_pair_value * side_sign;
			}
		}

		return score;
	}
	
	@Override
	public AnalysisResult evalBoard(IPosition board,int  alpha, int beta){
		
		int score = eval0(board);
		AnalysisResult result = new AnalysisResult(score, null);
		return result;
	}
}
