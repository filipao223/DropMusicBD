package server;

import request.Request;
import serializer.Serializer;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Process implements Runnable {
    private Socket client;
    private Serializer serializer = new Serializer();
    private String clientData;

    Process(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        //Log some info
        clientData = "IP: " + client.getInetAddress().getHostAddress()
                + " | Port: " + client.getLocalPort();
        System.out.println("New client being processed, " + clientData);

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
        // TODO validate user data

        try{
            switch(Integer.parseInt(tokens[0])){
                case Request.LOGIN:
                    if (LoginLogout.login(tokens[1], tokens[2], clientData))
                        System.out.println(clientData + " | " + tokens[1] + " | Logged in.");
                    break;
                case Request.LOGOUT:
                    if (LoginLogout.logout(tokens[1], clientData))
                        System.out.println(clientData + " | " + tokens[1] + " | Logged out.");
                    break;
                case Request.REGISTER:
                    if (Register.register(tokens[1], tokens[2], tokens[3], tokens[4], clientData))
                        System.out.println(clientData + " | " + tokens[1] + " | Registered.");
                    break;
                case Request.MAKE_EDITOR:
                    if (Manage.makeEditor(tokens[1], tokens[2], clientData))
                        System.out.println(clientData + " | " + tokens[1] + " | Made \"" + tokens[2] + "\" an editor.");
                    break;
                case Request.SEARCH:
                    if (Search.search(tokens[2], clientData))
                        System.out.println(clientData + " | " + tokens[1] + " | Successful query.");
                    break;
                case Request.CRITIQUE:
                    if (Critique.critique(tokens[1], tokens[2], Integer.parseInt(tokens[3]), tokens[4], clientData))
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[3] + " | Uploaded critique.");
                    break;
                case Request.ADD_PLAYLIST:
                    if (Playlist.createPlaylist(tokens[1], tokens[2], clientData))
                        System.out.println(clientData + " | " + tokens[1] + " | Created playlist.");
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
