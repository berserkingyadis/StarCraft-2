package runnables;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utilities.AI;
import utilities.GameObject;
import utilities.Resource;

public class Test{
	private AI ai;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Test();
	}

	// Commonly used variables
		private static Resource min50 = new Resource(Resource.MINERALS, 50),
				min100 = new Resource(Resource.MINERALS, 100),
				min150 = new Resource(Resource.MINERALS, 150),
				min200 = new Resource(Resource.MINERALS, 200),
				min250 = new Resource(Resource.MINERALS, 250),
				min300 = new Resource(Resource.MINERALS, 300),
				vesp25 = new Resource(Resource.VESPENE, 25), vesp50 = new Resource(
						Resource.VESPENE, 50), vesp100 = new Resource(
						Resource.VESPENE, 100), vesp200 = new Resource(
						Resource.VESPENE, 200), idleWorker = new Resource(
						Resource.IDLEWORKERS, 1);
	
	public Test() {
		AI ai = new AI();

		// Switch to game
		ai.altTab();

		// Wait for game to start
		ai.waitFor(min50);
		BufferedImage bi = ai.screenShot();
		try {
			ImageIO.write(bi, "png", new File("before_request.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Determine which base we're at
		// and setup accordingly
		GameObject[] cc = ai.requestGameObjects("CommandCenter");
		ai.setMouse(cc[0].getCenter());
		ai.debug("CommandCenter found at " + cc[0].getX() + ", " + cc[0].getY() +"");
		ai.debug("Width " + cc[0].getWidth());
		ai.debug("Height " + cc[0].getHeight());
		ai.debug("Center at " + cc[0].getCenter().x + ", " + cc[0].getCenter().y + "");
		ai.leftClick(cc[0]);
		ai.sleep(0.3);
		try {
			ImageIO.write(ai.screenShot(), "png", new File("after_request.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		ai = new AI();
//		ai.sleep(1);
//		ai.altTab();
//		boolean firstTime = true;
//		while(true){
//			BufferedImage bi = ai.screenShot();
//			if(CommandCenter.isSelected(bi)){
//				firstTime = true;
//				ai.debug("CommandCenter selected");
//			} else if(Armory.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Armory selected");
//			} else if(Barracks.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Barracks selected");
//			} else if(Factory.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Factory selected");
//			} else if(Reactor.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Reactor selected");
//			} else if(Refinery.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Refinery selected");
//			} else if(SupplyDepot.isSelected(bi)){
//				firstTime = true;
//				ai.debug("SupplyDepot selected");
//			} else if(TechLab.isSelected(bi)){
//				firstTime = true;
//				ai.debug("TechLab selected");
//			} else if(Mineral.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Mineral selected");
//			} else if(Vespene.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Vespene selected");
//			} else if(XelNagaTower.isSelected(bi)){
//				firstTime = true;
//				ai.debug("XelNagaTower selected");
//			} else if(Hellbat.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Hellbat selected");
//			} else if(Hellion.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Hellion selected");
//			} else if(Marauder.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Marauder selected");
//			} else if(Marine.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Marine selected");
//			} else if(Reaper.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Reaper selected");
//			} else if(SCV.isSelected(bi)){
//				firstTime = true;
//				ai.debug("SCV selected");
//			} else if(Thor.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Thor selected");
//			} else if(Viking.isSelected(bi)){
//				firstTime = true;
//				ai.debug("Viking selected");
//			} else if(WidowMine.isSelected(bi)){
//				firstTime = true;
//				ai.debug("WidowMine selected");
//			} else if(firstTime){
//				firstTime = false;
//				ai.debug("Nothing selected");
//			} 
//		}
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		BufferedImage im = ai.screenShot();
//		while(!CommandCenter.isSelected(im)){
//			ai.debug("Not Selected");
//			ai.sleep(1);
//			im = ai.screenShot();
//		}
//		
//		ai.debug("Selected!");
	}

}
