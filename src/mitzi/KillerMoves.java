package mitzi;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KillerMoves {
	private static Map<Integer, LinkedList<IMove>> killer_moves = new HashMap<Integer, LinkedList<IMove>>(
			35);
	
	private static int MAX_SIZE = 2;

	KillerMoves() {
	};

	static LinkedList<IMove> getKillerMoves(int ply) {
		LinkedList<IMove> k_m = killer_moves.get(ply);
		if (k_m == null)
			k_m=new LinkedList<IMove>();
			return k_m;
	}

	static void addKillerMove(int ply, IMove move) {
		LinkedList<IMove> k_m = killer_moves.get(ply);
		if(k_m ==null)
			k_m= new LinkedList<IMove>();
		if (k_m.size() == MAX_SIZE)
			k_m.iterator().remove();

		k_m.add(move);
	}
	
	static void addKillerMove(int ply, IMove move, List<IMove> entry) {
		if (entry.size() == MAX_SIZE)
			entry.iterator().remove();
		entry.add(move);
	}
	
	static void updateKillerMove(){
		for(int i = 2;killer_moves.containsKey(i);i++)
			killer_moves.put(i-2, killer_moves.get(i));
		
	}
}
