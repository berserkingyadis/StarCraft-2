package runnables;
import java.awt.Point;
import java.awt.event.KeyEvent;

import org.omg.CORBA.portable.IDLEntity;

import utilities.AI;
import utilities.Resource;

public class MarineRush {

	private static final double /*
								 * SLOWER = 1.66, SLOW = 1.25, NORMAL = 1, FAST
								 * = 0.8275,
								 */
	FASTER = 0.725;

	private static final double GAME_SPEED = FASTER;
	private static boolean buildOnLeft = true;
	private static int width, height;
	private static final int TILE_X = 40, TILE_Y = 33;
	public static Point BARRACKS, BARRACKS2, BARRACKS3,
				MINERAL, ENEMY, RALLY, RALLY2;

	// Commonly used variables
	private static Resource min50 = new Resource(Resource.MINERALS, 50),
			min100 = new Resource(Resource.MINERALS, 100),
			min150 = new Resource(Resource.MINERALS, 150);

	public static void main(String[] args) {
		AI ai = new AI();
		width = ai.getWidth();
		height = ai.getHeight();

		// Switch to game
		ai.sleep(0.5);
		ai.altTab();

		// Wait for game to start
		ai.waitFor(min50);
		// Build SCV
		ai.type(KeyEvent.VK_BACK_SPACE);
		ai.sleep(0.3);
		ai.leftClick(width / 2, height / 2 - 100);
		ai.type(KeyEvent.VK_S);

		// GROUP 7 <- COMMANDCENTER
		ai.assignToGroup(7);

		for (int i = 0; i < 2; i++) {
			// BUILD SCV
			ai.waitFor(min50);
			ai.type(KeyEvent.VK_S);
		}
		// GROUP 1 - 6 <- BUILDER SCVs
		for (int i = 1; i <= 3; i++) {
			ai.selectAll();
			ai.sleep(0.3);
			ai.leftClick(getArmyUnit(i));
			ai.sleep(0.3);
			ai.assignToGroup(i);
		}

		// Determine which base we're at
		// and setup accordingly
		buildOnLeft = ai.isAtBottomBase();
		if (buildOnLeft) {
			BARRACKS = new Point(180, 500);
			BARRACKS2 = new Point(350, 500);
			BARRACKS3 = new Point(510, 500);
			ENEMY = new Point(70, 590);
			RALLY = new Point(118, 716);
			RALLY2 = new Point(130, 610);
			MINERAL = new Point(width - 390, 425);
		} else {
			BARRACKS = new Point(width - 180, 500);
			BARRACKS2 = new Point(width - 350, 500);
			BARRACKS3 = new Point(width - 510, 500);
			ENEMY = new Point(150, 740);
			RALLY = new Point(105, 615);
			RALLY2 = new Point(100, 725);
			MINERAL = new Point(390, 350);
		}

		// BUILD SUPPLY DEPOT
		ai.debug("BUILD SUPLY");
		ai.selectGroup(1);
		ai.waitFor(new Resource(Resource.MINERALS, 75));
		ai.rightClick(getSupply(1));
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min100);
		ai.type(KeyEvent.VK_S);
		ai.leftClick(getSupply(1));
		
		// SEND SCV TO COLLECT
		ai.shiftRightClick(MINERAL);

		// BUILD SCV
		ai.debug("BUILD 2 SCV");
		ai.selectGroup(7);
		for (int i = 0; i < 2; i++) {
			ai.waitFor(min50);
			ai.type(KeyEvent.VK_S);
		}

		
		// BUILD BARRACKS
		ai.debug("BUILD BARRACKS 1");
		ai.selectGroup(2);
		ai.waitFor(new Resource(Resource.MINERALS,130));
		ai.rightClick(BARRACKS);
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min150);
		ai.type(KeyEvent.VK_B);
		ai.leftClick(BARRACKS);

		// BUILD ONE SCV
		ai.debug("BUILD SCV");
		ai.selectGroup(7);
		ai.waitFor(min50);
		ai.type(KeyEvent.VK_S);

		// Build barracks 2
		ai.debug("BUILD BARRACKS 2");
		ai.setMouse(BARRACKS2);
		ai.selectGroup(1);
		ai.waitFor(new Resource(Resource.MINERALS, 130));
		ai.rightClick(BARRACKS2);
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min150);
		ai.type(KeyEvent.VK_B);
		ai.leftClick(BARRACKS2);
		ai.shiftRightClick(MINERAL);

		// Build barracks 3
		ai.debug("BUILD BARRACKS 3");
		ai.setMouse(BARRACKS3);
		ai.selectGroup(3);
		ai.waitFor(new Resource(Resource.MINERALS, 130));
		ai.rightClick(BARRACKS3);
		ai.type(KeyEvent.VK_B);
		ai.waitFor(min150);
		ai.type(KeyEvent.VK_B);
		ai.leftClick(BARRACKS3);
		ai.shiftRightClick(MINERAL);

		// Make marine
		ai.debug("MAKE MARINE");
		ai.waitFor(new Resource(Resource.IDLEWORKERS, 1));
		ai.leftClick(BARRACKS);
		ai.waitFor(min50);
		ai.type(KeyEvent.VK_A);
		ai.selectGroup(2);
		ai.leftClick(MINERAL);
		
		// ORBITAL COMMAND
		ai.debug("ORBITAL COMMAND");
		ai.selectGroup(7);
		ai.waitFor(min150);
		ai.type(KeyEvent.VK_B);
		
		// Group 8 <- Barracks
		ai.debug("ASSIGN RALLY");
		ai.sleep(1);
		ai.leftClick(BARRACKS);
		ai.leftClick(BARRACKS);
		ai.sleep(0.3);
		ai.assignToGroup(8);
		ai.rightClick(RALLY2);

		// Make Marines until capped
		ai.debug("Make marines til supply capped");
		int cur = ai.getResourceAmount(Resource.CURSUPPLY);
		int max = ai.getResourceAmount(Resource.MAXSUPPLY);
		ai.debug(cur + " out of " + max);
		while(cur != max - 1){
			ai.waitFor(min50);
			ai.type(KeyEvent.VK_A);
			cur = ai.getResourceAmount(Resource.CURSUPPLY);
			max = ai.getResourceAmount(Resource.MAXSUPPLY);
		}

		// LOWER SUPPLY
		ai.debug("LOWER SUPPLY");
		ai.sleep(0.3);
		ai.leftClick(getSupply(1));
		ai.leftClick(getSupply(1));
		ai.sleep(0.3);
		ai.type(KeyEvent.VK_R);

		// Call down supplies
		ai.debug("SUPPLY");
		ai.selectGroup(7);
		ai.type(KeyEvent.VK_X);
		ai.sleep(1.5);
		ai.leftClick(getSupply(1));

		long start = System.currentTimeMillis() / 1000;
		while(System.currentTimeMillis() / 1000 - start < realToGameTime(70)){
			ai.selectGroup(8);
			ai.waitFor(min50);
			ai.type(KeyEvent.VK_A);
		}
		
		ai.selectGroup(8);
		ai.rightClick(ENEMY);
		
		//Build one more supply
		ai.debug("BUILDING SUPPLY 2");
		ai.waitFor(min100);
		ai.selectGroup(7);
		ai.selectGroup(7);
		ai.sleep(0.3);
		ai.selectGroup(1);
		ai.type(KeyEvent.VK_B);
		ai.sleep(0.3);
		ai.type(KeyEvent.VK_S);
		ai.sleep(0.3);
		ai.leftClick(getSupply(2));
		ai.shiftRightClick(MINERAL);
		
		// END GAME
		ai.debug("END GAME");
		while(ai.getResourceAmount(Resource.MINERALS) > 0){
			// PREPARE ATTACK
			ai.type(KeyEvent.VK_F2);
			ai.type(KeyEvent.VK_A);
			ai.leftClick(ENEMY);
			
			//Watch
			ai.type(KeyEvent.VK_F2);
			ai.type(KeyEvent.VK_F2);
			ai.sleep(0.3);

			//Try To build more marines
			if(ai.getResourceAmount(Resource.MINERALS) > 50){
				ai.selectGroup(8);
				ai.type(KeyEvent.VK_A);
			}

			//Try to accept surrender
			ai.leftClick(new Point(1160, 120));
			
		}

	}

	private static Point getSupply(int num) {
		int useWidth = 0;
		int reverse = -1;
		if (buildOnLeft) {
			useWidth = 1;
			reverse = 1;
		}
		num--;
		int x = 860;
		int y = 275;
		int xOffset = 2 * TILE_X * (num % 4) - 10 * (num / 4);
		int yOffset = 2 * TILE_Y * (num / 4);
		return new Point(useWidth * width - (x + xOffset) * reverse, y
				+ yOffset);
	}

	private static double realToGameTime(double time) {
		return time * GAME_SPEED;
	}

	private static Point getArmyUnit(int num) {
		num--;
		return new Point(490 + 40 * (num % 8), 650 + 40 * (num / 8));
	}

}
