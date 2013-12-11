package mitzi;

import mitzi.IMove;
import mitzi.RandyBrain;

/** 
 * The environment for playing chess
 *
 */
public class ChessGame {

	private static GameState game_state;

	public static void main(String[] args) {

		System.out.println("Lets play chess!");

		IMove move;

		game_state = new GameState();

		RandyBrain randy = new RandyBrain();
		HumanBrain human = new HumanBrain();
		//MitziBrain mitzi = new MitziBrain();
		
		while (true) {
			//Humans turn
			human.set(game_state);
			move = human.search(0, 0, 0, false, null);
			game_state.doMove(move);
			if (game_state.getPosition().isMatePosition()) {
				System.out.println("You won!");
				break;
			}
			if (game_state.getPosition().isStaleMatePosition()) {
				System.out.println("Draw!");
				break;
			}
			System.out.println(game_state.getPosition());
			
			//Randys turn
			randy.set(game_state);
			move = randy.search(0, 0, 0, false, null);
			System.out.println("Randy plays:" + move);
			game_state.doMove(move);
			if (game_state.getPosition().isMatePosition()) {
				System.out.println("You lost!");
				break;
			}
			if (game_state.getPosition().isStaleMatePosition()) {
				System.out.println("Draw!");
				break;
			}
			System.out.println(game_state.getPosition());

			/*
			//Mitzis turn
			mitzi.set(game_state);
			move = mitzi.search(100000, 100000, 6, false, null);
			System.out.println("Mitzi plays:" + move);
			game_state.doMove(move);
			if (game_state.getPosition().isMatePosition()) {
				System.out.println("You lost!");
				break;
			}
			if (game_state.getPosition().isStaleMatePosition()) {
				System.out.println("Draw!");
				break;
			}
			System.out.println(game_state.getPosition());
			*/
		}

	}

}
