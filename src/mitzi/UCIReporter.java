package mitzi;

import java.util.Map;

public final class UCIReporter {

	/**
	 * the engine should send these infos regularly
	 * 
	 * DEPTH: search depth in plies
	 * 
	 * NODES: number of nodes searched
	 * 
	 * NPS: number of nodes per second searched
	 * 
	 * SCORE_CP: the score from the engine's point of view in centipawns
	 */
	public static enum InfoType {
		DEPTH("depth"), NODES("nodes"), NPS("nps"), SCORE_CP("score cp");

		public String string;

		InfoType(String string) {
			this.string = string;
		}
	}

	private UCIReporter() {
	};

	/**
	 * Send debugging messages to the GUI.
	 * 
	 * @param string
	 *            the message to be displayed
	 */
	public static void sendInfoString(String string) {
		System.out.println("info string " + string);
	}

	/**
	 * Send information about search depth, number of nodes searched and number
	 * of nodes searched per second to the GUI.
	 * 
	 * @param type
	 *            one of UCIReporter.InfoType
	 * @param string
	 *            the integer value to be sent
	 */
	public static void sendInfoInt(InfoType type, int value) {
		System.out.println("info " + type.string + " " + value);
	}

	/**
	 * The Principal variation (PV) is a sequence of moves that programs
	 * consider best and therefore expect to be played. Also all infos belonging
	 * to the PV should be sent together.
	 * 
	 * NOTE: will most likely be extended later
	 * 
	 * @param pv
	 * @param depth
	 * @param value
	 *            the board's value in centipawns. positive values are in favor
	 *            of white.
	 */
	public static void sendInfoPV(Map<Integer, IMove> pv, int depth, int value) {
		System.out.print("info value cp " + value + " depth " + depth + " pv");
		for (int i = 0; pv.get(i) != null; i++)
			System.out.print(" " + pv.get(i));
		System.out.println();
	}
}
