package tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static tiles.Static.*;

public class Tile {

	public int i, j;

	public boolean isVisible = false;
	public boolean isFlag = false;

	public int status = 0;
	public BufferedImage img;

	public Tile(int i, int j) {
		this.i = i;
		this.j = j;
	}

	void update() {

	}

	void render(Graphics2D g) {
		if (isVisible == false) {
			if (isFlag == true) {
				g.drawImage(Board.img_flag, i * W + outline, j * W + outline, W - outline, W - outline, null);
			} else {
				g.drawImage(Board.img_hidden, i * W + outline, j * W + outline, W - outline, W - outline, null);
			}
		} else {
			g.drawImage(img, i * W + outline, j * W + outline, W - outline, W - outline, null);
		}
	}

	public int calculate(int status, Board board) {
		int statusNear = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (!(i == 0 && j == 0)) {
					int cj = this.j + j;
					int ci = this.i + i;

					if (cj >= 0 && ci >= 0 && cj <= rows - 1 && ci <= cols - 1) {
						if (status == -1) {
							if (board.tiles[cj][ci].status == -1) {
								statusNear++;
							}
						}
						if (status == -2) {
							if (board.tiles[cj][ci].isFlag == true) {
								statusNear++;
							}
						}
					}
				}
			}
		}
		return statusNear;
	}

}
