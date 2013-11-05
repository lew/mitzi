package mitzi;

public class Position {

	public final IBoard board;

	private AnalysisResult analysis_result;
	
	private IMove best_move;

	Position(IBoard board) {
		this.board = board;
	}

	public AnalysisResult getAnalysisResult() {
		return analysis_result;
	}

	public void setAnalysisResult(AnalysisResult analysis_result) {
		this.analysis_result = analysis_result;
	}

	public IMove getBest_move() {
		return best_move;
	}

	public void setBest_move(IMove best_move) {
		this.best_move = best_move;
	}
	
	@Override
	public String toString() {
		if (analysis_result != null)
			return board.toString() + " " + analysis_result.toString();
		else
			return board.toString() + " no evaluation";
	}
	
}
