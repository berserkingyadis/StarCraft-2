package utilities;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;


public class GameState {

	ArrayList<GameObject> objects;
	private int minerals, vespene, curSupply, maxSupply;
	
	private GameState(){
		objects = new ArrayList<GameObject>();
		this.minerals = 50;
		this.vespene = 0;
		this.curSupply = 5;
		this.maxSupply = 10;
	}
	
	public void updateValues(GameState oldState){
		this.minerals = oldState.minerals;
		this.vespene = oldState.vespene;
		this.curSupply = oldState.curSupply;
		this.maxSupply = oldState.maxSupply;
	}
	
	
	
	public static GameState loadState(File gameData) {
		GameState game = new GameState();
		try {
			Scanner scan = new Scanner(gameData);
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				String[] tokens = line.split(" ");
				
				//Setup constructor parameters
				Object[] parameters = new Object[4];
				for(int i = 0; i < parameters.length; i++){
					parameters[i] = Integer.parseInt(tokens[i+1]);
				}
								
				//Find the right class
				Class<?> c = Class.forName(tokens[0]);
				
				//Find its constructor
				Constructor<?>[] cons = c.getConstructors();

				//Assume it's the only one
				game.objects.add((GameObject)cons[0].newInstance(parameters));
				
			}
			scan.close();
		} catch (FileNotFoundException e) {
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
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return game;
	}

}
