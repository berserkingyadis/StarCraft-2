package utilities;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Util {

	private static final String GUI_ICONS_DIR = "GUI_Images/";
	private static final int DIGIT_WIDTH = 9;
	private static final int[][][] DIGITS = new int[][][] {
			// ZERO
			{ { 130, 255, 255, 255, 255, 255, 130 },
					{ 255, 130, 0, 0, 0, 130, 255 },
					{ 255, 0, 0, 0, 0, 0, 255 }, { 255, 0, 0, 0, 0, 0, 255 },
					{ 255, 0, 0, 0, 0, 0, 255 },
					{ 255, 130, 0, 0, 0, 130, 255 },
					{ 130, 255, 255, 255, 255, 255, 130 }, },
			// ONE
			{ { 0, 0, 0, 255, 255, 0, 0 }, { 0, 130, 255, 130, 255, 0, 0 },
					{ 0, 255, 0, 0, 255, 0, 0 }, { 0, 0, 0, 0, 255, 0, 0 },
					{ 0, 0, 0, 0, 255, 0, 0 }, { 0, 0, 0, 0, 255, 0, 0 },
					{ 0, 0, 0, 0, 255, 0, 0 }, },
			// TWO
			{ { 130, 255, 255, 255, 255, 255, 130 },
					{ 255, 130, 0, 0, 0, 130, 255 },
					{ 130, 0, 0, 0, 0, 0, 255 },
					{ 0, 155, 175, 195, 215, 235, 255 },
					{ 130, 255, 235, 215, 195, 175, 0 },
					{ 255, 0, 0, 0, 0, 0, 0 },
					{ 255, 255, 255, 255, 255, 255, 255 }, },
			// THREE
			{ { 130, 255, 255, 255, 255, 255, 130 },
					{ 255, 130, 0, 0, 0, 55, 255 }, { 55, 0, 0, 0, 0, 0, 255 },
					{ 0, 0, 0, 255, 255, 255, 130 },
					{ 55, 0, 0, 0, 0, 0, 255 }, { 255, 130, 0, 0, 0, 55, 255 },
					{ 55, 255, 255, 255, 255, 255, 130 }, },
			// FOUR
			{ { 0, 0, 0, 0, 255, 255, 0 }, { 0, 0, 0, 255, 130, 255, 0 },
					{ 0, 0, 255, 0, 0, 255, 0 }, { 130, 255, 0, 0, 0, 255, 0 },
					{ 255, 130, 0, 0, 130, 255, 130 },
					{ 255, 255, 255, 255, 255, 255, 255 },
					{ 0, 0, 0, 0, 130, 255, 130 }, },
			// FIVE
			{ { 255, 255, 255, 255, 255, 255, 255 },
					{ 255, 130, 0, 0, 0, 0, 0 }, { 255, 130, 0, 0, 0, 0, 0 },
					{ 255, 255, 255, 255, 255, 255, 130 },
					{ 130, 0, 0, 0, 0, 0, 255 },
					{ 255, 130, 0, 0, 0, 130, 255 },
					{ 130, 255, 255, 255, 255, 255, 130 }, },
			// SIX
			{ { 130, 255, 255, 255, 255, 255, 130 },
					{ 255, 130, 0, 0, 0, 0, 255 }, { 255, 0, 0, 0, 0, 0, 130 },
					{ 255, 255, 255, 255, 255, 255, 130 },
					{ 255, 0, 0, 0, 0, 0, 255 }, { 255, 0, 0, 0, 0, 130, 255 },
					{ 130, 255, 255, 255, 255, 255, 130 }, },
			// SEVEN
			{ { 255, 255, 255, 255, 255, 255, 255 },
					{ 0, 0, 0, 0, 0, 130, 255 }, { 0, 0, 0, 0, 0, 255, 0 },
					{ 0, 0, 0, 0, 255, 0, 0 }, { 0, 0, 0, 255, 0, 0, 0 },
					{ 0, 0, 255, 0, 0, 0, 0 }, { 0, 255, 0, 0, 0, 0, 0 }, },
			// EIGHT
			{ { 130, 255, 255, 255, 255, 255, 130 },
					{ 255, 55, 0, 0, 0, 55, 255 },
					{ 255, 0, 0, 0, 0, 55, 255 },
					{ 130, 255, 255, 255, 255, 255, 130 },
					{ 255, 0, 0, 0, 0, 55, 255 },
					{ 255, 55, 55, 55, 55, 55, 255 },
					{ 130, 255, 255, 255, 255, 255, 130 }, },
			// NINE
			{ { 130, 255, 255, 255, 255, 255, 130 },
					{ 255, 55, 0, 0, 0, 55, 255 },
					{ 255, 0, 0, 0, 0, 55, 255 },
					{ 130, 255, 255, 255, 255, 130, 255 },
					{ 130, 0, 0, 0, 0, 0, 255 }, { 255, 55, 0, 0, 0, 55, 255 },
					{ 130, 255, 255, 255, 255, 255, 110 }, },
			// NOTHING
			{ { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0 }, },

	};

	public static int getResourceAmount(BufferedImage im, int type) {
		int x = 0, y = 16;
		switch (type) {
		case Resource.MINERALS:
			x = 1086;
			break;
		case Resource.VESPENE:
			x = 1174;
			break;
		case Resource.CURSUPPLY:
			x = 1262;
			break;
		case Resource.MAXSUPPLY:
			x = 1262;
			break;
		}

		if (type == Resource.MAXSUPPLY) {
			int digit = 0;
			while (digit >= 0) {
				digit = getDigit(im, x, y);
				x += DIGIT_WIDTH;
			}
			x--;
		}

		int amount = 0;
		int digit = 0;
		while (digit >= 0) {
			amount = amount * 10 + digit;
			digit = getDigit(im, x, y);
			x += DIGIT_WIDTH;

		}
		return amount;
	}

	private static int getDigit(BufferedImage im, int x, int y) {
		long bestVal = Long.MAX_VALUE;
		int bestDig = -1;
		for (int i = 0; i < DIGITS.length; i++) {
			long curVal = 0;
			if (i != DIGITS.length - 1) {
				for (int row = 0; row < DIGITS[i].length; row++) {
					for (int col = 0; col < DIGITS[i][row].length; col++) {
						long c1 = grayScale(im.getRGB(x + col, y + row));
						long c2 = DIGITS[i][row][col];
						curVal += (c1 - c2) * (c1 - c2);
					}
				}

				if (curVal < bestVal) {
					bestVal = curVal;
					bestDig = i;
				}
			} else {
				int whitePixels = 0;
				for (int row = 0; row < DIGITS[i].length; row++) {
					for (int col = 0; col < DIGITS[i][row].length; col++) {
						if (grayScale(im.getRGB(x + col, y + row)) > 200) {
							whitePixels++;
						}

					}
				}
				if (whitePixels < 9) {
					bestDig = -1;
				}
			}

		}
		return bestDig;
	}

	private static int grayScale(int color) {
		Color c = new Color(color);
		return (c.getRed() + c.getBlue() + c.getGreen()) / 3;
	}

	public static boolean haveIdleWorkers(BufferedImage im) {
		int color = im.getRGB(40, 550);
		Color c = new Color(color);
		return c.getGreen() > 20;
	}

	public static boolean isAtBottomBase(BufferedImage bi) {
		int color = bi.getRGB(148, 736);
		Color c = new Color(color);
		// actual would be 187 or 31
		return c.getGreen() > 100;
	}

	public static GameObject[] getGameObjects(String[] objects) {
		GameObject[] result = new GameObject[objects.length];

		try {
			for (int i = 0; i < objects.length; i++) {
				if(objects[i].trim().length() > 5){
					String[] data = objects[i].split(" ");
					Object[] parameters = new Object[4];
					for (int j = 0; j < 4; j++) {
						parameters[j] = Integer.parseInt(data[j + 1]);
					}

					// Find the right class
					Class<?> c = Class.forName(data[0]);

					// Find its constructor
					Constructor<?>[] cons = c.getConstructors();

					// Assume it's the only one
					result[i] = (GameObject) cons[0].newInstance(parameters);
				}
				else{
					result = null;
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static BufferedImage[] loadIconsFor(String object) {
		try {
			File dir = new File(GUI_ICONS_DIR);
			File[] files = dir.listFiles();
			ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().contains(object)) {
					images.add(ImageIO.read(files[i]));
				}
			}

			return images.toArray(new BufferedImage[]{});
		} catch (IOException e) {
			return null;
		}

	}

	public static boolean canClickCommand(BufferedImage im, int x, int y) {
		Point p = getCommand(x,y);
		int color = im.getRGB(p.x, p.y);
		Color c = new Color(color);
		
		return Math.max(Math.max(c.getRed(), c.getGreen()), c.getBlue()) > 100;
	}
	
	public static Point getCommand(int x, int y){
		Point p = new Point(1102, 650);
		p.x += 49*x;
		p.y += 49*y;
		return p;
	}

}
