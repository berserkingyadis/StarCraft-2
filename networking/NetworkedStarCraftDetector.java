package networking;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class NetworkedStarCraftDetector extends StarCraftDetector{
    private final String HOST;
    private final int PORT;
    private Robot robot;
    private ThreadPoolExecutor tpe;
    private volatile int numOutstandingRequests;
    private Rectangle screenRect;

    private volatile boolean signalToStopTriggered;
    
    private volatile String mostRecentDetectionTime;
    private volatile String mostRecentDetectionData;
    private volatile String lastScreenJumpTime;

    public static final String DATE_KEY_FORMAT = "yyyy-MM-dd-HH:mm:ss";
    private static final int TIME_TO_SLEEP_FOR_SYNCRHONOUS_INFO_REQUEST = 100; // in ms
    private static final int TIME_TO_SLEEP_FOR_REQUEST_LAUNCH_POLL = 200; // in ms
    private static final int MAX_OUTSTANDING_REQUESTS = 10;

    // Time (in ms) to wait in between the sending of requests
    // That way, we don't blast off all our requests at once
    // We'll start this hard-coded, but move to estimating it
    private int minimumSpacingBetweenRequests;


    public NetworkedStarCraftDetector() throws AWTException{
        this("cedar.cs.wisc.edu", 8002);
    }

    /**
     * @param host Name of host you wish to connect to
     * @param remotePort Port that the matlab host is listening on
     */
    public NetworkedStarCraftDetector(String host, int remotePort) throws AWTException{
        HOST= host;
        PORT = remotePort;
        robot = new Robot();
        tpe = new ThreadPoolExecutor(MAX_OUTSTANDING_REQUESTS, MAX_OUTSTANDING_REQUESTS, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5));  
        minimumSpacingBetweenRequests = 1000;
        mostRecentDetectionTime = Util.getDateString();
        mostRecentDetectionData = "";
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        screenRect = new Rectangle(0, 0, dim.width, dim.height);
    }

    @Override
    public String getMostRecentInformation(){
        return mostRecentDetectionData;
    }


    @Override
    public String getCurrentInformation(){
        String currentTime = Util.getDateString();
        while( currentTime.compareTo( mostRecentDetectionTime ) > 0 ){
            try{
                Thread.sleep(TIME_TO_SLEEP_FOR_SYNCRHONOUS_INFO_REQUEST);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        return mostRecentDetectionData;
    }


    @Override
    public void registerScreenJump(){
        lastScreenJumpTime = Util.getDateString();
    }

    @Override
    public void registerScreenTranslation(){
        // TODO
    }


    public void signalToStop(){
        signalToStopTriggered = true;
    }
    
    public boolean signalToStopHasBeenGiven(){
        return signalToStopTriggered;
    }
    

    // ****** METHODS USED BY THE HELPER RunnableDetectionFetcher CLASS ********
    public synchronized void registerDetectionData(String detectionData, String key){
        if ( mostRecentDetectionTime.compareTo(key) < 0){
            System.out.println("Got new detection data");
            mostRecentDetectionTime = key;
            mostRecentDetectionData = detectionData;
        }
    }


    public void registerRequestTermination(){
        numOutstandingRequests--;
        System.out.println("Num outstanding requests: " + numOutstandingRequests);
    }


    @Override
    public void run(){
        try{
            runFetchLoop();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void runFetchLoop() throws InterruptedException{
        long timeOfLastRequestLaunch = System.nanoTime();
        while(true){
            if (signalToStopTriggered){
                break;
            }

            while(getUnitsOfInterest().isEmpty()){
                System.out.println("No units of interest indicated. Sleeping.");
                Thread.sleep(500);
            }

            BufferedImage screenShot = takeScreenShot();

            // Register the screenshot with some sort of key
            String screenShotKey = Util.getDateString();

            // Start thread to contact matlab and get a response
            tpe.execute(new RunnableDetectionFetcher(this, HOST, PORT, screenShot, screenShotKey));
            numOutstandingRequests++;
            System.out.println("Num outstanding requests: " + numOutstandingRequests);
            timeOfLastRequestLaunch = System.nanoTime();

            // Wait if we haven't waited enough a long enough time
            // Take screenshot
            long currentTime = System.nanoTime();
            long timeElapsed = currentTime - timeOfLastRequestLaunch;
            while( numOutstandingRequests >= MAX_OUTSTANDING_REQUESTS || 
                    timeElapsed / Math.pow(10, 6) < minimumSpacingBetweenRequests){
                currentTime = System.nanoTime();
                timeElapsed = currentTime - timeOfLastRequestLaunch;
                Thread.sleep(TIME_TO_SLEEP_FOR_REQUEST_LAUNCH_POLL);

                if (signalToStopTriggered){
                    break;
                }
            }
            System.out.println();
        }
        
        System.out.println("Telling thread pool to shut down");
        tpe.shutdownNow();
    }


    public BufferedImage takeScreenShot(){
        BufferedImage img = robot.createScreenCapture(screenRect);
        return img;
    }




    /**
     * A test main() method - not for production use
     * @param args unused
     */
    public static void main(String[] args){
        try {
            NetworkedStarCraftDetector detectorThread = new NetworkedStarCraftDetector("cedar.cs.wisc.edu", 8002);
            detectorThread.addUnitOfInterest("All");
            detectorThread.start();

            /*
            for(int i = 0; i < 3; i++){
                System.out.println("Querying for info");
                String info = detectorThread.getMostRecentInformation();
                System.out.println(info);
                Thread.sleep(1000);
            }
             */
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            System.out.println("PARTY TIME FREEZE");
//            detectorThread.signalToStop();
        } 
        //        catch(InterruptedException e){
        //            e.printStackTrace();
        //        }
        catch (AWTException e) {
            e.printStackTrace();
        }

    }
}
