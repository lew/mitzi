package mitzi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import mitzi.GameState;

public class HumanBrain implements IBrain {

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
			boolean infinite, List<IMove> searchMoves) {
		
		//Read in the move as string
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String string_move = null;
		try {
			string_move = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//convert it to an object move.
		IMove move = new Move(string_move);
		//if the move was illegal, the player has to choose another one.
		while(!game_state.getPosition().isPossibleMove(move)){
			System.out.println("Illegal move, choose another one!");
			try {
				string_move = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			move = new Move(string_move);
		}
		//return the choosen move.
		return move;
	}

	@Override
	public IMove stop() {
		return null;
	}

}
