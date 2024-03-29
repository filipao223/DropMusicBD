package server;

import request.Request;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class that handles critiques
 */
public class Critique {

    /**
     * Make a critique to given album from given user.
     * @param username name of the user that wants to write a critique
     * @param text body of the critique
     * @param rating integer rating, from 1 to 5
     * @param albumName name of the album
     * @param clientData basic client data, such as IP and port.
     * @return true/false
     */
    public static boolean critique(String username, String text, int rating, String albumName, String clientData){
        String optional = null;
        //server.Connect to database
        if (Connect.connect(clientData)){
            //Check if user exists
            if (!CheckExistence.userExists(username)){
                System.out.println(clientData + " | " + username
                        + " | User not found.");
                return false;
            }

            //Check if album exists
            if (!CheckExistence.albumExists(albumName)){
                System.out.println(clientData + " | " + username
                        + " | Album not found.");
                return false;
            }

            //Check if user already made critique to this album
            if (CheckExistence.critiqueExists(username, albumName)){
                System.out.println(clientData + " | " + username
                        + " | " + albumName + " | User already made critique.");
                return false;
            }

            try{
                Statement statement = Connect.connection.createStatement();
                statement.executeUpdate("INSERT INTO critique (c_text, rating, album_nalbum, users_user_id) " +
                        "VALUES (\"" + text + "\",rating=" + rating +
                        ",(SELECT nalbum FROM album WHERE album_name=\"" + albumName + "\")," +
                        "(SELECT user_id FROM users WHERE username=\"" + username + "\"));");
                return true;
            } catch (SQLException e) {
                if (e.getMessage() != null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData + " | " + username
                        + " | Failed to upload critique " + (optional==null?".":" | " + optional));
            }
        }
        return false;
    }
}
