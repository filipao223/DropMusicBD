package server;

import request.Request;

import java.sql.SQLException;
import java.sql.Statement;

public class Playlist {
    public static boolean createPlaylist(String username, String name, String clientData){
        String optional = null;
        //Open a database connection
        if (Connect.connect(clientData)){
            //Check if user exists
            if (!CheckExistence.userExists(username)){
                System.out.println(clientData + " | " + username
                        + " | User not found.");
                return false;
            }
            //Check if playlist already exists
            if (CheckExistence.playlistExists(name)){
                System.out.println(clientData + " | " + username
                        + " | Playlist already exists.");
                return false;
            }

            //Add playlist
            try{
                Statement statement = Connect.connection.createStatement();
                statement.executeUpdate("INSERT INTO playlist (p_name, private, users_user_id) " +
                        "VALUES (\"" + name + "\",1," +
                        "(SELECT user_id FROM users WHERE username=\"" + username + "\"));");
                return true;
            } catch (SQLException e) {
                if (e.getMessage() != null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData + " | " + username
                        + " | Failed to create playlist " + (optional==null?".":" | " + optional));
                return false;
            }
        }
        return false;
    }

    public static boolean removePlaylist(){
        return false;
    }

    public static boolean addMusicToPlaylist(){
        return false;
    }
}
