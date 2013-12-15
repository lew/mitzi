package mitzi;

import static mitzi.MateScores.NEG_INF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 
 * This class computes the value of a board in a proper way, see
 * http://philemon.cycovery.com/site/part2.html for more details.
 * 
 */
public class BoardAnalyzer implements IPositionAnalyzer {

	/**
	 * the square to array index from Position.java
	 */
	protected static int[] square_to_array_index = { 64, 64, 64, 64, 64, 64,
			64, 64, 64, 64, 64, 56, 48, 40, 32, 24, 16, 8, 0, 64, 64, 57, 49,
			41, 33, 25, 17, 9, 1, 64, 64, 58, 50, 42, 34, 26, 18, 10, 2, 64,
			64, 59, 51, 43, 35, 27, 19, 11, 3, 64, 64, 60, 52, 44, 36, 28, 20,
			12, 4, 64, 64, 61, 53, 45, 37, 29, 21, 13, 5, 64, 64, 62, 54, 46,
			38, 30, 22, 14, 6, 64, 64, 63, 55, 47, 39, 31, 23, 15, 7, 64, 64,
			64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64,
			64, 64, 64 };

	/**
	 * the material value of a piece.
	 */
	static private int[] piece_values = { 100, 500, 325, 325, 975, 000 };

	// The following arrays contains the value of a piece on a specific square,
	// always in favor of white. Since the arrays are symmetric w.r.t. the
	// columns, BLACK uses 63-i entry with opposite sign.
	/**
	 * value of squares for bishop and knight, in favor of white
	 */
	static private int[] piece_activity_b_k = { -16, -16, -8, -8, -8, -8, -16,
			-16, -16, -16, -4, -4, -4, -4, -16, -16, -8, 2, 6, 6, 6, 6, 2, -8,
			-8, 2, 6, 6, 6, 6, 2, -8, -8, 2, 4, 4, 4, 4, 2, -8, -8, 2, 2, 2, 2,
			2, 2, -8, -8, -8, 0, 0, 0, 0, -8, -8, -16, -8, -8, -8, -8, -8, -8,
			-16 };
	/**
	 * value of squares for rook, in favor of white
	 */
	static private int[] piece_activity_r = { 0, 0, 4, 6, 6, 4, 0, 0, 0, 0, 4,
			6, 6, 4, 0, 0, 0, 0, 4, 6, 6, 4, 0, 0, 0, 0, 4, 6, 6, 4, 0, 0, 0,
			0, 4, 6, 6, 4, 0, 0, 0, 0, 4, 6, 6, 4, 0, 0, 0, 0, 4, 6, 6, 4, 0,
			0, 0, 0, 4, 6, 6, 4, 0, 0, };

	/**
	 * value of squares for queen, in favor of white
	 */
	static private int[] piece_activity_q = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4,
			5, 5, 4, 0, 0, 0, 2, 4, 10, 10, 4, 2, 0, 0, 2, 10, 12, 12, 10, 2,
			0, -10, 2, 10, 12, 12, 10, 2, -10, -10, -10, 4, 10, 10, 4, -10,
			-10, -10, 2, 8, 8, 8, 8, 2, -10, -10, -8, 0, 0, 0, 0, -8, -10, };

	/**
	 * value of squares, which are weak/strong squares for bishop and knight
	 */
	static private int[] weak_positions = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 8, 12, 12, 8, 0, 0, 0, 2, 12, 16, 16, 12, 2, 0,
			0, 2, 12, 20, 20, 12, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };

	/**
	 * value of squares for white pawns. (not symmetric)
	 */
	static private int[] pawn_positions_w = { 0, 0, 0, 0, 0, 0, 0, 0, 28, 28,
			35, 42, 45, 35, 28, 28, -9, -3, 7, 12, 15, 7, -3, -9, -10, -10, 6,
			9, 10, 6, -11, -10, -11, -11, 4, 5, 6, 2, -11, -11, -11, -11, 0, 0,
			1, 0, -11, -11, -6, -6, 4, 5, 5, 4, -6, -6, 0, 0, 0, 0, 0, 0, 0, 0 };

	/**
	 * value of squares for black pawns. (not symmetric)
	 */
	static private int[] pawn_positions_b = { 0, 0, 0, 0, 0, 0, 0, 0, -6, -6,
			4, 5, 5, 4, -6, -6, -11, -11, 0, 0, 1, 0, -11, -11, -11, -11, 4, 5,
			6, 2, -11, -11, -10, -10, 6, 9, 10, 6, -11, -10, -9, -3, 7, 12, 15,
			7, -3, -9, 28, 28, 35, 42, 45, 35, 28, 28, 0, 0, 0, 0, 0, 0, 0, 0 };

	/**
	 * value of squares for white king, not valid in endgame. (not symmetric)
	 */
	static private int[] king_positions_w = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, -15,
			-10, 0, 0, 5, 10, 18, -8, -3, -8, 23, 10 };

	/**
	 * value of squares for black king, not valid in endgame. (not symmetric)
	 */
	static private int[] king_positions_b = { 5, 10, 18, -8, -3, -8, 23, 10, 0,
			0, 0, -10, -15, -10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	/**
	 * value for twin pawns for different rows.
	 */
	static private int[] twin_pawns = { 0, 0, 1, 2, 3, 4, 7, 0 };

	/**
	 * value for covered pawns for different rows.
	 */
	static private int[] covered_pawns = { 0, 0, 4, 6, 8, 12, 16, 0 };

	/**
	 * value for passed pawns for different rows.
	 */
	static private int[] passed_pawn = { 0, 2, 10, 20, 40, 60, 70, 0 };

	/**
	 * value for passed pawns, where in front of the pawn is a king for
	 * different rows.
	 */
	static private int[] passed_pawn_with_king = { 0, 0, 0, 0, 10, 50, 80, 0 };

	/**
	 * value for blocked passed pawns
	 */
	static private int[] blocked_passed_pawn = { 0, 0, -8, -16, -32, -45, -58,
			0 };

	/**
	 * if not all Bishop and Knight has moved, moving the queen results in
	 * negative score
	 */
	static private int PREMATURE_QUEEN = -17;

	/**
	 * bonus, if the rook is on an open line (no other pawns)
	 */
	static private int ROOK_OPEN_LINE = 20;

	/**
	 * bonus if the rook is on an halfopen line (only opponents pawns)
	 */
	static private int ROOK_HALFOPEN_LINE = 5;

	/**
	 * bonus if the rook is in the 7th row and opponents king is in the 8th or
	 * pawn in the 7th
	 */
	static private int ROOK_7TH_2ND = 25;

	/**
	 * bonus if the previous bonus holds and the 7th row is empty.
	 */
	static private int ROOK_7TH_2ND_ABSOLUTE = 15;

	/**
	 * bonus if a rook covers the other rook, this replaces the ROOK_7TH_2ND and
	 * counts for each rook (both on the 7th row)
	 */
	static private int REINFORCED_ROOK_7TH_2ND = 40;

	/**
	 * bonus if a rook is behind a passed pawn
	 */
	static private int PASSED_ROOK_SUPPORT = 10;

	/**
	 * gives a bonus if both bishops are still available in the endgame
	 */
	static private int ENDGAME_BISHOP_BONUS = 10; // not yet implemented

	/**
	 * bonus/malus if the bishop is caged on he baseline (the pawn in front of
	 * the bishop has moved and the two pawns left and right of the bisop have
	 * not moved )
	 */
	static private int BISHOP_BASELINE_CAGED = -12;

	/**
	 * bonus if a queen is covered on the 7th row by a rook
	 */
	static private int REINFORCING_QUEEN_7TH_2ND = 20;

	/**
	 * The player receives a bonus if the 2 bishops are alive.
	 */
	static private int bishop_pair_value = 25;

	/**
	 * multiple pawns in a columns get a malus.
	 */
	static private int MULTI_PAWN = -10;

	/**
	 * a isolated pawns (no pawn on the neighboring colums) get a malus
	 */
	static private int ISOLATED_PAWN = -20;

	/**
	 * bonus for coverd passed pawns on the 7th row
	 */
	static private int COVERED_PASSED_7TH_PAWN = 90;

	/**
	 * malus if castling is loss (needs to be fixed and optimized)
	 */
	static private int CASTLING_LOSS = -40;

	/**
	 * the number of pieces when the endgame starts (a first draft, needs to be
	 * optimized)
	 */
	static public int ENDGAME_THRESHOLD = 8;

	/**
	 * counts the number of board evaluations in quiesce().
	 */
	static public long eval_counter_seldepth = 0;

	/**
	 * counts the number of found positions in Transposition Table
	 */
	static public long table_counter = 0;

	@Override
	public AnalysisResult eval0(IPosition board) {
		int score = 0;
		// compute all the information needed by the evaluation function once.
		board.cacheOccupiedSquares();

		// Evaluate the pieces
		score += evalPieces(board);

		// Evaluate Pawn Stucture
		score += evalPawns(board);

		// Evaluate Diagonals and lines
		score += evalLinesAndDiagonals(board);

		// Evaluate position - activity
		score += evalPieceActivity(board);

		// Evaluate weak/strong position
		score += evalWeakPosition(board);

		// Evaluate the King's position (not in endgame)
		score += evalKingPos(board);

		AnalysisResult result = new AnalysisResult(score, false, false, 0, 0,
				Flag.EXACT);
		return result;
	}

	@Override
	public AnalysisResult evalBoard(IPosition position, int alpha, int beta)
			throws InterruptedException {
		AnalysisResult result = quiesce(position, alpha, beta);

		// The analysis result should always contain the pure value (not
		// perturbed via side_sign)
		return result;
	}

	/**
	 * Implements Quiescence search to avoid the horizon effect. The function
	 * increase the search depth until no capture is possible, where only
	 * captures are analyzed. The optimal value is found using the negamax
	 * algorithm.
	 * 
	 * @see <a
	 *      href="http://chessprogramming.wikispaces.com/Quiescence+Search">http://chessprogramming.wikispaces.com/Quiescence+Search</a>
	 * 
	 * @param position
	 *            the position to be analyzed
	 * @param alpha
	 *            the alpha value of alpha-beta search
	 * @param beta
	 *            the beta value of alpha-beta search
	 * @return the value of the board ( in favor of white)
	 * 
	 * @throws InterruptedException
	 */
	private AnalysisResult quiesce(IPosition position, int alpha, int beta)
			throws InterruptedException {

		if (Thread.interrupted()) {
			throw new InterruptedException();
		}

		int side_sign = Side.getSideSign(position.getActiveColor());

		// Cache lookup
		AnalysisResult entry = ResultCache.getResult(position);
		if (entry != null) {
			table_counter++;
			if (entry.flag == Flag.EXACT) {
				AnalysisResult new_entry = entry.tinyCopy();
				return new_entry;
			} else if (entry.flag == Flag.LOWERBOUND)
				alpha = Math.max(alpha, entry.score * side_sign);
			else if (entry.flag == Flag.UPPERBOUND)
				beta = Math.min(beta, entry.score * side_sign);

			if (alpha >= beta) {
				AnalysisResult new_entry = entry.tinyCopy();
				return new_entry;
			}
		}

		// generate moves
		List<IMove> moves = position.getPossibleMoves();

		// check for mate and stalemate
		if (moves.isEmpty()) {
			eval_counter_seldepth++;
			if (position.isCheckPosition()) {
				return new AnalysisResult(NEG_INF * side_sign, false, false, 0,
						0, Flag.EXACT);
			} else {
				return new AnalysisResult(0, true, false, 0, 0, Flag.EXACT);
			}
		}

		// evaluation of the current board.
		AnalysisResult standing_pat = eval0(position);
		eval_counter_seldepth++;

		int negaval = standing_pat.score * side_sign;

		// alpha beta cutoff
		if (negaval >= beta)
			return standing_pat;
		alpha = Math.max(alpha, negaval);

		// Generate possible Captures
		List<IMove> captures = position.generateCaptures();
		if (captures.size()>1) {
			// Generate MoveComperator
			//BasicMoveComparator move_comparator = new BasicMoveComparator(position);	
			
			CaptureComparator move_comparator = new CaptureComparator(position);
			Collections.sort(captures,
					Collections.reverseOrder(move_comparator));
		}

		AnalysisResult result = null;
		int best_value = NEG_INF;

		for (IMove move : captures) {

			position.doMove(move);
			AnalysisResult result_temp = quiesce(position, -beta, -alpha);
			position.undoMove(move);

			negaval = result_temp.score * side_sign;

			// find the best result
			if (negaval > best_value) {
				best_value = negaval;
				result = result_temp;
			}

			// cut-off
			if (negaval >= beta) {
				result.plys_to_seldepth++;
				return result;
			}
			alpha = Math.max(alpha, negaval);
		}

		// the standing_pat was computed in this depth
		if (result == null)
			return standing_pat;

		// the result comes from a depth below
		result.plys_to_seldepth++;
		return result;

	}

	/**
	 * Evaluates only the material value of the board.
	 * 
	 * @param board
	 *            the actual board
	 * @return the material value ( in favor of white)
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
	 * @return the score for the activity of Rook, Bishop, Knight, Queen ( in
	 *         favor of white)
	 */
	private int evalPieceActivity(IPosition board) {
		int score = 0;
		Set<Integer> squares;
		boolean queen_moved_last, queen_startpos;

		for (Side side : Side.values()) {
			int side_sign = Side.getSideSign(side);
			queen_moved_last = true;
			queen_startpos = false;

			// Queen
			squares = board.getOccupiedSquaresByColorAndType(side, Piece.QUEEN);
			if (side == Side.WHITE)
				for (int squ : squares)
					score += piece_activity_q[square_to_array_index[squ]];
			else
				for (int squ : squares)
					score -= piece_activity_q[63 - square_to_array_index[squ]];

			if ((squares.contains(SquareHelper.getSquare(
					SquareHelper.getRowForSide(side, 1), 4))))
				queen_startpos = true;

			// Bishop
			squares = board
					.getOccupiedSquaresByColorAndType(side, Piece.BISHOP);
			if (side == Side.WHITE)
				for (int squ : squares)
					score += piece_activity_b_k[square_to_array_index[squ]];
			else
				for (int squ : squares)
					score -= piece_activity_b_k[63 - square_to_array_index[squ]];

			if (!queen_startpos
					&& (squares.contains(SquareHelper.getSquare(
							SquareHelper.getRowForSide(side, 1), 3)) || squares
							.contains(SquareHelper.getSquare(
									SquareHelper.getRowForSide(side, 1), 3))))
				queen_moved_last = false;

			// Knight
			squares = board
					.getOccupiedSquaresByColorAndType(side, Piece.KNIGHT);
			if (side == Side.WHITE)
				for (int squ : squares)
					score += piece_activity_b_k[square_to_array_index[squ]];
			else
				for (int squ : squares)
					score -= piece_activity_b_k[63 - square_to_array_index[squ]];

			if (!queen_startpos
					&& (squares.contains(SquareHelper.getSquare(
							SquareHelper.getRowForSide(side, 1), 2)) || squares
							.contains(SquareHelper.getSquare(
									SquareHelper.getRowForSide(side, 1), 2))))
				queen_moved_last = false;

			// Rook
			squares = board.getOccupiedSquaresByColorAndType(side, Piece.ROOK);
			if (side == Side.WHITE)
				for (int squ : squares)
					score += piece_activity_r[square_to_array_index[squ]];
			else
				for (int squ : squares)
					score -= piece_activity_r[63 - square_to_array_index[squ]];

			if (!queen_startpos && !queen_moved_last)
				score += side_sign * PREMATURE_QUEEN;

		}
		return score;
	}

	/**
	 * this function evaluates the weak position of an outpost, however only for
	 * bishop and knight. If a knight is covered by pawn, the value increases.
	 * 
	 * @param board
	 *            the board to be analyzed
	 * @return the score w.r.t. weak/ strong positions ( in favor of white)
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
			// the knight, if no cover no bonus is added)
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

	/**
	 * Evaluates if rooks occupies open/halfopen lines, if they occupies the
	 * 7-th row or are covered there and if the bishop is caged on the baseline
	 * (the pawn in front of him has moved the the neighboring ones are here)
	 * 
	 * @param board
	 *            the board to be evaluated
	 * @return the score ( in favor of white)
	 */
	private int evalLinesAndDiagonals(IPosition board) {
		int score = 0;

		Set<Integer> squares_rook, squares_bishop;
		Piece p;
		Side s;

		for (Side side : Side.values()) {
			int side_sign = Side.getSideSign(side);
			Side opp_side = Side.getOppositeSide(side);

			squares_rook = board.getOccupiedSquaresByColorAndType(side,
					Piece.ROOK);

			// Open line and halfopen line bonus
			for (int square : squares_rook) {

				boolean half_open = true;
				boolean open = true;
				List<Integer> squares = new ArrayList<Integer>(
						SquareHelper.getAllSquaresInDirection(square,
								Direction.NORTH));
				squares.addAll(SquareHelper.getAllSquaresInDirection(square,
						Direction.SOUTH));

				for (int squ : squares) {
					if (board.getPieceFromBoard(squ) == Piece.PAWN) {
						if (board.getSideFromBoard(squ) == board
								.getActiveColor()) {
							half_open = false;
							open = false;
							break;
						} else
							open = false;
					}
				}
				if (half_open && open)
					score += side_sign * ROOK_OPEN_LINE;
				else if (half_open)
					score += side_sign * ROOK_HALFOPEN_LINE;

				// 7th || 2nd line bonus
				if (SquareHelper.getRow(square) == SquareHelper.getRowForSide(
						side, 7)) {

					boolean rook_7 = true;
					boolean rook_7_abs = true;
					boolean rook_7_cover_r = false;
					boolean rook_7_cover_q = false;
					boolean emty_direction = true;

					// Check all squares at the west side of the rook
					for (int squ : SquareHelper.getAllSquaresInDirection(
							square, Direction.WEST)) {
						p = board.getPieceFromBoard(squ);
						s = board.getSideFromBoard(squ);
						if (p == Piece.PAWN && s == opp_side) {
							rook_7 = false;
							rook_7_abs = false;
							break;
						} else if (emty_direction && p != null) {
							emty_direction = false;
							rook_7_abs = false;
							if (p == Piece.ROOK && s == side)
								rook_7_cover_r = true;
							else if (p == Piece.QUEEN && s == side)
								rook_7_cover_q = true;
						}

					}

					// Check all squares at the west side of the rook
					emty_direction = true;
					for (int squ : SquareHelper.getAllSquaresInDirection(
							square, Direction.EAST)) {
						p = board.getPieceFromBoard(squ);
						s = board.getSideFromBoard(squ);
						if (rook_7 && p == Piece.PAWN && s == opp_side) {
							rook_7 = false;
							rook_7_abs = false;
							break;
						} else if (emty_direction && p != null) {
							rook_7_abs = false;
							emty_direction = false;
							if (p == Piece.ROOK && s == side)
								rook_7_cover_r = true;
							else if (p == Piece.QUEEN && s == side)
								rook_7_cover_q = true;
						}

					}

					int king_pos = board.getKingPos(opp_side);
					if (SquareHelper.getRow(king_pos) == SquareHelper
							.getRowForSide(side, 8))
						rook_7 = true;

					if (rook_7)
						score += side_sign * ROOK_7TH_2ND;
					if (rook_7_abs)
						score += side_sign * ROOK_7TH_2ND_ABSOLUTE;
					if (rook_7_cover_r)
						score += side_sign * REINFORCED_ROOK_7TH_2ND;
					if (rook_7_cover_q)
						score += side_sign * REINFORCING_QUEEN_7TH_2ND;
				}

			}
			squares_bishop = board.getOccupiedSquaresByColorAndType(side,
					Piece.BISHOP);
			int row_s = SquareHelper.getRowForSide(side, 1);
			boolean bishop_caged = false;
			for (int square : squares_bishop)
				if ((square == SquareHelper.getSquare(row_s, 3) || square == SquareHelper
						.getSquare(row_s, 6))
						&& (board.getPieceFromBoard(square
								+ Direction.pawnDirection(side).offset) == Piece.PAWN && board
								.getSideFromBoard(square
										+ Direction.pawnDirection(side).offset) == side)) {
					bishop_caged = true;
					for (Direction dir : Direction
							.pawnCapturingDirections(side)) {
						if (board.getPieceFromBoard(square + dir.offset) != Piece.PAWN
								|| board.getSideFromBoard(square + dir.offset) != side)
							bishop_caged = false;
					}
				}
			if (bishop_caged == true)
				score += side_sign * BISHOP_BASELINE_CAGED;
		}
		return score;
	}

	/**
	 * evaluates the pawn structure. Checks for covered pawns, passed pawns,
	 * isolated pawns, twin pawns... value dependent of the row
	 * 
	 * @param position
	 *            the current position
	 * @return the value of the pawn structure in favor of white
	 */
	private int evalPawns(IPosition position) {

		int score = 0;
		int row, col, col_2, row_side;
		boolean isolated, covered, passed;
		for (Side side : Side.values()) {
			int side_sign = Side.getSideSign(side);
			Side opp_side = Side.getOppositeSide(side);
			Set<Integer> squares_pawn = position
					.getOccupiedSquaresByColorAndType(side, Piece.PAWN);
			Set<Integer> squares_pawn_opp = position
					.getOccupiedSquaresByColorAndType(opp_side, Piece.PAWN);

			if (side == Side.WHITE)
				for (int squ : squares_pawn)
					score += pawn_positions_w[square_to_array_index[squ]];
			else
				for (int squ : squares_pawn)
					score -= pawn_positions_b[square_to_array_index[squ]];

			for (int squ_1 : squares_pawn) {
				row = SquareHelper.getRow(squ_1);
				col = SquareHelper.getColumn(squ_1);

				row_side = SquareHelper.getRowForSide(side, row);

				isolated = true;
				covered = false;

				for (int squ_2 : squares_pawn) {
					// dont check the pawn with himself
					if (squ_2 == squ_1)
						continue;

					col_2 = SquareHelper.getColumn(squ_2);
					if (col == col_2)
						// add malus for multiple pawns in the same line.
						// TODO: maybe dont increase malus for triple,.. pawns
						score += side_sign * MULTI_PAWN;
					else if (col == col_2 + 1 || col == col_2 - 1) {
						isolated = false;

						if (row == SquareHelper.getRow(squ_2))
							// add bonus for twinpawns
							score += side_sign * twin_pawns[row_side];
						else if (row == SquareHelper.getRow(squ_2
								- Direction.pawnDirection(side).offset)) {
							// add bonus for covered pawns
							// TODO: maybe dont increase bonus for pawns covered
							// by 2 pawns
							covered = true;
							score += side_sign * covered_pawns[row_side];
						}
					}

				}

				if (isolated == true)
					score += side_sign * ISOLATED_PAWN;

				// check if a pawn is passed
				passed = true;
				for (int squ_2 : squares_pawn_opp) {
					col_2 = SquareHelper.getColumn(squ_2);
					if (col == col_2 || col == col_2 + 1 || col == col_2 - 1) {
						passed = false;
						break;
					}

				}
				if (passed == true) {
					// check if a passed pawn is blocked
					for (int squ_2 : squares_pawn_opp) {
						if (squ_1 + Direction.pawnDirection(side).offset == squ_2) {
							score += side_sign * blocked_passed_pawn[row_side];
							break;
						}
					}

					// check if a passed pawn is covered by a king (the king
					// should be in front of the pawn)
					for (Direction dir : Direction
							.pawnCapturingDirections(side))
						if (squ_1 + dir.offset == position.getKingPos(side))
							score += side_sign
									* passed_pawn_with_king[row_side];

					// add the bonus for a passed pawn
					score += side_sign * passed_pawn[row_side];

					// additional bonus for covered passed pawn
					if (covered == true
							&& row == SquareHelper.getRowForSide(side, 7))
						score += side_sign * COVERED_PASSED_7TH_PAWN;

					// if a rook is behind a passed pawn
					// TODO: check if it better do add the bonus is a rook is on
					// the same line (behind the pawn)
					if (position.getPieceFromBoard(squ_1
							- Direction.pawnDirection(side).offset) == Piece.ROOK
							&& position.getSideFromBoard(squ_1
									- Direction.pawnDirection(side).offset) == side)
						score += side_sign * PASSED_ROOK_SUPPORT;
				}

			}

		}
		return score;
	}

	/**
	 * draft of king's position evaluation function.
	 * 
	 * @param position
	 *            the current position
	 * @return the score
	 */
	private int evalKingPos(IPosition position) {
		int score = 0;
		int count_fig = position.getNumberOfPiecesByColor(Side.WHITE)
				+ position.getNumberOfPiecesByColor(Side.BLACK);
		if (count_fig > ENDGAME_THRESHOLD)
			for (Side side : Side.values()) {
				int side_sign = Side.getSideSign(side);
				int row_1 = SquareHelper.getRowForSide(side, 1);
				if (side == Side.WHITE)
					score += side_sign
							* king_positions_w[square_to_array_index[position
									.getKingPos(side)]];
				else
					score += side_sign
							* king_positions_b[square_to_array_index[position
									.getKingPos(side)]];

				// TODO: this dont work as it should... needs to be fixed (only
				// should
				// give penalty if during search castling is lost)
				if (!position.canCastle(SquareHelper.getSquare(row_1, 3))
						|| !position
								.canCastle(SquareHelper.getSquare(row_1, 7)))
					score += side_sign * CASTLING_LOSS;

			}

		return score;
	}
}
