package units;

import java.awt.image.BufferedImage;

import utilities.GameObject;
import utilities.Util;

public class Hellbat extends GameObject{

	private static BufferedImage[] icons = null;

	public Hellbat(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isStationary = true;
	}
}