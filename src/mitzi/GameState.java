package mitzi;

import java.util.ArrayList;

public class GameState {

	private IPosition position;

	private ArrayList<IMove> history = new ArrayList<IMove>();

	private int half_move_clock;

	private int full_move_clock;

	private class GameClock {
		// TODO study UCI time management
	}

	public GameState() {
		position = new Position();
		setToInitial();
	}
	
	public void setToInitial() {
		position.setToInitial();
		half_move_clock = 0;
		full_move_clock = 1;
	}

	public void setToFEN(String fen) {
		position = new Position();
		position.setToFEN(fen);

		String[] fen_parts = fen.split(" ");
		// set half move clock
		half_move_clock = Integer.parseInt(fen_parts[4]);
		// set full move clock
		full_move_clock = Integer.parseInt(fen_parts[5]);
	}
	
	public void doMove(IMove move) {
		mitzi.IPosition.MoveApplication mova = position.doMove(move);
		if (mova.resets_half_move_clock) {
			half_move_clock = 0;
		}
		if (position.getActiveColor() == Side.BLACK) {
			full_move_clock++;
		}
		history.add(move);
		position = mova.new_position;
	}

	public IPosition getPosition() {
		return position;
	}

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
	 * @return returns a list of all played moves.
	 */
	public ArrayList<IMove> getHistory(){
		return history;
	}
}
