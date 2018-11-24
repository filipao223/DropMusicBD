import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.Date;

public class Process implements Runnable {
    private static Connection connection;
    private Statement statement;
    private Socket client;
    private Serializer serializer = new Serializer();
    private String clientData;

    Process(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        //Log some info
        System.out.println("New client being processed, IP: " + client.getInetAddress().getHostAddress()
                + " | Port: " + client.getLocalPort());

        clientData = "IP: " + client.getInetAddress().getHostAddress()
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
            //Decode feature requested
            String[] tokens = tokenizer(new String(buffer));

            handleRequest(tokens);

        } catch (IOException e) {
            if (Request.DEV_MODE) e.printStackTrace();
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private boolean handleRequest(String[] tokens){
        //Format: feature_username_sql
        //if login: feature_username_password

        try{
            switch(Integer.parseInt(tokens[0])){
                case Request.LOGIN:
                    if (LoginLogout.login(tokens[1], tokens[2], clientData))
                        System.out.println(clientData + " | " + tokens[1] + " | Logged in.");
                    break;
                case Request.LOGOUT:
                    if (LoginLogout.logout(tokens[1], clientData))
                        System.out.println(clientData + " | " + tokens[1] + " | Logged out.");
            }
        } catch (Exception e){
            if (Request.DEV_MODE) e.printStackTrace();
            System.out.println(clientData + " | Failed to handle request.");
        }

        return false;
    }

    private String[] tokenizer(String main){
        return main.split("_");
    }
}
