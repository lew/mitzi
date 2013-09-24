package mitzi;

import java.util.Set;

public class NaiveBoard implements IBoard {

	protected static int[][] initial_board = {
			{ 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00 },
			{ 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00 },
			{ 00, 00, 12, 13, 14, 15, 16, 14, 13, 12, 00, 00 },
			{ 00, 00, 11, 11, 11, 11, 11, 11, 11, 11, 00, 00 },
			{ 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00 },
			{ 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00 },
			{ 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00 },
			{ 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00 },
			{ 00, 00, 01, 01, 01, 01, 01, 01, 01, 01, 00, 00 },
			{ 00, 00, 02, 03, 04, 05, 06, 04, 03, 02, 00, 00 },
			{ 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00 },
			{ 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00 } };

	private int[][] board = new int[12][12];

	private int full_move_clock;

	private int half_move_clock;

	private int[] castling = new int[4];

	private int en_passant_target;

	private int active_color;

	@Override
	public void setToInitial() {
		board = initial_board;

		full_move_clock = 1;

		half_move_clock = 0;

		// squares c1, g1, c8 and g8
		castling[0] = 2;
		castling[1] = 6;
		castling[2] = 58;
		castling[3] = 62;

		en_passant_target = -1;

		active_color = PieceHelper.WHITE;
	}

	@Override
	public void setToFEN(String fen) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBoard doMove(IMove move) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEnPassant() {
		return en_passant_target;
	}

	@Override
	public boolean canCastle(int king_to) {
		if ((king_to == 2 && castling[0] != 0)
				|| (king_to == 6 && castling[1] != 0)
				|| (king_to == 58 && castling[2] != 0)
				|| (king_to == 62 && castling[3] != 0)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Set<Integer> getOccupiedSquaresByColor(int color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Integer> getOccupiedSquaresByType(int type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Integer> getOccupiedSquaresByColorAndType(int color, int type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfPiecesByColor(int color) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfPiecesByType(int type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfPiecesByColorAndType(int color, int type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<IMove> getPossibleMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IMove> getPossibleMovesFrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IMove> getPossibleMovesTo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCheckPosition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMatePosition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPossibleMove(IMove move) {
		// TODO Auto-generated method stub
		return false;
	}

	public String toString() {
		return toFEN();
	}

	@Override
	public String toFEN() {
		StringBuilder fen = new StringBuilder();

		// piece placement
		for (int row = 2; row < 10; row++) {

			int counter = 0;

			for (int column = 2; column < 10; column++) {
				if (board[row][column] == 0) {
					counter++;
				} else {
					if (counter != 0) {
						fen.append(counter);
						counter = 0;
					}
					fen.append(PieceHelper.toString(board[row][column]));
				}
				if (column == 9 && counter != 0) {
					fen.append(counter);
				}
			}

			if (row != 9) {
				fen.append("/");
			}
		}
		fen.append(" ");

		// active color
		if (active_color == PieceHelper.WHITE) {
			fen.append("w");
		} else {
			fen.append("b");
		}
		fen.append(" ");

		// castling availability
		boolean castle_flag = false;
		if (castling[1] != 0) {
			fen.append("K");
			castle_flag = true;
		}
		if (castling[0] != 0) {
			fen.append("Q");
			castle_flag = true;
		}
		if (castling[3] != 0) {
			fen.append("k");
			castle_flag = true;
		}
		if (castling[2] != 0) {
			fen.append("q");
			castle_flag = true;
		}
		if (!castle_flag) {
			fen.append("-");
		}
		fen.append(" ");

		// en passant target square
		if (en_passant_target == -1) {
			fen.append("-");
		} else {
			fen.append(SquareHelper.toString(en_passant_target));
		}
		fen.append(" ");

		// halfmove clock
		fen.append(half_move_clock);
		fen.append(" ");

		// fullmove clock
		fen.append(full_move_clock);

		return fen.toString();
	}

	@Override
	public int getActiveColor() {
		return active_color;
	}

	@Override
	public int getHalfMoveClock() {
		return half_move_clock;
	}

	@Override
	public int getFullMoveClock() {
		return full_move_clock;
	}

}
