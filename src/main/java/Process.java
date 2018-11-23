import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Process implements Runnable {
    private static Connection connection;
    private Statement statement;
    private Socket client;
    private Serializer serializer = new Serializer();

    Process(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        //Log some info
        System.out.println("New client being processed, IP: " + client.getInetAddress().getHostAddress()
                + " | Port: " + client.getLocalPort());

        String clientData = "IP: " + client.getInetAddress().getHostAddress()
                + " | Port: " + client.getLocalPort();

        //Read the data sent
        byte[] buffer;
        try{
            DataInputStream in = new DataInputStream(client.getInputStream());
            buffer = serializer.readMessage(in);
            if (buffer == null){
                System.out.println(clientData + " | Error reading message.");
                return;
            }

            System.out.println(clientData + " | Received: " + new String(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean connect(){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost/dropmusic?" +
                    "user=root&password=ProjetoBD2018");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
