package utilities;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import networking.NetworkedStarCraftDetector;

public class AI {
	
	// Class constants to change execution behavior
	private static final boolean DEBUGGING = true;
	private static final boolean CLASSIFYING = false;
	private static final boolean HUMAN_LIKE = true;
	
	// Class constants 
	private static final double SLOW = 2, FAST = 3;
	private final static int GUI_Y = 525;
	private final Rectangle screenSize;
	
	// Instance variables
	private static int serialNum = 5000;
	private double mouseX = 0, mouseY = 0, xVel = 0, yVel = 0;
	private double PPMS = SLOW;
	private long lastScreenshot = 0;
	private long screenshotDelay = 250;
	private long lastMouseMoveTime;
	private NetworkedStarCraftDetector detectorThread;
	private BufferedImage screenshot = null;
	private Robot robot;

	public AI() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize = new Rectangle(0, 0, dim.width, dim.height);
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		mouseX = mouse.x;
		mouseY = mouse.y;
		try {

			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set the speed at which the mouse moves.
	 * 
	 * @param speed mouse speed in pixels per millisecond
	 */
	public void setMouseSpeed(double speed) {
		PPMS = speed;
	}

	/**
	 * Set the mouse to a normal speed
	 */
	public void slowMouse() {
		PPMS = SLOW;
	}

	/**
	 * Set the mouse to a fast speed
	 */
	public void fastMouse() {
		PPMS = FAST;
	}

	/**
	 * Connect to the detection server. Look for all units
	 * of interests to start.
	 */
	public void startDetectorThread() {
		try {
			detectorThread = new NetworkedStarCraftDetector(
					"cedar.cs.wisc.edu", 8002);
			detectorThread.addUnitOfInterest("All");
			detectorThread.start();
		} catch (AWTException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Retrieves the bounding boxes for all units of interest and
	 * saves that region as a new image. Used for easily generating custom
	 * negatives in the early stages of creating a cascade detector.
	 */
	public void classify() {
		if (CLASSIFYING) {
			try {
				BufferedImage bi = gamePlayScreenShot();
				sleep(0.3);
				String[] data = detectorThread.getMostRecentInformation()
						.split(",");
				GameObject[] objects = Util.getGameObjects(data);
				for (int i = 0; objects != null && i < objects.length; i++) {
					GameObject go = objects[i];
					BufferedImage im = new BufferedImage(go.getWidth(),
							go.getHeight(), BufferedImage.TYPE_INT_RGB);
					for (int y = 0; y < go.getHeight(); y++) {
						for (int x = 0; x < go.getWidth(); x++) {
							im.setRGB(x, y,
									bi.getRGB(x + go.getX(), y + go.getY()));
						}
					}
					ImageIO.write(im, "png", new File("boxes/"
							+ go.getClass().getSimpleName() + "_" + serialNum++
							+ ".png"));
				}

			} catch (IOException e) {
			}
		}
	}

	/**
	 * The robot presses alt + tab to switch to the previous window.
	 */
	public void altTab() {
		try {
			robot.keyPress(KeyEvent.VK_ALT);
			Thread.sleep(100);
			robot.keyPress(KeyEvent.VK_TAB);
			Thread.sleep(100);
			robot.keyRelease(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_ALT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Moves the mouse to p and left clicks
	 * @param p the pixel location to click
	 */
	public void leftClick(Point p) {
		leftClick(p.x, p.y);
	}

	/**
	 * Moves the mouse to p and right clicks
	 * @param p the pixel location to click
	 */
	public void rightClick(Point p) {
		rightClick(p.x, p.y);
	}

	/**
	 * Moves the mouse to p and left clicks while holding shift
	 * @param p the pixel location to click
	 */
	public void shiftLeftClick(Point p) {
		shiftLeftClick(p.x, p.y);
	}

	/**
	 * Moves the mouse to p and right clicks while holding shift
	 * @param p the pixel location to click
	 */
	public void shiftRightClick(Point p) {
		shiftRightClick(p.x, p.y);
	}

	/**
	 * Moves the mouse to the center of obj and left clicks
	 * @param obj the GameObject whose center is being clicked
	 */
	public void leftClick(GameObject obj) {
		leftClick(obj.getCenter().x, obj.getCenter().y);
	}
	
	/**
	 * Moves the mouse to the center of obj and right clicks
	 * @param obj the GameObject whose center is being clicked
	 */
	public void rightClick(GameObject obj) {
		rightClick(obj.getCenter().x, obj.getCenter().y);
	}

	/**
	 * Moves the mouse to the center of obj and left clicks while holding shift
	 * @param obj the GameObject whose center is being clicked
	 */
	public void shiftLeftClick(GameObject obj) {
		shiftLeftClick(obj.getCenter().x, obj.getCenter().y);
	}

	/**
	 * Moves the mouse to the center of obj and right clicks while hold shift
	 * @param obj the GameObject whose center is being clicked
	 */
	public void shiftRightClick(GameObject obj) {
		shiftRightClick(obj.getCenter().x, obj.getCenter().y);
	}

	/**
	 * Moves the mouse to pixel location (x,y) and left clicks
	 * @param x horizontal pixel location
	 * @param y vertical pixel location
	 */
	public void leftClick(int x, int y) {
		moveMouse(x, y);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	/**
	 * Moves the mouse to pixel location (x,y) and right clicks
	 * @param x horizontal pixel location
	 * @param y vertical pixel location
	 */
	public void rightClick(int x, int y) {
		moveMouse(x, y);
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
	}

	/**
	 * Moves the mouse to pixel location (x,y) and left clicks while holding shift
	 * @param x horizontal pixel location
	 * @param y vertical pixel location
	 */
	public void shiftLeftClick(int x, int y) {
		moveMouse(x, y);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		robot.keyRelease(KeyEvent.VK_SHIFT);
	}

	/**
	 * Moves the mouse to pixel location (x,y) and right clicks while holding shift
	 * @param x horizontal pixel location
	 * @param y vertical pixel location
	 */
	public void shiftRightClick(int x, int y) {
		moveMouse(x, y);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		robot.keyRelease(KeyEvent.VK_SHIFT);
	}
	

	/**
	 * Moves the mouse to p
	 * @param p the pixel location to move to
	 */
	public void setMouse(Point p) {
		setMouse(p.x, p.y);
	}

	/**
	 * Moves the mouse to pixel location (x, y). If HUMAN_LIKE is true, this will
	 * move towards (x,y) at a speed of PPMS pixels per millisecond. Otherwise,
	 * the mouse is simply placed at that location without simulated movement
	 * @param x horizontal value of the pixel location
	 * @param y vertical value of the pixel location
	 */
	public void moveMouse(int x, int y) {

		if (HUMAN_LIKE) {

			lastMouseMoveTime = System.currentTimeMillis();
			while (!(mouseX == x && mouseY == y)) {
				// Get current location
				// Done here to allow tampering
				Point mouse = MouseInfo.getPointerInfo().getLocation();
				mouseX = mouse.x;
				mouseY = mouse.y;

				double xDist = x - mouseX;
				double yDist = y - mouseY;
				double angle = Math.atan2(yDist, xDist);
				xVel = Math.cos(angle) * PPMS;
				yVel = Math.sin(angle) * PPMS;
				long newTime = System.currentTimeMillis();
				long deltaTime = newTime - lastMouseMoveTime;
				double toMoveX;
				if (Math.abs(x - mouseX) < Math.abs(xVel * deltaTime)) {
					toMoveX = x - mouseX;
				} else {
					toMoveX = xVel * deltaTime;
				}

				double toMoveY;
				if (Math.abs(y - mouseY) < Math.abs(yVel * deltaTime)) {
					toMoveY = y - mouseY;
				} else {
					toMoveY = yVel * deltaTime;
				}

				mouseX += toMoveX;
				mouseY += toMoveY;
				robot.mouseMove((int) mouseX, (int) mouseY);
				lastMouseMoveTime = newTime;
				sleep(0.01);
			}
		} else {
			robot.mouseMove(x, y);
		}
	}
	
	/**
	 * Moves the mouse to the top left corner of area and clicks and drags to 
	 * the bottom right corner of area
	 * @param area the area to drag the mouse over
	 */
	public void selectArea(Rectangle area) {
		moveMouse(area.x, area.y);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		moveMouse(area.x + area.width, area.y + area.height);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	/**
	 * Drags the mouse over the area (50,50) to 
	 * (screenSize.width - 50, screenSize.height - 222)
	 */
	public void selectAll() {
		moveMouse(50, 50);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		moveMouse(screenSize.width - 50, screenSize.height - 222);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	/**
	 * Saves the current selection to the given group
	 * @param group the group number to save the selection to
	 */
	public void assignToGroup(int group) {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_0 + group);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_0 + group);
	}

	/**
	 * Retrieves a previously assigned group 
	 * @param group the group number to retrieve
	 */
	public void selectGroup(int group) {
		robot.keyPress(KeyEvent.VK_0 + group);
		robot.keyRelease(KeyEvent.VK_0 + group);
	}

	/**
	 * Gets the width of the entire screen
	 * @return the screen width
	 */
	public int getWidth() {
		return screenSize.width;
	}

	/**
	 * Gets the height of the entire screen
	 * @return the screen height
	 */
	public int getHeight() {
		return screenSize.height;
	}

	/**
	 * Types a single key
	 * @param vkS the ID of the key to press (given by KeyEvent.VK_?
	 */
	public void type(int vkS) {
		robot.keyPress(vkS);
		robot.keyRelease(vkS);
	}

	/**
	 * Attempts to select all idle workers by pressing ctrl + F1
	 */
	public void selectAllIdle() {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_F1);
		robot.keyRelease(KeyEvent.VK_F1);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	/**
	 * Moves the mouse to pixel location (x,y)
	 * @param x horizontal pixel location
	 * @param y vertical pixel location
	 */
	public void setMouse(int x, int y) {
		moveMouse(x, y);
	}

	/**
	 * The AI waits and blocks
	 * @param seconds the amount of time to wait
	 */
	public void sleep(double seconds) {
		try {
			Thread.sleep((int) (seconds * 1000));

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to get debugging messages in game. Types the message to the game chat.
	 * Does nothing if DEBUGGING == false
	 * @param message the message to display
	 */
	public void debug(String message) {
		if (DEBUGGING) {
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			for (int i = 0; i < message.length(); i++) {
				char c = message.charAt(i);
				if (Character.isUpperCase(c)) {
					robot.keyPress(KeyEvent.VK_SHIFT);
				}
				robot.keyPress(Character.toUpperCase(c));
				robot.keyRelease(Character.toUpperCase(c));

				if (Character.isUpperCase(c)) {
					robot.keyRelease(KeyEvent.VK_SHIFT);
				}
			}

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		}
	}

	/**
	 * Blocks until the user has the specified resource amount
	 * @param param the resource needed (e.g. 50 minerals)
	 */
	public void waitFor(Resource param) {
		int value = -1;
		int target = param.getAmount();

		if (param.getType() == Resource.IDLEWORKERS) {
			while (!haveIdleWorkers()) {
				try {
					Thread.sleep(333);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {

			while (value < target) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				value = getResourceAmount(param.getType());
			}
		}

	}

	/**
	 * Used to get how much of the given resource is available
	 * @param type which resource is being requested
	 * @return the amount of the requested resource that is available
	 */
	public int getResourceAmount(int type) {
		BufferedImage im = robot.createScreenCapture(screenSize);

		return Util.getResourceAmount(im, type);
	}

	/**
	 * Used to check if any worker units are not being used
	 * @return whether or not there are any idle workers
	 */
	public boolean haveIdleWorkers() {
		BufferedImage im = robot.createScreenCapture(screenSize);

		return Util.haveIdleWorkers(im);
	}

	/**
	 * Used to check whether a command on the unit command card at (x,y)
	 * is currently usable
	 * @param x the column of the command
	 * @param y the row of the command
	 * @return false if it cannot be used or there is no command at the location,
	 * true otherwise.
	 */
	public boolean canClickCommand(int x, int y) {
		BufferedImage im = screenshot();

		return Util.canClickCommand(im, x, y);
	}

	/**
	 * Used to determine which base the AI is at. Does not use CV, rather finds our 
	 * location on the minimap
	 * @return true if the AI started on the bottom base, false otherwise
	 */
	public boolean isAtBottomBase() {
		BufferedImage bi = screenshot();
		return Util.isAtBottomBase(bi);
	}

	/**
	 * Used to determine which base the AI is at. Uses CV to determine bases on location of 
	 * the command center
	 * @return true if the AI started on the bottom base, false otherwise
	 */
	public boolean isAtBottomBaseRequest() {
		BufferedImage bi = screenshot();
		String[] data = detectorThread.getCurrentInformation().split(",");
		GameObject[] objects = Util.getGameObjects(data);
		for (int i = 0; i < objects.length; i++) {
			if (objects[i].isType("CommandCenter")) {
				return objects[i].getCenter().x < bi.getWidth() / 2;
			}
		}

		System.err.println("NO BASE FOUND!");
		return true;
	}

	/**
	 * Adds another detector to run on requests
	 * @param type which detector to add. A package specified class (e.g. buildings.CommandCenter)
	 */
	public void addUnitRequest(String type) {
		detectorThread.addUnitOfInterest(type);
	}

	/**
	 * Removes all requested detectors
	 */
	public void clearUnitRequests() {
		detectorThread.clearUnitsOfInterest();
	}
	
	/**
	 * Gets the most recent results from detection
	 * @return the GameObjects the were detected
	 */
	public GameObject[] requestGameObjects() {
		return requestGameObjects(false);

	}

	/**
	 * Gets the most recent results from detection
	 * @param takeNewScreenShot waits for a new result of detection if true,
	 * otherwise uses the most recent result
	 * @return the GameObjects the were detected
	 */
	public GameObject[] requestGameObjects(boolean takeNewScreenShot) {
		ArrayList<GameObject> filterObjects = new ArrayList<GameObject>();
		String[] data;
		if (takeNewScreenShot) {
			data = detectorThread.getCurrentInformation().split(",");
		} else {
			data = detectorThread.getMostRecentInformation().split(",");
		}
		GameObject[] objects = Util.getGameObjects(data);
		for (int i = 0; objects != null && i < objects.length; i++) {
			if (objects[i].getCenter().y < GUI_Y) {
				filterObjects.add(objects[i]);
			}
		}

		return filterObjects.toArray(new GameObject[] {});

	}

	/**
	 * Takes a screenshot if the previous one is expired or non-existant
	 * @return the screenshot
	 */
	public BufferedImage screenshot() {
		if (screenshot == null
				|| System.currentTimeMillis() - lastScreenshot > screenshotDelay) {
			screenshot = robot.createScreenCapture(screenSize);
			lastScreenshot = System.currentTimeMillis();
		}
		return screenshot;
	}

	/**
	 * Removes the current screenshot so a new one is forced
	 * next time screenShot() is called
	 */
	public void clearScreenshot() {
		screenshot = null;
	}

	/**
	 * Takes a new screenshot, cutting off the bottom 10% (HUD elements)
	 * @return the screenshot
	 */
	public BufferedImage gamePlayScreenShot() {
		Rectangle gameplay = new Rectangle(0, 0, screenSize.width,
				9 * screenSize.height / 10);
		return robot.createScreenCapture(gameplay);
	}

	/**
	 * Runs k-means clustering to find all the bases and return them as
	 * an array of rectangle bounding boxes. Also saves an image of the boxes overlayed
	 * on the screen to allow for inspection
	 * @return the bases bounding boxes
	 */
	public Rectangle[] findAllBases() {
		try {
			screenshot();
			Rectangle[] bases = Util.findBestClusters(screenshot);
			ImageIO.write(screenshot, "png", new File("preclusters.png"));
			Graphics g = screenshot.createGraphics();
			g.setColor(Color.RED);
			for (int i = 0; i < bases.length; i++) {
				Rectangle r = bases[i];
				g.drawLine(r.x, r.y, r.x + r.width, r.y);
				g.drawLine(r.x, r.y, r.x, r.y + r.height);
				g.drawLine(r.x, r.y + r.height, r.x + r.width, r.y + r.height);
				g.drawLine(r.x + r.width, r.y, r.x + r.width, r.y + r.height);
			}
			
			ImageIO.write(screenshot, "png", new File("clusters.png"));
			return bases;
		} catch (IOException e) {
		}
		return null;
	}

}
