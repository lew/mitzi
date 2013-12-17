package mitzi;

public class BasicBoardAnalyzer implements IPositionAnalyzer {

	private int[] start_castling;

	private int[] piece_values = { 100, 500, 325, 325, 975, 000 };
	
	@Override
	public AnalysisResult eval0(IPosition board) {

		int score = 0;
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
		AnalysisResult result = new AnalysisResult(score, null, true, 0, 0, null);
		return result;
	}
	
	@Override
	public AnalysisResult evalBoard(IPosition board,int  alpha, int beta){

		return eval0(board);
	}
	
	@Override
	public void setCastling(IPosition position){
		start_castling[0] = position.canCastle(31) ? 31 : -1;
		start_castling[1] = position.canCastle(71) ? 71 : -1;
		start_castling[2] = position.canCastle(38) ? 38 : -1;
		start_castling[3] = position.canCastle(78) ? 78 : -1;
	}
}
