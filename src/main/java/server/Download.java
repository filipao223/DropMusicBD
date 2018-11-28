package server;

import request.Request;
import serializer.Serializer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Download {
    private static Serializer serializer = new Serializer();
    public static boolean download(Socket client, String username, String music, String clientData){
        String optional = null;
        //Open a database connection
        if (Connect.connect(clientData)){
            //Check if the music exists
            if (!CheckExistence.musicExists(music)){
                System.out.println(clientData + " | " + username
                        + " | Music not found.");
                return false;
            }

            //Check if user is music uploader
            if (!Permission.ownsMusic(username, music)){
                //Check if this user has access to this music
                if (!Permission.musicShared(username, music)){
                    System.out.println(clientData + " | " + username
                            + " | You don't have access to this music.");
                    return false;
                }
            }

            //Check if there is a url for the music
            if (!CheckExistence.urlExists(music)){
                System.out.println(clientData + " | " + username
                        + " | Music not found in disk.");
                return false;
            }

            //Send music file to client
            try{
                Statement statement = Connect.connection.createStatement();
                //Get music url
                ResultSet rs = statement.executeQuery("SELECT url FROM url WHERE music_nmusic=" +
                        "(SELECT nmusic FROM music WHERE m_name=\"" + music + "\");");
                if (rs.next()){
                    //Send callback to client to inform download is ready
                    String callbackMessage = "Download_ok";
                    if (!Callback.callback(client, callbackMessage)){
                        System.out.println(clientData + " | Error informing client of download");
                        return false;
                    }
                    String url = rs.getString("url");
                    File file = new File(url);
                    if (file != null){
                        //Convert music file to byte array
                        try{
                            DataOutputStream out = new DataOutputStream(client.getOutputStream());
                            byte[] buffer = Files.readAllBytes(file.toPath());
                            if (!serializer.writeMessage(out, buffer, buffer.length)){
                                System.out.println("Error writing music file to server.");
                            }
                            return true;
                        } catch (IOException e) {
                            if (Request.DEV_MODE) e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
