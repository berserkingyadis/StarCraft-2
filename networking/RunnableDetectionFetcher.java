package networking;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;


public class RunnableDetectionFetcher implements Runnable{

    private final String HOST;
    private final int PORT;
    private final BufferedImage img;
    private final String screenShotKey;
    private NetworkedStarCraftDetector parentDetector;

    private static long numFetchersCreated = 0;
    private long id;


    /**
     * @param host Name of host you wish to connect to
     * @param remotePort Port that the matlab host is listening on
     */
    public RunnableDetectionFetcher(NetworkedStarCraftDetector parentDetector, String host, 
            int remotePort, BufferedImage img, String screenShotKey){
        this.HOST= host;
        this.PORT = remotePort;
        this.img = img;
        this.parentDetector = parentDetector;
        this.screenShotKey = screenShotKey;
        this.id = numFetchersCreated++;
    }


    /**
     * Opens up a socket to the remote matlab host, sends a screenshot, and 
     * then blocks, waiting for the matlab host to respond with a list of 
     * bounding boxes
     * @param img the game screenshot to send over the network
     * @return an array of strings of the form <UNIT_TYPE: columnCoord rowCoord width height>
     * @throws IOException
     */
    public void run(){
        Socket socket = null;
        try{
            System.out.println(screenShotKey + ": " + "launching...");
            long startTime = System.nanoTime();

            socket = new Socket();
            socket.connect(new InetSocketAddress(HOST, PORT), 10000);
            socket.setSoTimeout(5000);  
            BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream()));

            os.write((screenShotKey + "\n").getBytes());
            os.flush();

            Set<String> unitsOfInterest = parentDetector.getUnitsOfInterest();
            for( String unitName : unitsOfInterest ){
                os.write((unitName + ",").getBytes());
            }
            os.write("\n".getBytes());
            os.flush();

            /*
            Iterator writers = ImageIO.getImageWritersByFormatName("png");
            ImageWriter writer = (ImageWriter) writers.next();
            writer.setOutput(ImageIO.createImageOutputStream((os)));
            writer.write(img);
             */

            ImageIO.write(img, "PNG", os);

            //System.out.println("Waiting for matlab response");
            String matlabData = receiveMatlabResponse(br);
            String[] boundingBoxes = matlabData.split(",");

            //System.out.println("Got response!");
            System.out.println(matlabData);

            parentDetector.registerDetectionData(matlabData, screenShotKey);

            System.out.println(Arrays.toString(boundingBoxes));

            double elapsedTime = (System.nanoTime() - startTime) / Math.pow(10, 9);
            System.out.println(screenShotKey + ": elapsed time: " + elapsedTime );
        }
        catch(SocketTimeoutException e){
            System.out.println(screenShotKey + ": XXXXXXXX" + " timed out.");
        }
        catch(RequestCancelledException e){
            System.out.println(screenShotKey + ": " + " cancelled.");
        }
        catch(IOException e){
            System.out.println(screenShotKey + ": XXXXXXXX" + e.getMessage());
        }
        finally{
            System.out.println(screenShotKey + ": " +  " terminated");
            parentDetector.registerRequestTermination();
            Util.closeSocket(socket);
        }
    }


    private String receiveMatlabResponse(BufferedReader br) throws IOException{
        boolean numDetectionsReceived = false;
        while( ! numDetectionsReceived){
            String line = br.readLine();
            Scanner scnr = new Scanner(line);
            if (scnr.hasNextInt()){
                int numDetections = scnr.nextInt();
                numDetectionsReceived = true;
                if( numDetections == 0){
                    return "";
                }
            }
            else if (line.equals("Cancel")){
                throw new RequestCancelledException("Request was cancelled by the server.");
            }
        }

        return br.readLine();
    }
}
