package mitzi;

public interface IPositionAnalyzer {
	
	public int eval0(IPosition board);
	
	public AnalysisResult evalBoard(IPosition board, int alpha, int beta);

}
