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
}
