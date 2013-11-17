package mitzi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * 
 *         This class computes the value of a board in a proper way, see
 *         http://philemon.cycovery.com/site/part2.html for more details.
 * 
 */
public class BoardAnalyzer implements IPositionAnalyzer {

	// the square to array index from Position.java
	protected static int[] square_to_array_index = { 64, 64, 64, 64, 64, 64,
			64, 64, 64, 64, 64, 56, 48, 40, 32, 24, 16, 8, 0, 64, 64, 57, 49,
			41, 33, 25, 17, 9, 1, 64, 64, 58, 50, 42, 34, 26, 18, 10, 2, 64,
			64, 59, 51, 43, 35, 27, 19, 11, 3, 64, 64, 60, 52, 44, 36, 28, 20,
			12, 4, 64, 64, 61, 53, 45, 37, 29, 21, 13, 5, 64, 64, 62, 54, 46,
			38, 30, 22, 14, 6, 64, 64, 63, 55, 47, 39, 31, 23, 15, 7, 64, 64,
			64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
			64, 64, 64 };

	// The following arrays contains the value of a piece on a specific square,
	// always in favor of white. Since the arrays are symmetric w.r.t. the
	// columns, BLACK uses 63-i entry with opposite sign.
	static private int[] piece_activity_b_k = { -16, -16, -8, -8, -8, -8, -16,
			-16, -16, -16, -4, -4, -4, -4, -16, -16, -8, 2, 6, 6, 6, 6, 2, -8,
			-8, 2, 6, 6, 6, 6, 2, -8, -8, 2, 4, 4, 4, 4, 2, -8, -8, 2, 2, 2, 2,
			2, 2, -8, -8, -8, 0, 0, 0, 0, -8, -8, -16, -8, -8, -8, -8, -8, -8,
			-16 };

	static private int[] piece_activity_r = { 0, 0, 4, 6, 6, 4, 0, 0, 0, 0, 4,
			6, 6, 4, 0, 0, 0, 0, 4, 6, 6, 4, 0, 0, 0, 0, 4, 6, 6, 4, 0, 0, 0,
			0, 4, 6, 6, 4, 0, 0, 0, 0, 4, 6, 6, 4, 0, 0, 0, 0, 4, 6, 6, 4, 0,
			0, 0, 0, 4, 6, 6, 4, 0, 0, };

	static private int[] piece_activity_q = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4,
			5, 5, 4, 0, 0, 0, 2, 4, 10, 10, 4, 2, 0, 0, 2, 10, 12, 12, 10, 2,
			0, -10, 2, 10, 12, 12, 10, 2, -10, -10, -10, 4, 10, 10, 4, -10,
			-10, -10, 2, 8, 8, 8, 8, 2, -10, -10, -8, 0, 0, 0, 0, -8, -10, };

	static private int[] weak_positions = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 8, 12, 12, 8, 0, 0, 0, 2, 12, 16, 16, 12, 2, 0,
			0, 2, 12, 20, 20, 12, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };

	static private int[] piece_values = { 100, 500, 325, 325, 975, 000 };

	// if not all Bishop and Knight has moved, moving the queen results in
	// negative score
	static private int PREMATURE_QUEEN = -17; // not yet implemented

	// The player receives a bonus if the 2 bishops are alive.
	static private int bishop_pair_value = 50;

	
	static public long eval_counter_seldepth = 0;
	@Override
	public AnalysisResult eval0(IPosition board) {
		int score = 0;

		// Evaluate position - activity
		score += evalPieceActivity(board);

		// Evaluate weak/strong position
		score += evalWeakPosition(board);

		// Evaluate the pieces
		score += evalPieces(board);

		AnalysisResult result = new AnalysisResult(score, false, false, 0, 0,
				Flag.EXACT);
		return result;
	}

	@Override
	public AnalysisResult evalBoard(IPosition board, int alpha, int beta) {
		AnalysisResult result = quiesce(board, alpha, beta);
		
		//decrease the depth by one, because counted once too much (hopefully ;) ).
		result.plys_to_seldepth--;
		eval_counter_seldepth--;
		return result;
	}

	/**
	 * Implements Quiescence search to avoid the horizon effect. The function
	 * increase the search depth until no capture is possible, where only
	 * captures are analyzed.
	 * 
	 * @see <a
	 *      href="http://chessprogramming.wikispaces.com/Quiescence+Search">http://chessprogramming.wikispaces.com/Quiescence+Search</a>
	 * 
	 * @param board
	 *            the board to be analyzed
	 * @param alpha
	 *            the alpha value of alpha-beta search
	 * @param beta
	 *            the beta value of alpha-beta search
	 * @return the value of the board
	 */
	private AnalysisResult quiesce(IPosition board, int alpha, int beta) {

		AnalysisResult result = eval0(board);
		eval_counter_seldepth++;
		if (result.score >= beta) {
			result.score = beta;
			result.plys_to_seldepth++;
			return result;
		}
		if (result.score > alpha)
			alpha = result.score;

		Set<IMove> caputures = board.generateCaptures();
		BasicMoveComparator move_comparator = new BasicMoveComparator(board);

		// no previous computation given, use basic heuristic
		ArrayList<IMove> ordered_captures = new ArrayList<IMove>(caputures);
		Collections.sort(ordered_captures,
				Collections.reverseOrder(move_comparator));
		
		for (IMove move : caputures) {
			IPosition pos = board.doMove(move).new_position;
			result = quiesce(pos, -beta, -alpha);
			result.score = -result.score;

			if (result.score >= beta) {
				result.score = beta;
				result.plys_to_seldepth++;
				return result;
			}
			if (result.score > alpha)
				alpha = result.score;
		}
		result.plys_to_seldepth++;
		return result;

	}

	/**
	 * Evaluates only the material value of the board.
	 * 
	 * @param board
	 *            the actual board
	 * @return the material value
	 */
	private int evalPieces(IPosition board) {
		int score = 0;

		// basic evaluation
		for (Side side : Side.values()) {
			int side_sign = Side.getSideSign(side);

			// piece values
			for (Piece piece : Piece.values()) {
				score += board.getNumberOfPiecesByColorAndType(side, piece)
						* piece_values[piece.ordinal()] * side_sign;
			}

			// bishop pair gives bonus
			if (board.getNumberOfPiecesByColorAndType(side, Piece.BISHOP) == 2) {
				score += bishop_pair_value * side_sign;
			}
		}

		return score;
	}

	/**
	 * Computes the value of the possible activity of the pieces, e.g.
	 * centralization,...
	 * 
	 * @param board
	 *            the board to be analyzed
	 * @return the score for the activity of Rook, Bishop, Knight, Queen
	 */
	private int evalPieceActivity(IPosition board) {
		int score = 0;
		Set<Integer> squares;

		for (Side side : Side.values()) {

			// Bishop
			squares = board
					.getOccupiedSquaresByColorAndType(side, Piece.BISHOP);
			if (side == Side.WHITE)
				for (int squ : squares)
					score += piece_activity_b_k[square_to_array_index[squ]];
			else
				for (int squ : squares)
					score -= piece_activity_b_k[63 - square_to_array_index[squ]];

			// Knight
			squares = board
					.getOccupiedSquaresByColorAndType(side, Piece.KNIGHT);
			if (side == Side.WHITE)
				for (int squ : squares)
					score += piece_activity_b_k[square_to_array_index[squ]];
			else
				for (int squ : squares)
					score -= piece_activity_b_k[63 - square_to_array_index[squ]];

			// Rook
			squares = board.getOccupiedSquaresByColorAndType(side, Piece.ROOK);
			if (side == Side.WHITE)
				for (int squ : squares)
					score += piece_activity_r[square_to_array_index[squ]];
			else
				for (int squ : squares)
					score -= piece_activity_r[63 - square_to_array_index[squ]];

			// Queen
			squares = board.getOccupiedSquaresByColorAndType(side, Piece.QUEEN);
			if (side == Side.WHITE)
				for (int squ : squares)
					score += piece_activity_q[square_to_array_index[squ]];
			else
				for (int squ : squares)
					score -= piece_activity_q[63 - square_to_array_index[squ]];

		}
		return score;
	}

	/**
	 * this function evaluates the weak position of an outpost (?), however only
	 * for bishop and knight. If a knight is covered by pawn, the value
	 * increases.
	 * 
	 * @param board
	 *            the board to be analyzed
	 * @return the score w.r.t. weak/ strong positions
	 */
	private int evalWeakPosition(IPosition board) {

		int score = 0;
		Set<Integer> squares;

		for (Side side : Side.values()) {

			// Bishop
			squares = board
					.getOccupiedSquaresByColorAndType(side, Piece.BISHOP);
			if (side == Side.WHITE)
				for (int squ : squares)
					score += weak_positions[square_to_array_index[squ]];
			else
				for (int squ : squares)
					score -= weak_positions[63 - square_to_array_index[squ]];

			// Knight (value get multiplied times the number of pawn covering
			// the knight)
			squares = board
					.getOccupiedSquaresByColorAndType(side, Piece.BISHOP);
			int count = 0;
			if (side == Side.WHITE) {
				for (int squ : squares) {
					for (Direction dir : Direction
							.pawnCapturingDirections(Side.BLACK))
						if (board.getPieceFromBoard(squ + dir.offset) == Piece.PAWN)
							count++;
					score += count * weak_positions[square_to_array_index[squ]];
				}
			} else {
				for (int squ : squares) {
					for (Direction dir : Direction
							.pawnCapturingDirections(Side.WHITE))
						if (board.getPieceFromBoard(squ + dir.offset) == Piece.PAWN)
							count++;
					score -= count
							* weak_positions[63 - square_to_array_index[squ]];
				}
			}

		}

		return score;
	}
}
