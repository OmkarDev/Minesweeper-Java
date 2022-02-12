package window;

import static tiles.Static.*;
import static tiles.Static.background;
import static tiles.Static.cols;
import static tiles.Static.rows;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import tiles.Board;
import tiles.Tile;

public class Launcher extends Window implements MouseListener, KeyListener {

	private static final long serialVersionUID = 1L;
	Board board;

	public Launcher(String title) {
		super(title);
		cols = 16;
		rows = 16;
		W = 32;
		mines = 40;
		this.height = rows * W;
		this.width = cols * W;
		board = new Board(this, cols, rows);
		board.toScore = cols * rows - mines;
		this.width += outline;
		this.height += outline;
	}

	public void update() {
		board.update();
	}

	public void render(Graphics2D g) {
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		board.render(g);
	}

	public static void main(String[] args) {
		Launcher game = new Launcher("Minesweeper");
		game.setIcon("icon.png");
		game.addMouseListener(game);
		game.addKeyListener(game);
		game.display();
	}

	public void mousePressed(MouseEvent e) {
		int i = e.getX() / W;
		int j = e.getY() / W;
		if (e.getButton() == 1 && board.canPlay) {
			if (board.score == 0) {
				board.generateBoard(i, j);
			}
			Tile tile = board.tiles[j][i];
			if (tile.isFlag == false) {
				if (tile.isVisible == false) {
					if (tile.status == -1) {
						board.lost(tile);
					} else if (tile.status == 0) {
						board.clearTheVoid(i, j);
						board.score--;
					}
					tile.isVisible = true;
					board.score++;
				} else {
					if (tile.status > 0) {
						if (tile.status == tile.calculate(FLAGS, board)) {
							board.clearTheBoard(i, j);
						}
					}
				}

			}

			if (board.score == board.toScore && board.canPlay) {
				board.won();
			}
		}

		if (e.getButton() == 3 && board.canPlay) {
			Tile tile = board.tiles[j][i];
			if (tile.isFlag) {
				tile.isFlag = false;
				board.flags--;
				setTitle(board.flags);
				return;
			}
			if (tile.isVisible == false) {
				tile.isFlag = true;
				board.flags++;
			}
		}
		setTitle(board.flags);
	}

	public static void setTitle(int flags) {
		Window.frame.setTitle("Minesweeper Flags: " + flags + "/" + mines);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_R) {
			board.reset();
		}
		if (e.isControlDown()) {
			if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_W) {
				System.exit(0);
			}
		}
	}

	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
