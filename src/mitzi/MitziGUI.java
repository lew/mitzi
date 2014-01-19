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

	public MitziGUI() {
		Dimension boardSize = new Dimension(800, 800);
		// Use a Layered Pane for this this application
		layeredPane = new JLayeredPane();
		getContentPane().add(layeredPane);
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

	}

	public void setToFEN(String fen) {
		JPanel panel = (JPanel) chessBoard.getComponent(0);

		String[] fen_parts = fen.split(" ");

		// populate the squares
		String[] fen_rows = fen_parts[0].split("/");
		char[] pieces;
		for (int row = 0; row < 8; row++) {
			int offset = 0;
			for (int column = 0; column + offset < 8; column++) {
				pieces = fen_rows[row].toCharArray();
				int square = (row + offset) * 8 + column;
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
	}

	public void mousePressed(MouseEvent e) {
		chessPiece = null;
		Component c = chessBoard.findComponentAt(e.getX(), e.getY());
		if (c instanceof JPanel)
			return;
		Point parentLocation = c.getParent().getLocation();
		xAdjustment = parentLocation.x - e.getX();
		yAdjustment = parentLocation.y - e.getY();
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
		Component c = chessBoard.findComponentAt(e.getX(), e.getY());
		if (c instanceof JLabel) {
			Container parent = c.getParent();
			parent.remove(0);
			parent.add(chessPiece);
		} else {
			Container parent = (Container) c;
			parent.add(chessPiece);
		}
		chessPiece.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
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
		String initialFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		gui.setToFEN(initialFEN);
	}
}
