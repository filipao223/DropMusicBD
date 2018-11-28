package server;

import request.Request;
import serializer.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Statement;

public class Upload {
    private static Serializer serializer = new Serializer();

    public static boolean upload(Socket client, String username, String music, String clientData){
        //Remove file extension from music name
        String[] tokens = music.split("\\.");

        //Open a database connection
        if (Connect.connect(clientData)){
            if (CheckExistence.musicExists(tokens[0])){
                //Receive music file
                String callback = "Upload_ok";
                try{
                    DataOutputStream out = new DataOutputStream(client.getOutputStream());
                    serializer.writeMessage(out, callback.getBytes(), callback.length());
                } catch (IOException e) {
                    if (Request.DEV_MODE) e.printStackTrace();
                    return false;
                }
                try{
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    byte[] buffer;
                    buffer = serializer.readMessage(in);
                    if (buffer == null){
                        System.out.println(clientData + " | Error reading music file.");
                    }
                    else{
                        //Convert back to file
                        try (FileOutputStream fos = new FileOutputStream("serverStorage/" + music)) {
                            fos.write(buffer);
                            //Write url in the database
                            if (!updateURL(username, tokens[0], "serverStorage/" + music, clientData)){
                                return false;
                            }
                            Connect.disconnect(clientData);
                            return true;
                        } catch (Exception e){
                            if (Request.DEV_MODE) e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    if (Request.DEV_MODE) e.printStackTrace();
                }
            }
        }
        return false;
    }

    private static boolean updateURL(String username, String music, String url, String clientData){
        String optional = null;
        //Database connection will exist
        //User and music are guaranteed to exist
        try{
            Statement statement = Connect.connection.createStatement();
            statement.executeUpdate("INSERT INTO url (url, music_nmusic) VALUES (\"" + url + "\", " +
                    "(SELECT nmusic FROM music WHERE m_name=\"" + music + "\"));");
            Connect.disconnect(clientData);
            return true;
        } catch (SQLException e) {
            if (e.getMessage() != null) optional = e.getMessage();
            if (Request.DEV_MODE) e.printStackTrace();
            System.out.println(clientData + " | " + username
                    + " | Failed to update music URL " + (optional==null?".":" | " + optional));
        }
        return false;
    }
}
