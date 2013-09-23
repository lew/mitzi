package mitzi;

import java.util.List;

public interface IBoard {

	public void setToInitial();

	public void setToFEN(String fen);

	public IBoard doMove(IMove move);

	public List<IMove> getPossibleMoves();

}
