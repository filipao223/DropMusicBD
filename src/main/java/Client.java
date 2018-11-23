import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private int PORT = 16667;
    private String address;
    private Serializer serializer = new Serializer();

    Client(){
        startClient();
    }

    public static void main(String[] args){
        new Client();
    }

    private void startClient(){
        //Request connection to database server
        try{
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Failed getting host address");
            return;
        }

        try{
            Socket server = new Socket("localhost", PORT);
            String message = "2_user1";
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            if (!serializer.writeMessage(out, message.getBytes(), message.length())){
                System.out.println("Error writing message to server");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
