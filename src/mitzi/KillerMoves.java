package mitzi;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * this class saves for each ply a certain number (e.g. 2) of moves
 * (killermoves), which causes an alpha-beta cutoff. If more moves are saved,
 * that allowed, then they get deleted in the order they are saved (like
 * FIFO).This should improve the move ordering.
 * 
 */
public class KillerMoves {

	/**
	 * a map from a ply to the killermoves.
	 */
	private static Map<Integer, LinkedList<IMove>> killer_moves = new HashMap<Integer, LinkedList<IMove>>(
			35);

	/**
	 * number of killermoves saved
	 */
	private static int MAX_SIZE = 2;

	KillerMoves() {
	};

	/**
	 * returns for a given ply the killer moves, note that it should be checked
	 * if the move is legal.
	 * 
	 * @param ply
	 *            the plys from root node
	 * @return a list of killer moves.
	 */
	static LinkedList<IMove> getKillerMoves(int ply) {
		LinkedList<IMove> k_m = killer_moves.get(ply);
		if (k_m == null)
			k_m = new LinkedList<IMove>();
		return k_m;
	}

	/**
	 * add a new killermove, if more moves are saved than MAX_SIZE, the first
	 * killermove got removed.
	 * 
	 * @param ply
	 *            depth in the search tree
	 * @param move
	 *            the move to be added
	 */
	static void addKillerMove(int ply, IMove move) {
		LinkedList<IMove> k_m = killer_moves.get(ply);
		if (k_m == null)
			k_m = new LinkedList<IMove>();
		if (k_m.size() == MAX_SIZE)
			k_m.iterator().remove();

		k_m.add(move);
	}

	/**
	 * add a new killermove, if more moves are saved than MAX_SIZE, the first
	 * killermove got removed.
	 * 
	 * @param ply
	 *            depth in the search tree
	 * @param move
	 *            the move to be added
	 * @param entry
	 *            if available the old entry can be used for faster update. This
	 *            should be a reference to the old element.
	 */
	static void addKillerMove(int ply, IMove move, List<IMove> entry) {
		if (entry.size() == MAX_SIZE)
			entry.iterator().remove();
		entry.add(move);
	}

	/**
	 * updates the killermoves after the best move was found, i.e. all moves are
	 * shifted from depth -> depth -2
	 */
	static void updateKillerMove() {
		for (int i = 2; killer_moves.containsKey(i); i++)
			killer_moves.put(i - 2, killer_moves.get(i));

	}
}
