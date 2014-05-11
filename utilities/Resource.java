package utilities;

public class Resource {

	public static final int MINERALS = 0, VESPENE = 1, CURSUPPLY = 2, MAXSUPPLY = 3, IDLEWORKERS = 4;
	private int type, amount;
	
	public Resource(int type, int amount){
		this.type = type;
		this.amount = amount;
	}
	
	public int getType(){
		return type;
	}
	
	public int getAmount(){
		return amount;
	}
	
	public static Resource mineral(int amount){
		return new Resource(MINERALS, amount);
	}
	
	public static Resource vespene(int amount){
		return new Resource(VESPENE, amount);
	}
	
	public static Resource curSupply(int amount){
		return new Resource(CURSUPPLY, amount);
	}
	
	public static Resource maxSupply(int amount){
		return new Resource(MAXSUPPLY, amount);
	}
	
	public static Resource idleWorkers(){
		return new Resource(IDLEWORKERS, 1);
	}
}
