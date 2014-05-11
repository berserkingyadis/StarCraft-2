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
	private static final boolean DEBUGGING = true;
	private static final boolean CLASSIFYING = false;
	private static final boolean HUMAN_LIKE = true;
	private static final double SLOW = 2, FAST = 3;
	private static double PPMS = SLOW;
	private double mouseX = 0, mouseY = 0, xVel = 0, yVel = 0;
	private long lastMouseMoveTime;
	private Robot robot;
	private final Rectangle screenSize;
	private NetworkedStarCraftDetector detectorThread;
	private final static int GUI_Y = 525;
	private static int serialNum = 5000;
	private BufferedImage screenshot = null;
	private long lastScreenshot = 0;
	private long screenshotDelay = 250;

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

	public void setMouseSpeed(double speed) {
		PPMS = speed;
	}

	public void slowMouse() {
		PPMS = SLOW;
	}

	public void fastMouse() {
		PPMS = FAST;
	}

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

	public void leftClick(Point p) {
		leftClick(p.x, p.y);
	}

	public void rightClick(Point p) {
		rightClick(p.x, p.y);
	}

	public void shiftLeftClick(Point p) {
		shiftLeftClick(p.x, p.y);
	}

	public void shiftRightClick(Point p) {
		shiftRightClick(p.x, p.y);
	}

	public void leftClick(GameObject obj) {
		leftClick(obj.getCenter().x, obj.getCenter().y);
	}

	public void rightClick(GameObject obj) {
		rightClick(obj.getCenter().x, obj.getCenter().y);
	}

	public void shiftLeftClick(GameObject obj) {
		shiftLeftClick(obj.getCenter().x, obj.getCenter().y);
	}

	public void shiftRightClick(GameObject obj) {
		shiftRightClick(obj.getCenter().x, obj.getCenter().y);
	}

	public void moveMouse(int x, int y) {

		if (HUMAN_LIKE) {

			lastMouseMoveTime = System.currentTimeMillis();
			while (!(mouseX == x && mouseY == y)) {
				// Get current location
				// Done here to allow tampering
				Point mouse = MouseInfo.getPointerInfo().getLocation();
				mouseX = mouse.x;
				mouseY = mouse.y;
				// Used to quit when user interrupts
				/*
				 * if((int)mouseX != mouse.x || (int)mouseY != mouse.y){
				 * System.exit(0); } //
				 */

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

	public void killDetection() {
	}

	public void leftClick(int x, int y) {
		moveMouse(x, y);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	public void rightClick(int x, int y) {
		moveMouse(x, y);
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
	}

	public void shiftLeftClick(int x, int y) {
		moveMouse(x, y);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		robot.keyRelease(KeyEvent.VK_SHIFT);
	}

	public void shiftRightClick(int x, int y) {
		moveMouse(x, y);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		robot.keyRelease(KeyEvent.VK_SHIFT);
	}

	public void selectArea(Rectangle area) {
		moveMouse(area.x, area.y);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		moveMouse(area.x + area.width, area.y + area.height);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	public void selectAll() {
		moveMouse(50, 50);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		moveMouse(screenSize.width - 50, screenSize.height - 222);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	public void assignToGroup(int group) {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_0 + group);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_0 + group);
	}

	public void selectGroup(int group) {
		robot.keyPress(KeyEvent.VK_0 + group);
		robot.keyRelease(KeyEvent.VK_0 + group);
	}

	public int getWidth() {
		return screenSize.width;
	}

	public int getHeight() {
		return screenSize.height;
	}

	public void type(int vkS) {
		robot.keyPress(vkS);
		robot.keyRelease(vkS);
	}

	public void selectAllIdle() {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_F1);
		robot.keyRelease(KeyEvent.VK_F1);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	public void setMouse(int x, int y) {
		moveMouse(x, y);
	}

	public void sleep(double seconds) {
		try {
			Thread.sleep((int) (seconds * 1000));

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

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

	public int getResourceAmount(int type) {
		BufferedImage im = robot.createScreenCapture(screenSize);

		return Util.getResourceAmount(im, type);
	}

	public boolean haveIdleWorkers() {
		BufferedImage im = robot.createScreenCapture(screenSize);

		return Util.haveIdleWorkers(im);
	}

	public boolean canClickCommand(int x, int y) {
		BufferedImage im = screenShot();

		return Util.canClickCommand(im, x, y);
	}

	public void setMouse(Point p) {
		setMouse(p.x, p.y);
	}

	public boolean isAtBottomBase() {
		BufferedImage bi = screenShot();
		return Util.isAtBottomBase(bi);
	}

	public boolean isAtBottomBaseRequest() {
		BufferedImage bi = screenShot();
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

	public GameObject[] requestGameObjects() {
		return requestGameObjects(false);

	}

	public void addUnitRequest(String type) {
		detectorThread.addUnitOfInterest(type);
	}

	public void clearUnitRequests() {
		detectorThread.clearUnitsOfInterest();
	}

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

	public BufferedImage screenShot() {
		if (screenshot == null
				|| System.currentTimeMillis() - lastScreenshot > screenshotDelay) {
			screenshot = robot.createScreenCapture(screenSize);
			lastScreenshot = System.currentTimeMillis();
		}
		return screenshot;
	}

	public void clearScreenshot() {
		screenshot = null;
	}

	public BufferedImage gamePlayScreenShot() {
		Rectangle gameplay = new Rectangle(0, 0, screenSize.width,
				9 * screenSize.height / 10);
		return robot.createScreenCapture(gameplay);
	}

	public Rectangle[] findAllBases() {
		try {
			screenShot();
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
