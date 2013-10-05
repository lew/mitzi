package mitzi;

import java.util.Random;
import java.util.Set;

public class RandyBrain implements IBrain {

	private IBoard board;
	private Set<IMove> moves;

	@Override
	public void set(IBoard board) {
		this.board = board;
	}

	@Override
	public IMove search(int movetime, int maxMoveTime, int searchDepth,
			boolean infinite, Set<IMove> searchMoves) {

		// ignores time limits and other restrictions
		// take that, UCI…

		moves = board.getPossibleMoves();

		int randy = new Random().nextInt(moves.size());
		int i = 0;
		for (IMove move : moves) {
			if (i == randy)
				return move;
			i = i + 1;
		}

		return null; // cannot not happen anyway
	}

	@Override
	public IMove stop() {
		//TODO: implement :)
		return null;
	}

}
