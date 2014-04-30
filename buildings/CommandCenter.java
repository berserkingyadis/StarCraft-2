package buildings;

import java.awt.image.BufferedImage;

import utilities.GameObject;
import utilities.Util;

public class CommandCenter extends GameObject {

	private static BufferedImage[] icons = null;
	
	public CommandCenter(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isStationary = true;
	}

	public boolean isType(String type) {
		return type.equals("CommandCenter");
	}
}
