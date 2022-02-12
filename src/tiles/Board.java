package tiles;

import static tiles.Static.MINES;
import static tiles.Static.cols;
import static tiles.Static.mines;
import static tiles.Static.rows;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import window.Launcher;
import window.Window;

public class Board {

	public static Image img_hidden;
	public static Image img_flag;
	public Tile[][] tiles;

	int w, h;
	private BufferedImage img_bomb;
	private BufferedImage img_bomb_B;
	private BufferedImage img_won;
	private BufferedImage img_lost;
	private BufferedImage[] tiles_img;

	public int score;
	public boolean canPlay = true;

	public ImageIcon icon_won;
	public ImageIcon icon_lost;

	Launcher game;
	public int toScore;
	public int flags;

	URL getFile(String name) {
		return getClass().getResource("/" + name);
	}

	public Board(Launcher game, int w, int h) {
		this.game = game;
		this.w = w;
		this.h = h;
		try {
			img_bomb = ImageIO.read(getFile("bomb.png"));
			img_bomb_B = ImageIO.read(getFile("bombB.png"));
			img_won = ImageIO.read(getFile("icon_won.png"));
			img_lost = ImageIO.read(getFile("icon_lost.png"));
			img_hidden = ImageIO.read(getFile("hidden.png"));
			img_flag = ImageIO.read(getFile("flag.png"));
			tiles_img = new BufferedImage[9];
			for (int i = 0; i <= 8; i++) {
				tiles_img[i] = ImageIO.read(getFile(i + ".png"));
			}
			icon_won = new ImageIcon(img_won);
			icon_lost = new ImageIcon(img_lost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		tiles = new Tile[h][w];
		reset();
	}

	public void reset() {
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				tiles[j][i] = new Tile(i, j);
			}
		}
		score = 0;
		canPlay = true;
		flags = 0;
		Launcher.setTitle(flags);
	}

	public void generateBoard(int b, int c) {
		int no_mines = 0;
		while (no_mines < mines) {
			int rCol = getRndInteger(0, w);
			int rRow = getRndInteger(0, h);
			if (rCol == b && rRow == c) {
				continue;
			}
			Tile tile = tiles[rRow][rCol];
			if (tile.status == -1) {
				continue;
			}
			tile.status = -1;
			no_mines++;
		}

		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				if (tiles[j][i].status != -1) {
					tiles[j][i].status = tiles[j][i].calculate(MINES, this);
				}
			}
		}

		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				if (tiles[j][i].status == -1) {
					tiles[j][i].img = img_bomb;
				} else {
					var st = tiles[j][i].status;
					tiles[j][i].img = tiles_img[st];
				}
			}
		}
	}

	public void update() {
		for (int j = 0; j < tiles.length; j++) {
			for (int i = 0; i < tiles[0].length; i++) {
				tiles[j][i].update();
			}
		}
	}

	public void render(Graphics2D g) {
		for (int j = 0; j < tiles.length; j++) {
			for (int i = 0; i < tiles[0].length; i++) {
				tiles[j][i].render(g);
			}
		}
	}

	public int getRndInteger(int min, int max) {
		return (int) (Math.floor(Math.random() * (max - min)) + min);
	}

	public void clearTheVoid(int i, int j) {
		if (!(j >= 0 && i >= 0 && j <= rows - 1 && i <= cols - 1)) {
			return;
		}
		Tile tile = tiles[j][i];
		if (tile.status == -1) {
			return;
		}
		if (tile.status != 0) {
			if (!tile.isVisible) {
				score++;
			}
			tile.isVisible = true;
			return;
		}
		if (tile.isVisible == true) {
			return;
		}
		if (!tile.isVisible) {
			score++;
		}
		tile.isVisible = true;
		clearTheVoid(i + 1, j);
		clearTheVoid(i - 1, j);
		clearTheVoid(i, j + 1);
		clearTheVoid(i, j - 1);
		clearTheVoid(i + 1, j + 1);
		clearTheVoid(i - 1, j - 1);

		clearTheVoid(i + 1, j - 1);
		clearTheVoid(i - 1, j + 1);
	}

	public void clearTheBoard(int i, int j) {
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (!(x == 0 && y == 0)) {
					int m = j + x;
					int n = i + y;
					if ((m >= 0 && n >= 0 && m <= rows - 1 && n <= cols - 1)) {
						Tile tile = tiles[m][n];
						if (tile.status == -1 && tile.isFlag == false) {
							lost(tile);
							tile.isVisible = true;
						} else if (tile.status == 0) {
							clearTheVoid(tile.i, tile.j);
							continue;
						} else {
							if (!tile.isFlag) {
								if (!tile.isVisible) {
									score++;
								}
								tile.isVisible = true;
							}
						}
					}
				}
			}
		}
	}

	public void lost(Tile t) {
		t.img = img_bomb_B;
		String message = "You have Lost\nPress 'R' to Replay.";
		String title = "LOST";
		showMessage(message, title, icon_lost);
	}

	public void won() {
		String message = "You have Won!!!\nPress 'R' to Replay.";
		String title = "WON";
		showMessage(message, title, icon_won);
	}

	public void showMessage(String message, String title, ImageIcon icon) {
		for (int j = 0; j < tiles.length; j++) {
			for (int i = 0; i < tiles[0].length; i++) {
				Tile tile = tiles[j][i];
				tile.isVisible = true;
			}
		}
		JOptionPane.showMessageDialog(Window.frame, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
		canPlay = false;
	}

}
