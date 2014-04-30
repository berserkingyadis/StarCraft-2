package utilities;

import java.awt.AWTException;
import java.awt.Dimension;
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

public class AI {
	
	private static final boolean DEBUGGING = true;
	private Robot robot;
	private final Rectangle screenSize;
	private NetworkedMatlabCommunicator server;
	private final static int GUI_Y = 525;
	private static int serialNum = 5000;

	public AI() {
		try {
			server = new NetworkedMatlabCommunicator();
		} catch (IOException e1) {
			
		}
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize = new Rectangle(0, 0, dim.width, dim.height);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void classify(){
		try {
			BufferedImage bi = gamePlayScreenShot();
			String[] data = server.getBoundingBoxesForScreenshot(bi);
			GameObject[] objects = Util.getGameObjects(data);
			for(int i = 0; objects != null && i < objects.length; i++){
				GameObject go = objects[i];
				BufferedImage im = new BufferedImage(go.getWidth(), go.getHeight(), BufferedImage.TYPE_INT_RGB);
				for(int y = 0; y < go.getHeight(); y++){
					for(int x = 0; x < go.getWidth(); x++){
						im.setRGB(x, y, bi.getRGB(x+go.getX(),y+go.getY()));
					}
				}
				ImageIO.write(im, "png",
						new File("boxes/" + go.getClass().getSimpleName() + "_" + serialNum++ + ".png"));
			}
				
		} catch (IOException e) {
		}
		
	}
	
	public void altTab(){
		try {
			robot.keyPress(KeyEvent.VK_ALT);
			Thread.sleep(100);
			robot.keyPress(KeyEvent.VK_TAB);
			Thread.sleep(100);
			robot.keyRelease(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_ALT);
		}  catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void leftClick(Point p){
		leftClick(p.x, p.y);
	}
	
	public void rightClick(Point p){
		rightClick(p.x, p.y);
	}
	
	public void shiftLeftClick(Point p){
		shiftLeftClick(p.x, p.y);
	}
	
	public void shiftRightClick(Point p){
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
		shiftRightClick(obj.getCenter().x,
				obj.getCenter().y);
	}

	public void leftClick(int x, int y) {
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	public void rightClick(int x, int y) {
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
	}

	public void shiftLeftClick(int x, int y) {
		robot.mouseMove(x, y);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		robot.keyRelease(KeyEvent.VK_SHIFT);
	}

	public void shiftRightClick(int x, int y) {
		robot.mouseMove(x, y);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		robot.keyRelease(KeyEvent.VK_SHIFT);
	}

	public void selectAll() {
		robot.mouseMove(50, 50);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseMove(screenSize.width - 50, screenSize.height - 50);
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
		robot.mouseMove(x, y);
	}

	public void sleep(double seconds) {
		try {
			Thread.sleep((int)(seconds*1000));
			
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
			while (!haveIdleWorkers()){
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

	public boolean canClickCommand(int x, int y){
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
	
	public boolean isAtBottomBaseRequest(){
		try {
			BufferedImage bi = screenShot();
			String[] data = server.getBoundingBoxesForScreenshot(bi);
			GameObject[] objects = Util.getGameObjects(data);
			for(int i = 0; i < objects.length; i++){
				if(objects[i].isType("CommandCenter")){
					return objects[i].getCenter().x < bi.getWidth()/2;
				}
			}
			
			System.err.println("NO BASE FOUND!");
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public GameObject[] requestGameObjects(String type){
		try {
			ArrayList<GameObject> filterObjects = new ArrayList<GameObject>();
			BufferedImage bi = screenShot();
			String[] data = server.getBoundingBoxesForScreenshot(bi);
			GameObject[] objects = Util.getGameObjects(data);
			for(int i = 0; i < objects.length; i++){
				System.out.println(objects[i].getClass().getSimpleName());
				if(objects[i].getClass().getSimpleName().equals(type)){
					if(objects[i].getCenter().y < GUI_Y){
						System.out.println("added");
						filterObjects.add(objects[i]);
					}
				}
			}
			
			//Ensure correct type
			for(int i = 0; i < filterObjects.size(); i++){
				GameObject current = filterObjects.get(i);
				leftClick(current);
				sleep(1);
				bi = screenShot();
				sleep(1);
				if(!current.isSelected(bi)){
					System.out.println("removed");
					filterObjects.remove(i);
					i--;
				}
				
				shiftLeftClick(current);
			}

			return filterObjects.toArray(new GameObject[]{});
		} catch (IOException e) {
			return null;
		}
		
	}

	public BufferedImage screenShot() {
		return robot.createScreenCapture(screenSize);
	}
	
	public BufferedImage gamePlayScreenShot(){
		Rectangle gameplay = new Rectangle(0,0,screenSize.width, 7*screenSize.height/10);
		return robot.createScreenCapture(gameplay);
	}

}
