package mitzi;

import java.util.ArrayList;

public class GameState {

	/**
	 * the actual position of the current game state
	 */
	private IPosition position;

	/**
	 * the history of played moves
	 */
	private ArrayList<IMove> history = new ArrayList<IMove>();

	/**
	 * This is the number of halfmoves since the last pawn advance or capture.
	 * This is used to determine if a draw can be claimed under the fifty-move
	 * rule.
	 */
	private int half_move_clock;

	/**
	 * The number of the full move. It starts at 1, and is incremented after
	 * Black's move.
	 */
	private int full_move_clock;

	private class GameClock {
		// TODO study UCI time management
	}

	/**
	 * creates a new Game with initial position.
	 */
	public GameState() {
		position = new Position();
		setToInitial();
	}

	/**
	 * sets the current game to the initial state.
	 */
	public void setToInitial() {
		position.setToInitial();
		half_move_clock = 0;
		full_move_clock = 1;
	}

	/**
	 * sets the current game to the position of the given fen string
	 * 
	 * @param fen
	 *            the position in fen notation
	 */
	public void setToFEN(String fen) {
		position = new Position();
		position.setToFEN(fen);

		String[] fen_parts = fen.split(" ");
		// set half move clock
		half_move_clock = Integer.parseInt(fen_parts[4]);
		// set full move clock
		full_move_clock = Integer.parseInt(fen_parts[5]);
	}

	/**
	 * Do the given move and update half_move_clock, full_move_clock and
	 * history. It is checked, if the move is valid or not.
	 * 
	 * @param move
	 *            the given move
	 */
	public void doMove(IMove move) {
		if (position.isPossibleMove(move)) {
			mitzi.IPosition.MoveApplication mova = position.doMove(move);
			if (mova.resets_half_move_clock) {
				half_move_clock = 0;
			}
			if (position.getActiveColor() == Side.BLACK) {
				full_move_clock++;
			}
			history.add(move);
			position = mova.new_position;
		} else {
			throw new IllegalArgumentException("INVALID MOVE");
		}
	}

	/**
	 * @return the actual position of the game
	 */
	public IPosition getPosition() {
		return position;
	}

	/**
	 * creates the fen string of the actual board.
	 */
	@Override
	public String toString() {
		StringBuilder fen = new StringBuilder();

		fen.append(position.toString());
		fen.append(" ");

		// halfmove clock
		fen.append(half_move_clock);
		fen.append(" ");

		// fullmove clock
		fen.append(full_move_clock);

		return fen.toString();
	}

	/**
	 * This is the number of halfmoves since the last pawn advance or capture.
	 * This is used to determine if a draw can be claimed under the fifty-move
	 * rule.
	 * 
	 * @return number of halfmoves since the last pawn advance or capture
	 */
	public int getHalfMoveClock() {
		return half_move_clock;
	}

	/**
	 * The number of the full move. It starts at 1, and is incremented after
	 * Black's move.
	 * 
	 * @return number of the full move
	 */
	public int getFullMoveClock() {
		return full_move_clock;
	}

	/**
	 * return all previous played moves.
	 * 
	 * @return returns a list of all played moves.
	 */
	public ArrayList<IMove> getHistory() {
		return history;
	}
}
