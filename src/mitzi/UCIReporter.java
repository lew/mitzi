package mitzi;

public final class UCIReporter {

	/**
	 * the engine should send these infos regularly
	 * 
	 * DEPTH: search depth in plies
	 * 
	 * NODES: number of nodes searched
	 * 
	 * NPS: number of nodes per second searched
	 */
	public static enum InfoType {
		DEPTH("depth"), NODES("nodes"), NPS("nps");

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
	public static void sendInfoNum(InfoType type, long eval_counter) {
		System.out.println("info " + type.string + " " + eval_counter);
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
	 * @param score
	 *            the board's score in centipawns. positive values are in favor
	 *            of white.
	 */
	public static void sendInfoPV(Variation pv, int depth, int score) {
		System.out.println("info score cp " + score + " depth " + depth
				+ " pv " + pv);
	}
}
