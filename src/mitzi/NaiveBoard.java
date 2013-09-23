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

	@Override
	public void setToInitial() {
		board = initial_board;
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
	public boolean canEnPassant(int hit_field) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canCastle(int king_to) {
		// TODO Auto-generated method stub
		return false;
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
	public int getNumberOfFiguresByColor(int color) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfFiguresByType(int type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfFiguresByColorAndType(int color, int type) {
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
		//TODO output active color, halfmoves, etc
		StringBuilder fen = new StringBuilder();

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
					fen.append(FigureHelper.toString(board[row][column]));
				}
				if (column == 9 && counter != 0) {
					fen.append(counter);
				}
			}

			if (row != 9) {
				fen.append("/");
			}
		}

		return fen.toString();
	}

}
