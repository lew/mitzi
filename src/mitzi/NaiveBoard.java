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

	// squares c1, g1, c8 and g8 in ICCF numeric notation
	// do not change the squares' order or bad things will happen!
	private int[] castling = new int[4];

	private int en_passant_target;

	private int active_color;

	private NaiveBoard returnCopy() {
		NaiveBoard newBoard = new NaiveBoard();

		newBoard.active_color = active_color;
		newBoard.en_passant_target = en_passant_target;
		newBoard.full_move_clock = full_move_clock;
		newBoard.half_move_clock = half_move_clock;
		System.arraycopy(castling, 0, newBoard.castling, 0, 4);

		for (int i = 2; i < 11; i++)
			System.arraycopy(board[i], 0, newBoard.board[i], 0, 12);

		return newBoard;
	}

	private int getFromBoard(int square) {
		int row = SquareHelper.getRow(square);
		int column = SquareHelper.getColumn(square);
		return board[10 - row][1 + column];
	}

	private void setOnBoard(int square, int piece_value) {
		int row = SquareHelper.getRow(square);
		int column = SquareHelper.getColumn(square);
		board[10 - row][1 + column] = piece_value;
	}

	@Override
	public void setToInitial() {
		board = initial_board;

		full_move_clock = 1;

		half_move_clock = 0;

		castling[0] = 31;
		castling[1] = 71;
		castling[2] = 38;
		castling[3] = 78;

		en_passant_target = -1;

		active_color = PieceHelper.WHITE;
	}

	@Override
	public void setToFEN(String fen) {
		// TODO Auto-generated method stub

	}

	@Override
	public NaiveBoard doMove(IMove move) {

		NaiveBoard newBoard = this.returnCopy();

		int src = move.getFromSquare();
		int dest = move.getToSquare();

		// if promotion
		if (move.getPromotion() != 0) {
			newBoard.setOnBoard(src, 0);
			newBoard.setOnBoard(dest, move.getPromotion());

			newBoard.half_move_clock = 0;

		}
		// If rochade
		else if (PieceHelper.pieceType(src) == PieceHelper.KNIGHT
				&& Math.abs((src - dest)) == 20) {
			newBoard.setOnBoard(dest,
					PieceHelper.pieceValue(PieceHelper.KING, active_color));
			newBoard.setOnBoard(src, 0);
			newBoard.setOnBoard((src + dest) / 2,
					PieceHelper.pieceValue(PieceHelper.ROOK, active_color));
			if (SquareHelper.getColumn(dest) == 3)
				newBoard.setOnBoard(src - 40, 0);
			else
				newBoard.setOnBoard(src + 30, 0);

			newBoard.half_move_clock++;
			
		}
		// If en passant
		else if (PieceHelper.pieceType(src) == PieceHelper.PAWN
				&& dest == this.getEnPassant()) {
			newBoard.setOnBoard(dest,
					PieceHelper.pieceValue(PieceHelper.PAWN, active_color));
			newBoard.setOnBoard(src, 0);
			if (active_color == PieceHelper.WHITE)
				newBoard.setOnBoard(dest - 1, 0);
			else
				newBoard.setOnBoard(dest + 1, 0);

			newBoard.half_move_clock = 0;
		}
		// Usual move
		else {
			newBoard.setOnBoard(dest, this.getFromBoard(src));
			newBoard.setOnBoard(src, 0);

			if (this.getFromBoard(dest) != 0
					|| PieceHelper.pieceType(this.getFromBoard(src)) == PieceHelper.PAWN)
				newBoard.half_move_clock = 0;
			else
				newBoard.half_move_clock++;

		}

		// Change active_color after move
		newBoard.active_color = PieceHelper.pieceOppositeColor(this
				.getFromBoard(src)); 
		if (active_color == PieceHelper.BLACK)
			newBoard.full_move_clock++;

		// Update en_passant
		if (PieceHelper.pieceType(this.getFromBoard(src)) == PieceHelper.PAWN
				&& Math.abs(dest - src) == 2)
			newBoard.en_passant_target = (dest + src) / 2;
		else
			newBoard.en_passant_target = -1;
		
		// Update casteling
		if(PieceHelper.pieceType(this.getFromBoard(src))==PieceHelper.KING)
			if(active_color==PieceHelper.WHITE && src== 51){
				newBoard.castling[0]=-1;
				newBoard.castling[1]=-1;
			}
			else if(active_color==PieceHelper.BLACK && src== 58){
				newBoard.castling[2]=-1;
				newBoard.castling[3]=-1;
			}
		else if(PieceHelper.pieceType(this.getFromBoard(src))==PieceHelper.ROOK)
			if(active_color==PieceHelper.WHITE)
			{
				if(src==81)
					newBoard.castling[1]=-1;
				else if(src==11)
					newBoard.castling[0]=-1;
			}
			else
			{
				if(src==88)
					newBoard.castling[3]=-1;
				else if(src==18)
					newBoard.castling[2]=-1;
			}
				

		return newBoard;
	}

	@Override
	public int getEnPassant() {
		return en_passant_target;
	}

	@Override
	public boolean canCastle(int king_to) {
		if ((king_to == 31 && castling[0] != 0)
				|| (king_to == 71 && castling[1] != 0)
				|| (king_to == 38 && castling[2] != 0)
				|| (king_to == 78 && castling[3] != 0)) {
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
	public Set<IMove> getPossibleMovesFrom(int square) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IMove> getPossibleMovesTo(int square) {
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
