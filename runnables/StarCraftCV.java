package runnables;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import utilities.AI;
import utilities.GameObject;
import utilities.Resource;
import utilities.Util;
import buildings.CommandCenter;

public class StarCraftCV {

	private static final double /*
								 * SLOWER = 1.66, SLOW = 1.25, NORMAL = 1, FAST
								 * = 0.8275,
								 */
	FASTER = 0.725;

	private static final double GAME_SPEED = FASTER;
	private static boolean buildOnLeft = true;
	private static int width, height, mineralIndex;
	private static AI ai;
	public static Point BARRACKS, REFINERY, REFINERY2, FACTORY,
			FACTORY2, SUPPLY, SUPPLY2, SUPPLY3;
	public static Rectangle MINERS;
	private static Rectangle[] bases = null;
	private static int homeBaseIndex, enemyBaseIndex;
	public static GameObject[] minerals = null;
	
	public static void main(String[] args) {
		ai = new AI();
		width = ai.getWidth();
		height = ai.getHeight();

		// Switch to game
		ai.sleep(0.5);
		ai.altTab();
		
		
		// Wait for game to start
		ai.waitFor(Resource.mineral(50));
		
		//Scout for enemy
		scoutForEnemy();
		
		// Start detector
		ai.startDetectorThread();
		
		// Determine which base we're at
		// and setup accordingly
		ai.clearUnitRequests();
		ai.addUnitRequest("buildings.CommandCenter");
		GameObject[] ccs = ai.requestGameObjects();
		while(ccs.length != 1){
			ai.sleep(0.5);
			ccs = ai.requestGameObjects();
		}
		
		ai.clearUnitRequests();
		ai.addUnitRequest("resources.Vespene");
		CommandCenter cc = (CommandCenter)ccs[0];
		if(cc.getCenter().x > width/2){ buildOnLeft = false; }
		// Build SCV
		ai.leftClick(cc.getCenter());
		// GROUP 7 <- COMMANDCENTER
		ai.assignToGroup(1);
		
		for (int i = 0; i < 5; i++) {
			// BUILD SCV
			ai.waitFor(Resource.mineral(50));
			ai.type(KeyEvent.VK_S);
			ai.sleep(0.3);
		}
		
		//Suply Depot
		REFINERY = null;
		while(REFINERY == null){
			GameObject[] geysers = ai.requestGameObjects();
			for(int i = 0; i < geysers.length; i++){
				if(geysers[i].getCenter().y < height/2){
					REFINERY = geysers[i].getCenter();
				}
			}
		}
		ai.clearUnitRequests();
		ai.addUnitRequest("resources.Mineral");
		REFINERY2 = new Point(REFINERY.x, height - REFINERY.y - 100);
		SUPPLY = new Point(REFINERY.x + (buildOnLeft ? -125 : 125), REFINERY.y);
		SUPPLY2 = new Point(SUPPLY.x + (buildOnLeft ? -125 : 125), SUPPLY.y);
		SUPPLY3 = new Point(SUPPLY.x + (buildOnLeft ? -350 : 350), SUPPLY.y);
		ai.selectAll();
		ai.waitFor(Resource.mineral(70));
		ai.setMouse(SUPPLY);
		ai.type(KeyEvent.VK_B);
		ai.sleep(0.3);
		ai.waitFor(Resource.mineral(100));
		ai.type(KeyEvent.VK_S);
		ai.leftClick(SUPPLY);
		ai.sleep(3);
		ai.leftClick(SUPPLY);
		ai.type(KeyEvent.VK_Q);
		minerals = ai.requestGameObjects();
		boolean valid = false;
		while(!valid){
			if(minerals.length > 0){
				if(minerals[0].isType("resources.Mineral")){
					valid = true;
				}
			}
			if(!valid){
				minerals = ai.requestGameObjects();
			}
		}
		queueToMinerals();
		ai.selectGroup(2);
		queueToMinerals();

		//Make 4 SCVs
		ai.selectGroup(1);
		while(ai.getResourceAmount(Resource.MAXSUPPLY) < 19);
		for (int i = 0; i < 4; i++) {
			// BUILD SCV
			ai.waitFor(Resource.mineral(50));
			ai.type(KeyEvent.VK_S);
			ai.sleep(0.3);
		}
		
		//Make REFINERYs
		ai.selectAll();
		ai.waitFor(Resource.mineral(130));
		ai.setMouse(REFINERY);
		ai.type(KeyEvent.VK_B);
		ai.waitFor(Resource.mineral(150));
		ai.type(KeyEvent.VK_R);
		ai.sleep(0.3);
		ai.leftClick(REFINERY);
		ai.setMouse(REFINERY2);
		ai.type(KeyEvent.VK_B);
		ai.type(KeyEvent.VK_R);
		ai.sleep(0.3);
		ai.leftClick(REFINERY2);
		long refineryStart = System.currentTimeMillis();
		
		//Make Barracks
		if(buildOnLeft){
			MINERS = new Rectangle(cc.getX() + cc.getWidth() + 50, REFINERY.y + 150, width/3, height/3);
		} else {
			MINERS = new Rectangle(200, REFINERY.y + 150, cc.getX() - 250, height/3);
		}
		ai.selectArea(MINERS);
		BARRACKS = new Point(cc.getX() + (buildOnLeft ? -100 : cc.getWidth() + 100), cc.getCenter().y);
		ai.waitFor(Resource.mineral(130));
		ai.setMouse(BARRACKS);
		ai.type(KeyEvent.VK_B);
		ai.waitFor(Resource.mineral(150));
		ai.type(KeyEvent.VK_B);
		ai.leftClick(BARRACKS);
		ai.sleep(5);
		ai.leftClick(BARRACKS);
		ai.type(KeyEvent.VK_Q);
		queueToMinerals();
		long barracksStart = System.currentTimeMillis();
		
		//Send workers to vespene
		while(System.currentTimeMillis() - refineryStart < realToGameTime(30000)){
			ai.sleep(0.1);
		}
		for(int i = 0; i < 4; i++){
			ai.selectArea(MINERS);
			ai.leftClick(getArmyUnit(i+1));
			ai.rightClick((i % 2 == 0 ? REFINERY : REFINERY2));
		}
		
		//Make other workers
		ai.selectGroup(1);
		for (int i = 0; i < 3; i++) {
			// BUILD SCV
			ai.waitFor(Resource.mineral(50));
			ai.type(KeyEvent.VK_S);
		}
		
		//Build Factories
		FACTORY = new Point(BARRACKS.x, BARRACKS.y - 150);
		FACTORY2 = new Point(BARRACKS.x, BARRACKS.y + 150);
		ai.selectAll();
		ai.selectArea(MINERS);
		ai.type(KeyEvent.VK_V);
		ai.setMouse(FACTORY);
		while(System.currentTimeMillis() - barracksStart < realToGameTime(67000)){
			ai.sleep(0.1);
		}
		ai.waitFor(Resource.vespene(200));
		ai.waitFor(Resource.mineral(300));
		ai.type(KeyEvent.VK_F);
		ai.leftClick(FACTORY);
		ai.type(KeyEvent.VK_V);
		ai.type(KeyEvent.VK_F);
		ai.leftClick(FACTORY2);
		long factoryStart = System.currentTimeMillis();
		
		//Build Supply 2
		ai.waitFor(Resource.mineral(100));
		ai.selectArea(MINERS);
		ai.type(KeyEvent.VK_B);
		ai.type(KeyEvent.VK_S);
		ai.leftClick(SUPPLY2);
		ai.sleep(5);
		ai.leftClick(SUPPLY2);
		ai.type(KeyEvent.VK_Q);
		queueToMinerals();
		
		//Send Factory workers to minerals
		ai.leftClick(FACTORY);
		ai.type(KeyEvent.VK_Q);
		queueToMinerals();
		ai.leftClick(FACTORY2);
		ai.type(KeyEvent.VK_Q);
		queueToMinerals();
		
		//Tech labs
		ai.sleep(0.5);
		ai.leftClick(FACTORY);
		ai.shiftLeftClick(FACTORY2);
		ai.waitFor(Resource.mineral(100));
		ai.waitFor(Resource.vespene(50));
		while(System.currentTimeMillis() - factoryStart < realToGameTime(65000)){
			ai.sleep(0.1);
		}
		ai.type(KeyEvent.VK_X);
		ai.type(KeyEvent.VK_X);
		long techStart = System.currentTimeMillis();
		
		//Tanks
		while(System.currentTimeMillis() - techStart < realToGameTime(25500)){
			ai.sleep(0.1);
		}
		
		ai.waitFor(Resource.mineral(300));
		ai.waitFor(Resource.vespene(250));
		ai.type(KeyEvent.VK_S);
		ai.type(KeyEvent.VK_S);
		
		//Supply Depot
		ai.selectArea(MINERS);
		ai.waitFor(Resource.mineral(100));
		ai.type(KeyEvent.VK_B);
		ai.type(KeyEvent.VK_S);
		ai.leftClick(SUPPLY3);
		ai.sleep(10);
		ai.leftClick(SUPPLY3);
		ai.type(KeyEvent.VK_Q);
		queueToMinerals();
		
		//More tanks
		ai.leftClick(FACTORY);
		ai.shiftLeftClick(FACTORY2);
		ai.waitFor(Resource.mineral(300));
		ai.waitFor(Resource.vespene(250));
		ai.waitFor(Resource.maxSupply(35));
		ai.type(KeyEvent.VK_S);
		ai.type(KeyEvent.VK_S);
		
		long tankStart = System.currentTimeMillis();
		
		//Find enemy
		Point enemy = getScoutingResults();
		
		//Attack
		while(System.currentTimeMillis() - tankStart < realToGameTime(45500)){
			ai.sleep(0.1);
		}
		ai.type(KeyEvent.VK_F2);
		ai.type(KeyEvent.VK_A);
		ai.leftClick(enemy);
		long startedMoving = System.currentTimeMillis();
		
		
		ai.addUnitRequest("buildings.Barracks");
		ai.addUnitRequest("buildings.CommandCenter");
		ai.addUnitRequest("buildings.Factory");
		ai.addUnitRequest("units.Marine");
		ai.addUnitRequest("buildings.SupplyDepot");
		
		while(System.currentTimeMillis() - startedMoving < 15000){
			ai.type(KeyEvent.VK_F2);
			ai.sleep(0.1);
			ai.type(KeyEvent.VK_F2);
			ai.sleep(0.3);
		}
		ai.debug("Waking up");
		ai.type(KeyEvent.VK_F2);
		ai.type(KeyEvent.VK_F2);
		ai.type(KeyEvent.VK_A);
		ai.leftClick(enemy);
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		boolean isInSiege = false;
		while(Util.MINIMAP.contains(mouse)){
			if(!isInSiege){
				ai.type(KeyEvent.VK_F2);
				ai.type(KeyEvent.VK_F2);
				ai.type(KeyEvent.VK_A);
				ai.leftClick(enemy);
			}
			boolean potentialEnemy = false;
			GameObject[] screen = ai.requestGameObjects();
			ArrayList<GameObject> threats = new ArrayList<GameObject>();
			for(int i = 0; i < screen.length; i++){
				String name = screen[i].getClass().getName();
				String simple = screen[i].getClass().getSimpleName();
				if(notInCenter(screen[i]) && (name.contains("buildings") || name.contains("units"))){
					if(!isInSiege){
						ai.debug("Found " + simple + ".");
					}
					threats.add(screen[i]);
					potentialEnemy = true;
				}
			}

			if(potentialEnemy && !isInSiege){
				isInSiege = true;
				ai.debug("Entering Siege Mode");
				ai.type(KeyEvent.VK_E);
				ai.sleep(4.5);
			} else if(!potentialEnemy && isInSiege){
				isInSiege = false;
				ai.debug("Entering Tank Mode");
				ai.type(KeyEvent.VK_D);
				ai.sleep(4.5);
				ai.type(KeyEvent.VK_A);
				ai.leftClick(enemy);
			}
			
			mouse = MouseInfo.getPointerInfo().getLocation();
			ai.sleep(0.1);
		}
		
		System.out.println("DONE");
		System.out.println(Util.MINIMAP);
		System.out.println(mouse);
		System.out.println(enemy);
		System.exit(0);
	}

	private static void queueToMinerals(){
		boolean sendMore = false;
		if(ai.haveIdleWorkers()){
			mineralIndex = (mineralIndex + 1) % minerals.length;
			sendMore = true;
		}
		
		ai.shiftRightClick(minerals[mineralIndex].getCenter());
		
		if(sendMore){
			ai.selectAllIdle();
			ai.rightClick(minerals[mineralIndex].getCenter());
		}
		
	}
	
	private static Point getScoutingResults() {
		enemyBaseIndex = Util.findEnemyBase(bases, ai.screenShot());
		while(enemyBaseIndex < 0){
			bases = ai.findAllBases();
			enemyBaseIndex = Util.findEnemyBase(bases, ai.screenShot());
		}
		return getCenter(bases[enemyBaseIndex]);
	}

	private static void scoutForEnemy() {

		bases = ai.findAllBases();
		homeBaseIndex = Util.findOwnBase(bases, ai.screenShot());
		while(homeBaseIndex < 0){
			bases = ai.findAllBases();
			homeBaseIndex = Util.findOwnBase(bases, ai.screenShot());
		}
		ai.selectAll();
		ai.leftClick(getArmyUnit(1));
		ai.assignToGroup(2);

		boolean reverse = homeBaseIndex < bases.length/2;

		ai.rightClick(width/2, height/2);
		for(int baseIndex = 0; baseIndex < bases.length; baseIndex++){
			if(reverse){
				if(bases.length - baseIndex - 1 != homeBaseIndex){
					ai.shiftRightClick(getCenter(bases[bases.length - baseIndex - 1]));
				}

			} else {
				if(baseIndex != homeBaseIndex){

					ai.shiftRightClick(getCenter(bases[baseIndex]));
				}
			}
		}
		
		ai.shiftRightClick(getCenter(bases[homeBaseIndex]));
	}

	private static Point getCenter(Rectangle r){
		return new Point(r.x + r.width/2, r.y + r.height/2);
	}
	
	private static double realToGameTime(double time) {
		return time * GAME_SPEED;
	}

	private static Point getArmyUnit(int num) {
		num--;
		return new Point(490 + 40 * (num % 8), 650 + 40 * (num / 8));
	}

	private static boolean notInCenter(GameObject go){
		return go.getX() < width/2 - 150 ||
				go.getX() + go.getWidth() > width/2 + 150 ||
				go.getY() < height/2 - 100 ||
				go.getY() + go.getHeight() > height/2 + 100;
	}
	
}
