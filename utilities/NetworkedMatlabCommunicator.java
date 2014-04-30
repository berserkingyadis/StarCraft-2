package utilities;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;


public class NetworkedMatlabCommunicator {
    
    private Socket socket = null;
    //private Socket recvSocket;
    private final String HOST = "cedar.cs.wisc.edu";
    private final int PORT = 8002;
    private BufferedReader br = null;
    
    
    /**
     * @param host Name of host you wish to connect to
     * @param remotePort Port that the matlab host is listening on
     * @throws UnknownHostException
     * @throws IOException
     */
    public NetworkedMatlabCommunicator() throws UnknownHostException, IOException{
    	//ServerSocket serverSocket = new ServerSocket(PORT + 5);
        //recvSocket = serverSocket.accept();
    }

    
    /**
     * Opens up a socket to the remote matlab host, sends a screenshot, and 
     * then blocks, waiting for the matlab host to respond with a list of 
     * bounding boxes
     * @param img the game screenshot to send over the network
     * @return an array of strings of the form <UNIT_TYPE: columnCoord rowCoord width height>
     * @throws IOException
     */
    public String[] getBoundingBoxesForScreenshot(BufferedImage img) throws IOException{
        socket = new Socket(HOST, PORT);
        br = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        
        ImageIO.write(img, "PNG", socket.getOutputStream());
        
        System.out.println("Waiting for matlab response");
        String matlabData = receiveMatlabResponse();
        String[] boundingBoxes = matlabData.split(",");
        
        System.out.println("Got response!");
        System.out.println(matlabData);
        
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("ACK\n");
        writer.flush();
        
        socket.close();
        System.out.println(Arrays.toString(boundingBoxes));
        return boundingBoxes;
    }
    
    
    private String receiveMatlabResponse() throws IOException{
        try{
            int numDetections = Integer.parseInt(br.readLine());
            if(numDetections == 0){ return ""; }
        }
        catch(NumberFormatException e){
            System.out.println("Received garbage numDetections.");
        }
        return br.readLine();
    }
    
    
    /**
     * A test main() method - not for production use
     * @param args unused
     */
    public static void main(String[] args){
        try {
            NetworkedMatlabCommunicator client = new NetworkedMatlabCommunicator();
            Robot r = new Robot();
            Point p = MouseInfo.getPointerInfo().getLocation();
            System.out.println(p);
            Rectangle rect = new Rectangle(100, 100, 100, 100);
            BufferedImage img = r.createScreenCapture(rect);
            
            for(int i = 0; i < 3; i++){
                client.getBoundingBoxesForScreenshot(img);
            }
        } 
        catch (UnknownHostException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        } 
        catch (AWTException e) {
            e.printStackTrace();
        }
        
    }
}
