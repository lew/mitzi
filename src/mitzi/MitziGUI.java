package mitzi;

import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class MitziGUI extends JFrame implements MouseListener,
		MouseMotionListener {

	private static final long serialVersionUID = -418000626395118246L;

	JLayeredPane layeredPane;
	JPanel chessBoard;
	JLabel chessPiece;
	int xAdjustment;
	int yAdjustment;

	int start_square;
	int end_square;

	private static GameState state = new GameState();

	private static boolean mitzis_turn;
	
	private static Side mitzis_side =null;
	
	Dimension boardSize = new Dimension(800, 800);

	public Object[] options = { "ok" };
	
	public MitziGUI() {
		redraw();
	}

	private void redraw() {

		// Use a Layered Pane for this this application
		layeredPane = new JLayeredPane();

		layeredPane.setPreferredSize(boardSize);
		layeredPane.addMouseListener(this);
		layeredPane.addMouseMotionListener(this);

		// Add a chess board to the Layered Pane
		chessBoard = new JPanel();
		layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
		chessBoard.setLayout(new GridLayout(8, 8));
		chessBoard.setPreferredSize(boardSize);
		chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

		// Color it b/w
		Color black = Color.getHSBColor((float) 0.10, (float) 0.40,
				(float) 0.80);
		Color white = Color.getHSBColor((float) 0.15, (float) 0.13,
				(float) 0.98);
		for (int i = 0; i < 64; i++) {
			JPanel square = new JPanel(new BorderLayout());
			chessBoard.add(square);
			square.setBackground((i + i / 8) % 2 == 0 ? white : black);
		}

		getContentPane().removeAll();
		getContentPane().add(layeredPane);
	}

	private int getSquare(int x, int y) {
		x = x / 100 + 1;
		y = (800 - y) / 100 + 1;
		return x * 10 + y;
	}

	private Component squareToComponent(int squ) {

		int row = 8 - squ % 10;
		int col = ((int) squ / 10) - 1;

		Component c = chessBoard.getComponent(row * 8 + col);
		return c;
	}

	public void setToFEN(String fen) {

		redraw();

		JPanel panel;

		String[] fen_parts = fen.split(" ");

		// populate the squares
		String[] fen_rows = fen_parts[0].split("/");
		char[] pieces;
		for (int row = 0; row < 8; row++) {
			int offset = 0;
			for (int column = 0; column + offset < 8; column++) {
				pieces = fen_rows[row].toCharArray();
				int square = row * 8 + column + offset;
				JLabel piece;
				switch (pieces[column]) {
				case 'P':
					piece = new JLabel("♙");
					break;
				case 'R':
					piece = new JLabel("♖");
					break;
				case 'N':
					piece = new JLabel("♘");
					break;
				case 'B':
					piece = new JLabel("♗");
					break;
				case 'Q':
					piece = new JLabel("♕");
					break;
				case 'K':
					piece = new JLabel("♔");
					break;
				case 'p':
					piece = new JLabel("♟");
					break;
				case 'r':
					piece = new JLabel("♜");
					break;
				case 'n':
					piece = new JLabel("♞");
					break;
				case 'b':
					piece = new JLabel("♝");
					break;
				case 'q':
					piece = new JLabel("♛");
					break;
				case 'k':
					piece = new JLabel("♚");
					break;
				default:
					piece = new JLabel("");
					offset += Character.getNumericValue(pieces[column]) - 1;
					break;
				}
				panel = (JPanel) chessBoard.getComponent(square);
				piece.setFont(new Font("Serif", Font.PLAIN, 100));
				panel.add(piece);
			}
		}
		chessBoard.updateUI();
	}

	public void mousePressed(MouseEvent e) {
		chessPiece = null;
		Component c = chessBoard.findComponentAt(e.getX(), e.getY());

		if (c instanceof JPanel)
			return;
		Point parentLocation = c.getParent().getLocation();
		xAdjustment = parentLocation.x - e.getX();
		yAdjustment = parentLocation.y - e.getY();
		start_square = getSquare(e.getX(), e.getY());
		chessPiece = (JLabel) c;
		chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
		chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
		layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);
	}

	// Move the chess piece around
	public void mouseDragged(MouseEvent me) {
		if (chessPiece == null)
			return;
		chessPiece
				.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
	}

	// Drop the chess piece back onto the chess board
	public void mouseReleased(MouseEvent e) {
		if (chessPiece == null)
			return;
		chessPiece.setVisible(false);
		end_square = getSquare(e.getX(), e.getY());

		// check for promotion
		IMove move;
		if (state.getPosition().getPieceFromBoard(start_square) == Piece.PAWN
				&& (SquareHelper.getRow(end_square) == 8 || SquareHelper
						.getRow(end_square) == 1)) {
			move = new Move(start_square, end_square, askPromotion());
		} else {
			move = new Move(start_square, end_square);
		}
		
		//if its not your turn, you are not allowed to do anything.
		if(mitzis_side == state.getPosition().getActiveColor())
		{
			Container parent = (Container) squareToComponent(start_square);
			parent.add(chessPiece);
			chessPiece.setVisible(true);
			return;
		}
		
		//try to do move
		try {
			state.doMove(move);
		} catch (IllegalArgumentException ex) {
			Container parent = (Container) squareToComponent(start_square);
			parent.add(chessPiece);
			chessPiece.setVisible(true);
			return;
		}
		

		IPosition position = state.getPosition();
		setToFEN(position.toFEN());
		if (state.getPosition().isMatePosition()) {
			JOptionPane.showOptionDialog(this,
					"You Won!","Information",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			return;
		}
		if (state.getPosition().isStaleMatePosition()) {
			JOptionPane.showOptionDialog(this,
					"Draw","Information",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			return;
		}
		
		mitzis_turn = true;
	}
	
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	private Piece askPromotion() {
		Object[] options = { "Queen", "Rook", "Bishop", "Knight" };
		int n = JOptionPane.showOptionDialog(this,
				"Which piece do you want to promote to?", "Pawn Promotion",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		if (n == 0) {
			return Piece.QUEEN;
		} else if (n == 1) {
			return Piece.ROOK;
		} else if (n == 2) {
			return Piece.BISHOP;
		} else {
			return Piece.KNIGHT;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new MitziGUI();
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setTitle("Mitzi GUI");

		MitziGUI gui = (MitziGUI) frame;
		Object[] choice = { "You", "Mitzi" };
		
		String initialFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		state.setToFEN(initialFEN);
		gui.setToFEN(initialFEN);
		int n = JOptionPane.showOptionDialog(frame,
				"Who should start","Question",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				choice, choice[0]);
		
		if(n==0)
		{
			mitzis_turn = false;
			mitzis_side = Side.BLACK;
		}
		else
		{
			mitzis_turn = true;
			mitzis_side = Side.WHITE;
		}
		IBrain mitzi = new MitziBrain();
		IMove move;
		while (true) {
			System.out.print("");
			if(mitzis_turn) {
				// Mitzis turn
				
				mitzi.set(state);
				move = mitzi.search(5000, 5000, 6, true, null);
				state.doMove(move);
				
				gui.setToFEN(state.getPosition().toFEN());
				if (state.getPosition().isMatePosition()) {
					JOptionPane.showOptionDialog(frame,
							"Mitzi Won!","Information",
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
							gui.options, gui.options[0]);
					return;
				}
				if (state.getPosition().isStaleMatePosition()) {
					JOptionPane.showOptionDialog(frame,
							"Draw!","Information",
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
							gui.options, gui.options[0]);
					return;
				}
				mitzis_turn=false;
			}
		}
	}
}
