package networking;
import java.util.LinkedHashSet;
import java.util.Set;


public abstract class StarCraftDetector extends Thread {
    private volatile Set<String> unitsOfInterest;
    
    public StarCraftDetector(){
        unitsOfInterest = new LinkedHashSet<String>();
    }
    
    public synchronized Set<String> getUnitsOfInterest(){
        return new LinkedHashSet<String>(unitsOfInterest);
    }
    
    public synchronized void addUnitOfInterest(String unit){
        unitsOfInterest.add( unit );
    }
    
    public synchronized void removeUnitOfInterest(String unit){
        if ( unitsOfInterest.contains(unit) ){
            unitsOfInterest.remove(unit);
        }
    }
    
    public synchronized void clearUnitsOfInterest(){
    	unitsOfInterest.clear();
    }
    
    public abstract String getMostRecentInformation();
    public abstract String getCurrentInformation();
    public abstract void registerScreenJump();
    public abstract void registerScreenTranslation();
}
