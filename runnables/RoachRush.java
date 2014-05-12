package runnables;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import utilities.AI;
import utilities.GameObject;
import utilities.Resource;


public class RoachRush {
	
	
	private static final double /*SLOWER = 1.66,
								SLOW = 1.25, 
								NORMAL = 1,
								FAST = 0.8275, */
								FASTER = 0.725;
	private static long lastInject = 0;
	private static final double GAME_SPEED = FASTER;
	private static boolean buildOnLeft = true;
	private static int width, height;
	private static final int TILE_X = 40, TILE_Y = 33;
	public static Point SPAWNINGPOOL, ROACHWARREN, EXTRACTOR, EXTRACTOR2, RALLY, RALLY2, BASE,
						MINERAL;
	public static Point[] BASES = new Point[]{
		new Point(150, 740),
		new Point(90, 735),
		new Point(188, 705),
		new Point(138, 673),
		new Point(90, 662),
		new Point(40, 630),
		new Point(125, 600),
		new Point(66, 595)
	};
	
	//Commonly used variables
	private static Resource min25 = new Resource(Resource.MINERALS, 25),
							min50 = new Resource(Resource.MINERALS, 50),
							min75 = new Resource(Resource.MINERALS, 75),
							min100 = new Resource(Resource.MINERALS, 100),
							min150 = new Resource(Resource.MINERALS, 150),
							min200 = new Resource(Resource.MINERALS, 200),
							vesp25 = new Resource(Resource.VESPENE, 25),
							vesp100 = new Resource(Resource.VESPENE, 100);
	
	
	public static void main(String[] args) {
		AI ai = new AI();
		width = ai.getWidth();
		height = ai.getHeight();

		//Switch to game
		ai.altTab();
		ai.sleep(0.5);
		
		while(true){
			//Wait for game to start
			ai.waitFor(min50);
			long gameStart = System.currentTimeMillis();
	
			//GROUP 1 <- hatchery
			ai.type(KeyEvent.VK_BACK_SPACE);
			ai.sleep(0.3);
			ai.leftClick(width/2, height/2 - 100);
			ai.assignToGroup(1);
			
			//4 drones
			for(int i = 0; i < 4; i++){
				makeDrone(ai);	
			}
			
			//Overlord
			makeOverlord(ai);
			
			
			//5 drones
			for(int i = 0; i < 4; i++){
				makeDrone(ai);
			}
			//Determine which base we're at
			//and setup accordingly
			buildOnLeft = ai.isAtBottomBase();
			if(buildOnLeft){ 
				SPAWNINGPOOL = new Point(1065, 220);
				EXTRACTOR = new Point(width/2 + 100, 100);
				EXTRACTOR2 = new Point(width/2 + 100, 575);
				ROACHWARREN = new Point(460, 320);
				RALLY = new Point(118, 716);
				RALLY2 = new Point(130, 610);
				BASE = new Point(149, 737);
				MINERAL = new Point(960, 270);
			} else {
				SPAWNINGPOOL = new Point(270, 400);
				EXTRACTOR = new Point(width/2 - 50, 100);
				EXTRACTOR2 = new Point(width/2 - 50, 575);
				ROACHWARREN = new Point(900, 320);
				RALLY = new Point(105, 615);
				RALLY2 = new Point(100, 725);
				BASE = new Point(73, 595);
				MINERAL = new Point(390, 350);
			}
			
			//BUILD Extractor
			while(System.currentTimeMillis() - gameStart < realToGameTime(105000));
			buildExtractor(ai, 1);
			long extractorStart = System.currentTimeMillis();

			//SPAWNING POOL
			buildSpawningPool(ai);
			
			//BUILD 3 DRONES
			for(int i = 0; i < 3; i++){
				makeDrone(ai);
			}
			
			//Wait for extractor to finish
			while(System.currentTimeMillis() - extractorStart < realToGameTime(30000));
			
			//Send 3 drones to extractor
			for(int i = 1; i <= 3; i++){
				ai.selectAll();
				ai.sleep(0.1);
				ai.leftClick(getArmyUnit(i + 2));
				ai.rightClick(EXTRACTOR);
			}
			
			//DRONE
			makeDrone(ai);
			
			//BUILD QUEEN
			ai.leftClick(SPAWNINGPOOL);
			ai.sleep(0.3);
			while(!ai.canClickCommand(0, 0));
			ai.selectGroup(1);
			ai.waitFor(min150);
			ai.type(KeyEvent.VK_Q);
			ai.rightClick(RALLY);
			ai.sleep(0.1);
			
			//BUILD ROACH WARREN
			ai.selectAll();
			ai.sleep(0.1);
			ai.leftClick(getArmyUnit(6));
			ai.type(KeyEvent.VK_B);
			ai.waitFor(new Resource(Resource.MINERALS, 125));
			ai.rightClick(ROACHWARREN);
			ai.waitFor(new Resource(Resource.MINERALS, 150));
			ai.type(KeyEvent.VK_R);
			ai.waitFor(new Resource(Resource.IDLEWORKERS, 1));
			ai.sleep(0.1);
			ai.leftClick(ROACHWARREN);		
			long roachWarrenDown = System.currentTimeMillis();
			//make overlord
			for(int i = 0; i < 2; i++){
				makeOverlord(ai);
			}
			
			//Group 2 <- Queen
			ai.selectGroup(1);
			ai.sleep(0.3);
			while(ai.canClickCommand(4, 2));
			ai.selectAll();
			ai.sleep(0.3);
			ai.leftClick(getArmyUnit(1));
			ai.assignToGroup(2);
			
			//Inject
			tryToInject(ai);
			
			//make roaches
			long roachStart = 0;
			while(System.currentTimeMillis() - roachWarrenDown < realToGameTime(55000));
			for(int i = 0; i < 9; i++){
				makeRoach(ai);
				ai.clearScreenshot();
				roachStart = System.currentTimeMillis();
				tryToInject(ai);
			}
			
			//Overlord?
			makeOverlord(ai);
			
			//Wait for last roach to finish
			while(System.currentTimeMillis() - roachStart < realToGameTime(27000));
			
			//Attack
			ai.type(KeyEvent.VK_F2);
			ai.type(KeyEvent.VK_A);
			for(int i = 0; i < BASES.length; i++){
				if(buildOnLeft){
					ai.shiftLeftClick(BASES[BASES.length - i - 1]);
				} else {
					ai.shiftLeftClick(BASES[i]);
				}
			}
			
			while(ai.getResourceAmount(Resource.MAXSUPPLY) > 0){
				//Watch
				ai.type(KeyEvent.VK_F2);
				ai.type(KeyEvent.VK_F2);
				ai.type(KeyEvent.VK_A);
				for(int i = 0; i < BASES.length; i++){
					if(buildOnLeft){
						ai.shiftLeftClick(BASES[BASES.length - i - 1]);
					} else {
						ai.shiftLeftClick(BASES[i]);
					}
				}
				
				
				
				//Inject
				if(tryToInject(ai)){
					for(int i = 0; i < 6; i++){
						//Reinforce
						makeOverlordIfNeeded(ai);
						tryToMakeRoach(ai);
						ai.type(KeyEvent.VK_F2);
						ai.type(KeyEvent.VK_F2);
					}
					
				}
				ai.type(KeyEvent.VK_F2);
				ai.type(KeyEvent.VK_F2);
				
				//ACCEPT SURRENDER
				ai.leftClick(new Point(1160, 120));
			
			}
			//Go to score screen
			ai.sleep(2);
			ai.leftClick(new Point(680, 475));
			//Play again
			ai.sleep(20);
			ai.leftClick(new Point(310, 635));
			//Add ai
			ai.sleep(20);
			ai.leftClick(new Point(925, 95));
			//Start Game
			ai.sleep(5);
			ai.leftClick(new Point(565, 625));
		}
	}
	
	private static Point getSupply(int num){
		int useWidth = 1;
		int reverse = 1;
		if(buildOnLeft){
			useWidth = 0;
			reverse = -1;
		}
		num--;
		int x = 225;
		int y = 100;
		int xOffset = 2*TILE_X * (num % 4) - 10 * ( num/4 );
		int yOffset = 2*TILE_Y * (num / 4);
		return new Point(useWidth * width - (x+xOffset) * reverse , y+yOffset);
	}
	
	private static boolean tryToInject(AI ai){
		if(System.currentTimeMillis() - lastInject >= realToGameTime(40000)){
			ai.selectGroup(2);
			ai.type(KeyEvent.VK_V);
			ai.leftClick(BASE);
			lastInject = System.currentTimeMillis();
			return true;
		}
		return false;
	}
	
	private static void tryToMakeZergling(AI ai){
		ai.selectGroup(1);
		ai.sleep(0.3);
		if(GameObject.hasLarva(ai.screenshot())){
			if(ai.getResourceAmount(Resource.MINERALS) >= 50){
				if(ai.getResourceAmount(Resource.CURSUPPLY) != 
						ai.getResourceAmount(Resource.MAXSUPPLY)){
					ai.type(KeyEvent.VK_S);
					ai.type(KeyEvent.VK_Z);
				}
			}
		}
	}
	
	private static void tryToMakeRoach(AI ai){
		ai.selectGroup(1);
		ai.sleep(0.3);
		if(ai.getResourceAmount(Resource.MINERALS) >= 75 &&
				ai.getResourceAmount(Resource.VESPENE) >= 25 &&
				GameObject.hasLarva(ai.screenshot()) && 
				ai.getResourceAmount(Resource.CURSUPPLY) < 
				ai.getResourceAmount(Resource.MAXSUPPLY) - 1){
			ai.type(KeyEvent.VK_S);
			ai.type(KeyEvent.VK_R);
		}
	}
	
	private static void makeDrone(AI ai){
		ai.selectGroup(1);
		while(ai.getResourceAmount(Resource.CURSUPPLY) == 
				ai.getResourceAmount(Resource.MAXSUPPLY));
		ai.waitFor(min50);
		while(!GameObject.hasLarva(ai.screenshot()));
		ai.type(KeyEvent.VK_S);
		ai.type(KeyEvent.VK_D);
	}
	
	private static void makeZergling(AI ai){
		
		ai.selectGroup(1);
		while(ai.getResourceAmount(Resource.CURSUPPLY) == 
				ai.getResourceAmount(Resource.MAXSUPPLY));
		ai.waitFor(min50);
		BufferedImage bi = ai.screenshot();
		ai.sleep(0.3);
		while(!GameObject.hasLarva(bi)){
			bi = ai.screenshot();
			ai.sleep(0.3);
		}
		ai.type(KeyEvent.VK_S);
		ai.type(KeyEvent.VK_Z);
	}
	
	private static void makeRoach(AI ai){
		
		ai.selectGroup(1);
		while(ai.getResourceAmount(Resource.CURSUPPLY) >= 
				ai.getResourceAmount(Resource.MAXSUPPLY) - 1);
		ai.waitFor(min75);
		ai.waitFor(vesp25);
		while(!GameObject.hasLarva(ai.screenshot()));
		ai.type(KeyEvent.VK_S);
		ai.type(KeyEvent.VK_R);
	}
	
	private static void makeOverlord(AI ai){
		ai.selectGroup(1);
		ai.sleep(0.1);
		ai.waitFor(min100);
		while(!GameObject.hasLarva(ai.screenshot()));
		ai.type(KeyEvent.VK_S);
		ai.type(KeyEvent.VK_V);
	}
	
	private static void makeOverlordIfNeeded(AI ai){
		ai.selectGroup(1);
		ai.sleep(0.1);
		if(GameObject.hasLarva(ai.screenshot())){
			if(ai.getResourceAmount(Resource.MINERALS) >= 100){
				if(ai.getResourceAmount(Resource.CURSUPPLY) >= 
						ai.getResourceAmount(Resource.MAXSUPPLY) - 1){
					ai.type(KeyEvent.VK_S);
					ai.type(KeyEvent.VK_V);
				}
			}
		}
	}
	
	private static void buildExtractor(AI ai, int num){
		ai.selectAll();
		ai.sleep(0.1);
		ai.leftClick(getArmyUnit(10));
		if(num == 1){ai.rightClick(EXTRACTOR);}
		else {ai.rightClick(EXTRACTOR2);}
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min25);
		ai.type(KeyEvent.VK_E);
		ai.sleep(0.1);
		if(num == 1){ai.leftClick(EXTRACTOR);}
		else {ai.leftClick(EXTRACTOR2);}
	}
	
	private static void buildSpawningPool(AI ai){
		ai.selectAll();
		ai.waitFor(new Resource(Resource.MINERALS, 175));
		ai.sleep(0.1);
		ai.leftClick(getArmyUnit(4));
		ai.rightClick(SPAWNINGPOOL);
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min200);
		ai.type(KeyEvent.VK_S);
		ai.sleep(0.1);
		ai.leftClick(SPAWNINGPOOL);
	}
	
	private static double realToGameTime(double time){
		return time * GAME_SPEED;
	}
	
	private static Point getArmyUnit(int num){
		num--;
		return new Point(490 + 40 *(num % 8), 650 + 40 *(num / 8) );
	}

}
