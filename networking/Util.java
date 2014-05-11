package networking;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Util {
    
    private static final String SOUND_DIR = "audio/";
    private static final String IMG_DIR = "images/";
    
    public static final String QUERY_IMG_DEST_PATH = "../query-inbox/";
    
    public static void writeImage(BufferedImage img){
        try {
            File out = new File(IMG_DIR + getDateString() + ".png");
            ImageIO.write(img, "png", out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }  

    
    public static void closeSocket(Socket socket){
        try{
            if(socket != null){
                socket.close();
            }
        }
        catch( IOException e ){
            e.printStackTrace();
        }
    }
    
    public static void closeServerSocket(ServerSocket socket){
        try{
            if(socket != null){
                socket.close();
            }
        }
        catch( IOException e ){
            e.printStackTrace();
        }
    }

    
    public static String getDateString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
