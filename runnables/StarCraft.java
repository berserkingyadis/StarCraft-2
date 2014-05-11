package runnables;
import java.awt.Point;
import java.awt.event.KeyEvent;

import utilities.AI;
import utilities.Resource;


public class StarCraft {
	
	
	private static final double /*SLOWER = 1.66,
								SLOW = 1.25, 
								NORMAL = 1,
								FAST = 0.8275, */
								FASTER = 0.725;
	
	private static final double GAME_SPEED = FASTER;
	private static boolean buildOnLeft = true;
	private static int width, height;
	private static final int TILE_X = 40, TILE_Y = 33;
	public static Point BARRACKS, BARRACKS2, REFINERY, ARMORY,
						ENEMY, FACTORY, REFINERY2, RALLY, RALLY2;
	
	//Commonly used variables
	private static Resource min50 = new Resource(Resource.MINERALS, 50),
							min100 = new Resource(Resource.MINERALS, 100),
							min150 = new Resource(Resource.MINERALS, 150),
							min200 = new Resource(Resource.MINERALS, 200),
							min250 = new Resource(Resource.MINERALS, 250),
							min300 = new Resource(Resource.MINERALS, 300),
							vesp25 = new Resource(Resource.VESPENE, 25),
							vesp50 = new Resource(Resource.VESPENE, 50),
							vesp100 = new Resource(Resource.VESPENE, 100),
							vesp200 = new Resource(Resource.VESPENE, 200),
							idleWorker = new Resource(Resource.IDLEWORKERS, 1);
	
	
	public static void main(String[] args) {
		AI ai = new AI();
		width = ai.getWidth();
		height = ai.getHeight();

		//Switch to game
		ai.sleep(0.5);
		ai.altTab();
		
		//Wait for game to start
		ai.waitFor(min50);
		//Build SCV
		ai.type(KeyEvent.VK_BACK_SPACE);
		ai.sleep(0.3);
		ai.leftClick(width/2, height/2 - 100);
		ai.type(KeyEvent.VK_S);
		
		//GROUP 7 <- COMMANDCENTER
		ai.assignToGroup(7);
		
		//Determine which base we're at
		//and setup accordingly
		buildOnLeft = ai.isAtBottomBase();
		if(buildOnLeft){ 
			BARRACKS = new Point(180, 500);
			BARRACKS2 = new Point(430, 500);
			REFINERY = new Point(width/2 + 100, 100);
			REFINERY2 = new Point(width/2 + 100, 575);
			FACTORY = new Point(250, 385);
			ARMORY = new Point(585, 176);
			ENEMY = new Point(70, 590);
			RALLY = new Point(118, 716);
			RALLY2 = new Point(130, 610);
		} else {
			BARRACKS = new Point(width - 180, 500);
			BARRACKS2 = new Point(width - 430, 500);
			REFINERY = new Point(width/2 - 50, 100);
			REFINERY2 = new Point(width/2 - 50, 575);
			FACTORY = new Point(width - 250, 385);
			ARMORY = new Point(width - 585, 176);
			ENEMY = new Point(150, 740);
			RALLY = new Point(105, 615);
			RALLY2 = new Point(100, 725);
		}
		
		ai.selectGroup(7);
		for(int i = 0; i < 3; i++){
			//BUILD SCV
			ai.waitFor(min50);
			ai.type(KeyEvent.VK_S);
		}
		//GROUP 1 - 6 <- BUILDER SCVs
		for(int i = 1; i <= 6; i++){
			ai.selectAll();
			ai.sleep(0.3);
			ai.leftClick(getArmyUnit(i));
			ai.sleep(0.3);
			ai.assignToGroup(i);
		}
		
		//BUILD SUPPLY DEPOT
		ai.selectGroup(1);
		ai.waitFor(new Resource(Resource.MINERALS, 70));
		ai.rightClick(getSupply(1));
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min100);
		ai.type(KeyEvent.VK_S);
		ai.leftClick(getSupply(1));
		
		//BUILD SCV
		ai.selectGroup(7);
		ai.waitFor(min50);
		ai.type(KeyEvent.VK_S);
		
		//BUILD BARRACKS
		ai.selectGroup(1);
		ai.shiftRightClick(BARRACKS);
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min150);
		ai.type(KeyEvent.VK_B);
		ai.leftClick(BARRACKS);
		
		//Build Refinery
		ai.debug("Build Refinery");
		ai.sleep(1);
		ai.selectGroup(3);
		ai.waitFor(new Resource(Resource.MINERALS, 65));
		ai.rightClick(REFINERY);
		ai.type(KeyEvent.VK_B);
		ai.setMouse(REFINERY);
		ai.waitFor(new Resource(Resource.MINERALS, 75));
		ai.type(KeyEvent.VK_R);
		ai.sleep(1);
		ai.shiftLeftClick(REFINERY);
		
		//BUILD TWO SCVs
		ai.debug("Two SCVs");
		ai.selectGroup(7);
		for(int i = 0; i < 2; i++){
			ai.waitFor(min50);
			ai.type(KeyEvent.VK_S);
		}
		
		//GROUP 8 <- BARRACKS
		ai.leftClick(BARRACKS);
		ai.assignToGroup(8);
		
		//SCVs
		ai.debug("3 SCVs");
		ai.selectGroup(7);
		for(int i = 0; i < 3; i++){
			ai.waitFor(min50);
			ai.type(KeyEvent.VK_S);
		}

		//Send workers to refinery
		ai.debug("SCVs to Refinery");
		ai.selectGroup(4);
		ai.rightClick(REFINERY);
		ai.selectGroup(5);
		ai.rightClick(REFINERY);
		
		//Build barracks 2
		ai.debug("Build Barracks 2");
		ai.setMouse(BARRACKS2);
		ai.selectGroup(1);
		ai.shiftLeftClick(BARRACKS2);
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min150);
		ai.type(KeyEvent.VK_B);
		ai.leftClick(BARRACKS2);
		
		//BUILD REACTOR
		ai.debug("Reactor on barracks 1");
		ai.leftClick(BARRACKS);
		ai.waitFor(min50);
		ai.waitFor(vesp50);
		while(ai.canClickCommand(4, 2));
		ai.type(KeyEvent.VK_C);
		ai.sleep(0.5);
		ai.leftClick(BARRACKS);
		
		
		
		//ORBITAL COMMAND
		ai.debug("orbital command");
		ai.selectGroup(7);
		ai.waitFor(min150);
		while(ai.canClickCommand(4, 2));
		ai.type(KeyEvent.VK_B);

		//Group 4 <- both barracks
		ai.selectGroup(8);
		ai.shiftLeftClick(BARRACKS2);
		ai.assignToGroup(8);
		ai.rightClick(RALLY);
		
		//BUILD REACTOR 2
		ai.debug("reactor 2");
		ai.leftClick(BARRACKS2);
		ai.waitFor(min50);
		ai.waitFor(vesp50);
		while(ai.canClickCommand(4, 2));
		ai.type(KeyEvent.VK_C);
		ai.sleep(0.5);
		ai.leftClick(BARRACKS2);

		//Send first scv to refinery
		ai.debug("SCV on Barracks 2 to refinery 2");
		ai.selectGroup(1);
		ai.shiftRightClick(REFINERY2);
		
		//BUILD FACTORY
		ai.debug("Factory");
		ai.selectGroup(6);
		ai.waitFor(min150);
		ai.waitFor(vesp100);
		ai.type(KeyEvent.VK_V);
		ai.type(KeyEvent.VK_F);
		ai.sleep(0.3);
		ai.leftClick(FACTORY);
		
		//Build supply 2
		ai.debug("Supply Depot 2");
		ai.selectGroup(2);
		ai.waitFor(new Resource(Resource.MINERALS, 65));
		ai.rightClick(getSupply(2));
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min100);
		ai.type(KeyEvent.VK_S);
		ai.leftClick(getSupply(2));

		//SCVs
		ai.debug("3 scvs");
		ai.selectGroup(7);
		for(int i = 0; i < 3; i++){
			ai.waitFor(min50);
			ai.type(KeyEvent.VK_S);
		}
		
		//SEND SCV 2 TO REFINERY
		ai.debug("Queue up refinery 2");
		ai.selectGroup(2);
		ai.type(KeyEvent.VK_B);
		ai.waitFor(new Resource(Resource.MINERALS, 75));
		ai.type(KeyEvent.VK_R);
		ai.setMouse(REFINERY2);
		ai.sleep(0.3);
		ai.shiftLeftClick(REFINERY2);
		
		//LOWER SUPPLY
		ai.sleep(0.3);
		ai.leftClick(getSupply(1));
		ai.leftClick(getSupply(1));
		ai.sleep(0.3);
		ai.type(KeyEvent.VK_R);

		//BUILD ARMORY
		ai.debug("armory");
		ai.selectGroup(6);
		ai.shiftRightClick(ARMORY);
		ai.leftClick(FACTORY);
		ai.waitFor(min150);
		ai.waitFor(vesp100);
		while(ai.canClickCommand(4, 2));
		ai.selectGroup(6);
		ai.type(KeyEvent.VK_V);
		ai.type(KeyEvent.VK_A);
		ai.sleep(0.3);
		ai.leftClick(ARMORY);
		
		//BUILD TECHLAB ON FACTORY
		ai.debug("techlab");
		ai.sleep(1);
		ai.leftClick(FACTORY);
		ai.assignToGroup(9);
		ai.rightClick(RALLY);
		ai.waitFor(min50);
		ai.waitFor(vesp25);
		ai.type(KeyEvent.VK_X);
		ai.sleep(0.3);
		ai.leftClick(FACTORY);
		
		//Build marines
		ai.debug("4 marines");
		ai.leftClick(BARRACKS);
		while(ai.canClickCommand(4, 2));
		for(int i = 0; i < 4; i++){
			ai.waitFor(min50);
			ai.type(KeyEvent.VK_A);
		}
		ai.leftClick(BARRACKS2);
		while(ai.canClickCommand(4, 2));
		ai.selectGroup(8);
		for(int i = 0; i < 6; i++){
			ai.waitFor(min50);
			ai.type(KeyEvent.VK_A);
		}
			
		//BUILD SUPPLY 3
		ai.debug("Supply 3");
		ai.selectGroup(6);
		ai.waitFor(new Resource(Resource.MINERALS, 70));
		ai.shiftRightClick(getSupply(3));
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min100);
		ai.type(KeyEvent.VK_S);
		ai.shiftLeftClick(getSupply(3));
		
		//Call down supplies
		ai.debug("call down");
		ai.sleep(1);
		ai.selectGroup(7);
		for(int i = 0; i < 2; i++){
			ai.type(KeyEvent.VK_X);
			ai.sleep(0.5);
			ai.leftClick(getSupply(i+1));
		}
		//SCVs
		ai.debug("4 SCVs");
		ai.selectGroup(7);
		for(int i = 0; i < 4; i++){
			ai.waitFor(min50);
			ai.type(KeyEvent.VK_S);
		}
				
		//BUILD SUPPLY 4
		ai.debug("supply 4");
		ai.selectGroup(6);
		ai.waitFor(new Resource(Resource.MINERALS, 70));
		ai.shiftRightClick(getSupply(4));
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min100);
		ai.type(KeyEvent.VK_S);
		ai.sleep(0.3);
		ai.shiftLeftClick(getSupply(4));
		ai.shiftRightClick(REFINERY2);
		
		//Call down supplies
		ai.debug("call down");
		ai.selectGroup(7);
		ai.type(KeyEvent.VK_X);
		ai.sleep(0.3);
		ai.leftClick(getSupply(3));
		
		//BUILD THORS
		ai.debug("thor");
		ai.selectGroup(9);
		ai.waitFor(min300);
		ai.waitFor(vesp200);
		while(!ai.canClickCommand(4, 0));
		ai.type(KeyEvent.VK_T);
		long thorStart = System.currentTimeMillis();
	
		//END GAME
		ai.debug("END GAME");
		for(int wave = 0; wave < 2; wave++){

			if(wave == 1){
				while(System.currentTimeMillis() - thorStart < realToGameTime(60000));
			}

			//PREPARE ATTACK
			ai.type(KeyEvent.VK_F2);
			ai.type(KeyEvent.VK_F2);
			ai.type(KeyEvent.VK_A);
			if(wave == 0){
				ai.leftClick(RALLY);
			} else {
				ai.leftClick(ENEMY);
			}

			ai.type(KeyEvent.VK_F2);
			ai.type(KeyEvent.VK_F2);

			//BUILD THORS
			ai.selectGroup(9);
			ai.waitFor(min300);
			ai.waitFor(vesp200);
			ai.type(KeyEvent.VK_T);

			//BUILD MARINES
			for(int m = 0; m < 8; m++){

				ai.type(KeyEvent.VK_F2);
				ai.type(KeyEvent.VK_F2);
				ai.selectGroup(8);
				ai.waitFor(min50);
				ai.type(KeyEvent.VK_A);
			}

			//Watch
			for(int j = 0; j < 60; j++){
				ai.type(KeyEvent.VK_F2);
				ai.type(KeyEvent.VK_A);
				ai.leftClick(ENEMY);
				ai.type(KeyEvent.VK_F2);
				ai.type(KeyEvent.VK_F2);
				ai.sleep(0.5);
			}
		}
		
		//ACCEPT SURRENDER
		for(int i = 0; i < 6; i++){
			ai.sleep(5);
			ai.leftClick(new Point(1160, 120));
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
	
	private static double realToGameTime(double time){
		return time * GAME_SPEED;
	}
	
	private static Point getArmyUnit(int num){
		num--;
		return new Point(490 + 40 *(num % 8), 650 + 40 *(num / 8) );
	}

}
