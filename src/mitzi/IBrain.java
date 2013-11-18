package mitzi;

import java.util.List;

public interface IBrain {

	/**
	 * Before the engine is asked to search on a game state, there will always be
	 * this command to tell the engine about the current game state.
	 * 
	 * @param game_state
	 *            the current game state
	 */
	public void set(GameState game_state);

	/**
	 * Start calculating on the current position.
	 * 
	 * @param movetime
	 *            search for exactly this time in milliseconds
	 * @param maxMoveTime
	 *            search for at most this time in milliseconds
	 * @param searchDepth
	 *            the maximum search depth in plys
	 * @param infinite
	 *            If set to true, search until the "stop" command. Do not exit
	 *            the search without being told so in this mode!
	 * @param searchMoves
	 *            Restrict search to this moves only. If null, the engine may
	 *            search any moves.
	 * @return the hopefully best move
	 */
	public IMove search(int movetime, int maxMoveTime, int searchDepth,
			boolean infinite, List<IMove> searchMoves);

	/**
	 * Stop calculating immediately and return the best move.
	 * 
	 * @return the currently best move
	 */
	public IMove stop();

}
