package utilities;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class GameObject {
	protected int x, y, width, height;
	protected boolean isStationary;
	private static final int ICON_X = 440, ICON_Y = 630;
	private static HashMap<String, BufferedImage[]> icons;

	/**
	 * Checks if any larva are available when playing zerg.
	 * This method should not be in this class, but it was
	 * convenient to use the icons hash.
	 * @param im a screenshot
	 * @return true if there is at least one larva, false otherwise.
	 */
	public static boolean hasLarva(BufferedImage im){
		if(icons == null){
			icons = new HashMap<String, BufferedImage[]>();
		}
		
		if(!icons.containsKey("NoLarva")){
			BufferedImage bi = null;
			try {
				bi = ImageIO.read(new File("no_larva.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			icons.put("NoLarva", new BufferedImage[]{bi});
		}
		
		int x = 1120, y = 633;
		BufferedImage bi = icons.get("NoLarva")[0];
		for(int i = 0; i < bi.getHeight(); i++){
			for(int j = 0; j < bi.getWidth(); j++){
				int c1 = bi.getRGB(j, i);
				int c2 = im.getRGB(j+x, i+y);
				if(c1 != c2){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Check if the currently selected unit matches type
	 * @param im screenshot
	 * @param type the unit in question
	 * @return whether type is selected
	 */
	public static boolean isSelected(BufferedImage im, String type){
		if(icons == null){
			icons = new HashMap<String, BufferedImage[]>();
		}
		
		if(!icons.containsKey(type)){
			icons.put(type, Util.loadIconsFor(type));
		}
		
		return isSelected(im, icons.get(type));
	}
	
	/**
	 * Checks if the current selection matches the object type (intended for use by subclasses)
	 * @param im screenshot
	 * @return true if this class is currently selected, false otherwise
	 */
	public boolean isSelected(BufferedImage im) {
		if(icons == null){
			icons = new HashMap<String, BufferedImage[]>();
		}
		String className = this.getClass().getSimpleName();
		if(!icons.containsKey(className)){
			icons.put(className, Util.loadIconsFor(className));
		}
		
		return isSelected(im, icons.get(className));
	}

	/**
	 * Checks if the selection matches any of the images in icons
	 * @param screen screenshot
	 * @param icons possible selection icons
	 * @return true if the selection mathes any of the icons, false otherwise
	 */
	protected static boolean isSelected(BufferedImage screen,
			BufferedImage[] icons) {
		boolean foundOne = false;
		for (int i = 0; !foundOne && i < icons.length; i++) {
			int width = icons[i].getWidth();
			int height = icons[i].getHeight();
			boolean isTheOne = true;
			for (int r = 0; isTheOne && r < height; r++) {
				for (int c = 0; isTheOne && c < width; c++) {
					Color iconColor = new Color(icons[i].getRGB(c, r));
					Color screenColor = new Color(screen.getRGB(c + ICON_X, r + ICON_Y));
					boolean iconHasColor = iconColor.getRed() + 
											iconColor.getGreen() + 
											iconColor.getBlue() > 0;
					boolean screenHasColor = screenColor.getRed() + 
											screenColor.getGreen() + 
											screenColor.getBlue() > 0;
					isTheOne = iconHasColor == screenHasColor;
				}
			}
			foundOne = isTheOne;
		}
		return foundOne;
	}

	public Point getCenter() {
		return new Point(x + width / 2, y + height / 2);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isBlocking(int x, int y, int width, int height) {
		boolean blocking = ((x > this.x && x < this.x + this.width) || (x
				+ width > this.x && x + width < this.x + this.width))
				&& ((y > this.y && y < this.y + this.height) || (y + height > this.y && y
						+ height < this.y + this.height));
		return isStationary && blocking;
	}

	public boolean isType(String type) {
		return this.getClass().getName().equals(type);
	}
	
	public String toString(){
		return this.getClass().getSimpleName() + " X " + x + " Y " + y + " WIDTH " + width + " HEIGHT " + height;
	}
}
