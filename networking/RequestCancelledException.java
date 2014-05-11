package networking;
import java.io.IOException;

public class RequestCancelledException extends IOException{
    public RequestCancelledException(String str){
        super(str);
    }
}
