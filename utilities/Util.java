package utilities;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Util {

	public static final Rectangle MINIMAP = new Rectangle(20, 575, 185, 180);
	
	private static final int DIGIT_WIDTH = 9;
	private static final String GUI_ICONS_DIR = "GUI_Images/";
	private static final Color RESOURCES = new Color(126, 191, 241);
	private static final Color SELF = new Color(0,187,0);
	private static final Color SELF2 = new Color(0,255,0);
	private static final Color ENEMY = new Color(255,0,0);
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

	/**
	 * Returns the current amount of the specified resource
	 * @param im screenshot
	 * @param type resource identifier
	 * @return the amount
	 */
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

	/**
	 * Gets the digit at pixel location (x,y)
	 * @param im screenshot
	 * @param x horizontal pixel value
	 * @param y vertical pixel value
	 * @return the digit value
	 */
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

	/**
	 * Calculates the gray scale of a color
	 * @param color the color to convert
	 * @return gray scale value
	 */
	private static int grayScale(int color) {
		Color c = new Color(color);
		return (c.getRed() + c.getBlue() + c.getGreen()) / 3;
	}

	/**
	 * Returns whether there are idle workers or not
	 * @param im screenshot
	 * @return true if there is at least one idle worker, false otherwise
	 */
	public static boolean haveIdleWorkers(BufferedImage im) {
		int color = im.getRGB(40, 550);
		Color c = new Color(color);
		return c.getGreen() > 20;
	}

	/**
	 * Checks which base the player is at. Does not use CV detection.
	 * Check AI for a CV implementation.
	 * @param bi screenshot
	 * @return true if at bottom base location, false otherwise
	 */
	public static boolean isAtBottomBase(BufferedImage bi) {
		int color = bi.getRGB(148, 736);
		Color c = new Color(color);
		// actual would be 187 or 31
		return c.getGreen() > 100;
	}

	/**
	 * Converts a string array of types and parameters to their
	 * respective objects.
	 * @param objects the string array in the format <classname> x y width height
	 * @return the array of GameObjects
	 */
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

	/**
	 * Loads all icon images that contain 'object' in their pathname
	 * @param object the pathname substring requested
	 * @return all valid images
	 */
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

	/**
	 * Returns whether or not the command at location (x,y) on the unit command
	 * card can be clicked
	 * @param im screenshot
	 * @param x column of command
	 * @param y row of command
	 * @return false if the command cannot be click or doesn't exist, true otherwise
	 */
	public static boolean canClickCommand(BufferedImage im, int x, int y) {
		Point p = getCommand(x,y);
		int color = im.getRGB(p.x, p.y);
		Color c = new Color(color);
		
		return Math.max(Math.max(c.getRed(), c.getGreen()), c.getBlue()) > 100;
	}
	
	/**
	 * Converts a row and column to pixel location
	 * @param x column of command
	 * @param y row of command
	 * @return the associated point
	 */
	public static Point getCommand(int x, int y){
		Point p = new Point(1102, 650);
		p.x += 49*x;
		p.y += 49*y;
		return p;
	}

	/**
	 * Calculates k-means clustering of all minerals in the minimap for k= 4-20
	 * and returns the bounding boxes for the best scoring k
	 * @param screenshot screenshot
	 * @return best bounding boxes
	 */
	public static Rectangle[] findBestClusters(BufferedImage screenshot) {
		//Get all points
		Point[] points = findAllPoints(screenshot);
		Rectangle[] clusters = null;
		double bestScore = Integer.MAX_VALUE;
		for(int k = 4; k <= 20; k++){
			//Find clusters for current K
			Rectangle[] newClusters = runKMeans(points, k);
			
			//Score current K
			double score = scoreClusters(newClusters);
			
			//Compare 
			if(score < bestScore){
				bestScore = score;
				clusters = newClusters;
			}
		} 
		return clusters;
	}
	
	/**
	 * Calculates a score for a group of bounding boxes
	 * @param clusters the boxes
	 * @return the score
	 */
	private static double scoreClusters(Rectangle[] clusters) {
		int minArea = Integer.MAX_VALUE;
		int maxArea = 0;
		for(int i = 0; i < clusters.length; i++){
			int area = clusters[i].width * clusters[i].height;
			if(area < minArea){ minArea = area; }
			if(area > maxArea){ maxArea = area; }
		}
		return maxArea - minArea;
	}
	
	/**
	 * Find the player's base from a group of bases
	 * @param clusters possible base locations
	 * @param screen screenshot
	 * @return the index of the base location in clusters, -1 if not found
	 */
	public static int findOwnBase(Rectangle[] clusters, BufferedImage screen){
		int result = findColorBase(clusters, screen, SELF);
		if(result < 0){
			result = findColorBase(clusters, screen, SELF2);
		}
		return result;
	}
	
	/**
	 * Find the enemy's base from a group of bases
	 * @param clusters possible base locations
	 * @param screen screenshot
	 * @return the index of the base location in clusters, -1 if not found
	 */
	public static int findEnemyBase(Rectangle[] clusters, BufferedImage screen){
		return findColorBase(clusters, screen, ENEMY);
	}

	/**
	 * Finds the given color in a group of regions
	 * @param clusters regions
	 * @param screen screenshot
	 * @param c color being searched for
	 * @return first region index containing c, -1 if not found
	 */
	private static int findColorBase(Rectangle[] clusters, BufferedImage screen, Color c){
		for(int i = 0; i < clusters.length; i++){
			Rectangle r = clusters[i];
			for(int y = r.y; y < r.y + r.height; y++){
				for(int x = r.x; x < r.x + r.width; x++){
					if(screen.getRGB(x,y) == c.getRGB()){
						return i;
					}
				}
			}
		}
		
		return -1;
	}
	
	/**
	 * Returns an array of all the points in the minimap that represent minerals
	 * @param bi screenshot
	 * @return the array of mineral locations
	 */
	public static Point[] findAllPoints(BufferedImage bi){
		ArrayList<Point> points = new ArrayList<Point>();
		for(int y = MINIMAP.y; y < MINIMAP.y + MINIMAP.height; y++){
			for(int x = MINIMAP.x; x < MINIMAP.x + MINIMAP.width; x++){
				if(bi.getRGB(x, y) == RESOURCES.getRGB()){
					points.add(new Point(x,y));
				}
			}
		}
		
		return points.toArray(new Point[]{});
	}
	
	/**
	 * Calculates the bounding for a given set of points and k clusters
	 * @param points the points to cluster
	 * @param k number of clusters
	 * @return bounding boxes for each cluster
	 */
	public static Rectangle[] runKMeans(Point[] points, int k){
		Rectangle[] clusters = new Rectangle[k];
		Point[] clusterCenters = new Point[k];
		
		//Initialize cluster centers
		for(int i = 0; i < k; i++){
			clusterCenters[i] = new Point((int)(Math.random()*(MINIMAP.width) + MINIMAP.x), 
										(int)(Math.random()*MINIMAP.height) + MINIMAP.y);
		}
		
		//Give each cluster one point
		ArrayList<ArrayList<Point>> assigned = new ArrayList<ArrayList<Point>>();
		for(int i = 0; i < k; i++){
			assigned.add(new ArrayList<Point>());
			double bestDist = Double.MAX_VALUE;
			int bestIndex = 0;
			for(int j = 0; j < points.length; j++){
				if(points[j] != null){
					double dist = clusterCenters[i].distance(points[j]);
					if(dist < bestDist){
						//Make sure it's not already used
						boolean ok = true;
						int prev = 0;
						while(ok && prev < i){
							ok = !assigned.get(prev).get(0).equals(points[j]);
							prev++;
						}

						if(ok){
							bestDist = dist;
							bestIndex = j;
						}
					}
				}
			}
			
			assigned.get(i).add(points[bestIndex]);
			//temporarily remove
			points[bestIndex] = null;
		}
		
		//Assign the rest of the points
		for(int i = 0; i < points.length; i++){
			if(points[i] != null){
				double bestDist = Double.MAX_VALUE;
				int bestIndex = 0;
				for(int j = 0; j < clusterCenters.length; j++){
					double dist = points[i].distance(clusterCenters[j]);
					if(dist < bestDist){
						bestDist = dist;
						bestIndex = j;
					}
				}
				
				assigned.get(bestIndex).add(points[i]);
			}
		}
		
		//replace
		int nullIndex = -1;
		int replaced = 0;
		while(replaced < k){
			if(points[++nullIndex] == null){
				points[nullIndex] = assigned.get(replaced++).get(0);
			}
		}
			
		
		boolean changedGroup = true;
		while(changedGroup){
			changedGroup = false;
			
			//update centers
			for(int i = 0; i < clusters.length; i++){
				double x = 0;
				double y = 0;
				for(int j = 0; j < assigned.get(i).size(); j++){
					x += assigned.get(i).get(j).getX();
					y += assigned.get(i).get(j).getY();
				}
				x /= assigned.get(i).size();
				y /= assigned.get(i).size();
				
				clusterCenters[i] = new Point((int)x,(int)y);
			}
			
			// for each point in assigned, check if it should
			// change groups
			for(int i = 0; i < assigned.size(); i++){
				for(int j = 0; j < assigned.get(i).size(); j++){
					Point p = assigned.get(i).get(j);
					double bestDist = Double.MAX_VALUE;
					int bestIndex = 0;
					for(int center = 0; center < clusterCenters.length; center++){
						double dist = clusterCenters[center].distance(p);
						if(dist < bestDist){
							bestDist = dist;
							bestIndex = center;
						}
					}
					
					//if it should be in a new list
					if(bestIndex != i){
						changedGroup = true;
						assigned.get(i).remove(j--);
						assigned.get(bestIndex).add(p);
					}
				}
			}
		}
		
		//Remove unused clusters
		for(int i = 0; i < assigned.size(); i++){
			if(assigned.get(i).size() == 0){
				assigned.remove(i--);
			}
		}
		
		// Calculate regions from clusters
		clusters = new Rectangle[assigned.size()];
		for(int i = 0; i < clusters.length; i++){
			int x = Integer.MAX_VALUE;
			int y = Integer.MAX_VALUE;
			int right = 0;
			int bottom = 0;
			for(int j = 0; j < assigned.get(i).size(); j++){
				Point p = assigned.get(i).get(j);
				if(p.x < x){x = p.x;}
				if(p.y < y){y = p.y;}
				if(p.x > right){right = p.x;}
				if(p.y > bottom){bottom = p.y;}
			}
			clusters[i] = new Rectangle(x,y,right-x, bottom-y);
		}
		sort(clusters);
		return clusters;
	}
	
	/**
	 * Sorts clusters as best as possible to get a left to right, top to bottom ordering.
	 * ... using bubble sort. There's at max 20 clusters, OK?
	 * @param clusters clusters to sort
	 */
	public static void sort(Rectangle[] clusters){
		for(int i = 0; i < clusters.length; i++){
			for(int j = i + 1; j < clusters.length; j++){
				if(outOfOrder(clusters[i], clusters[j])){
					Rectangle temp = clusters[i];
					clusters[i] = clusters[j];
					clusters[j] = temp;
				}
			}
		}
	}
	
	/**
	 * Determines if two boxes are currently in the correct order
	 * @param r1 rectangle currently considered first
	 * @param r2 rectangle currently considered second
	 * @return true if they should be reversed, false otherwise
	 */
	public static boolean outOfOrder(Rectangle r1, Rectangle r2){
		if(r1.y+r1.height < r2.y){
			return false;
		} else if(r2.y + r2.height < r1.y){
			return true;
		} else {
			return r1.x > r2.x;
		}
	}
}
