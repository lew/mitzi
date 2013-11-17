package mitzi;

import java.util.Random;
import java.util.Set;

/**
 * This class implements the most basic search engine, the random move
 * selection. All possible moves of the actual game state are computed and one
 * of them is randomly selected.
 * 
 */
public class RandyBrain implements IBrain {

	/**
	 * The current game state
	 */
	private GameState game_state;

	@Override
	public void set(GameState game_state) {
		this.game_state = game_state;
	}

	@Override
	public IMove search(int movetime, int maxMoveTime, int searchDepth,
			boolean infinite, Set<IMove> searchMoves) {

		Set<IMove> moves = game_state.getPosition().getPossibleMoves();

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
		// no need to implement the stop function, since RandyBrain is fast
		// enough.
		return null;
	}

}
