package mitzi;

import static mitzi.MateScores.*;

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
	 * Send information about the currently searched move to the GUI.
	 * 
	 * @param move
	 *            currently searching this IMove
	 * @param move_number
	 *            currently searching move number n, for the first move n should
	 *            be 1 not 0.
	 */
	public static void sendInfoCurrMove(IMove move, int move_number) {
		System.out.println("info currmove " + move + " currmovenumber "
				+ move_number);
	}

	/**
	 * The Principal variation (PV) is a sequence of moves that programs
	 * consider best and therefore expect to be played. Also all infos belonging
	 * to the PV should be sent together.
	 * 
	 * @param position
	 *            a Position with an AnalysisResult
	 */
	public static void sendInfoPV(IPosition position) {
		AnalysisResult result = position.getAnalysisResult();
		if (result == null)
			return;
					
		if (result.score == NEG_INF && position.getActiveColor() == Side.WHITE
				|| result.score == POS_INF
				&& position.getActiveColor() == Side.BLACK) {
			System.out.print("info score mate -"
					+ ((result.plys_to_eval0 + 1) / 2) + " depth "
					+ result.plys_to_eval0 + " sel_depth "
					+ result.plys_to_seldepth + " pv");
		} else if (result.score == NEG_INF
				&& position.getActiveColor() == Side.BLACK
				|| result.score == POS_INF
				&& position.getActiveColor() == Side.WHITE) {
			System.out.print("info score mate "
					+ ((result.plys_to_eval0 + 1) / 2) + " depth "
					+ result.plys_to_eval0 + " sel_depth "
					+ result.plys_to_seldepth + " pv");
		} else {
			System.out.print("info score cp " + result.score + " depth "
					+ result.plys_to_eval0 + " sel_depth "
					+ result.plys_to_seldepth +" pv");
		}
		
		for (IMove move : result.getPV(position)) {
			System.out.print(" " + move);
		}
		
		System.out.println();
	}
}
