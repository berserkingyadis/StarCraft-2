package runnables;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utilities.AI;

public class MapImageCreator {

	private static Point miniMap = new Point(20, 575);
	private static int width = 185, height = 185;
	private static int xTiles = 8, yTiles = 12;
	public static void main(String[] args) {
		try {
			int y = miniMap.y + height / ( 2 * yTiles);
			AI ai = new AI();
			ai.sleep(1);
			for (int i = 0; i < yTiles; i++) {
				int x = miniMap.x + width / (2 * xTiles);
				for (int j = 0; j < xTiles; j++) {
					ai.leftClick(x, y);
					ai.sleep(1);
					BufferedImage im = ai.screenshot();
					ai.sleep(1);
					File f = new File("MapImages/tile_" + i + "_" + j + ".png");
					ai.sleep(1);
					ImageIO.write(im, "png", f);

					ai.sleep(1);
					x += width / xTiles;
				}
				y += height / yTiles;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
