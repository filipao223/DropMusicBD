package server;

import request.Request;

import java.sql.ResultSet;
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
                Connect.disconnect(clientData);
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

    public static boolean removePlaylist(String username, String playlist, String clientData){
        String optional = null;
        //Open database connection
        if (Connect.connect(clientData)){
            //Check if user exists
            if (!CheckExistence.userExists(username)){
                System.out.println(clientData + " | " + username
                        + " | User not found.");
                return false;
            }

            //Check if playlist exists
            if (!CheckExistence.playlistExists(playlist)){
                System.out.println(clientData + " | " + username
                        + " | Playlist not found.");
                return false;
            }

            //Check if user owns the playlist
            if (!Permission.ownsPlaylist(username, playlist)){
                System.out.println(clientData + " | " + username
                        + " | You don't own this playlist.");
                return false;
            }

            //Delete the playlist (cascade delete will be on)
            try{
                Statement statement = Connect.connection.createStatement();
                //Get playlist id from name
                ResultSet rs = statement.executeQuery("SELECT nplaylist FROM playlist WHERE p_name=\"" + playlist + "\";");
                if (!rs.next()){
                    System.out.println(clientData + " | " + username
                            + " | Failed to delete playlist.");
                    return false;
                }
                int nplaylist = rs.getInt("nplaylist");
                statement.executeUpdate("DELETE FROM playlist WHERE nplaylist=" + nplaylist + ";");
                Connect.disconnect(clientData);
                return true;
            } catch (SQLException e) {
                if (e.getMessage() != null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData + " | " + username
                        + " | Failed to delete playlist " + (optional==null?".":" | " + optional));
                return false;
            }
        }
        return false;
    }

    public static boolean addMusicToPlaylist(String username, String playlist, String music, String clientData){
        String optional = null;
        //Open a database connection
        if (Connect.connect(clientData)){
            //Check if user exists
            if (!CheckExistence.userExists(username)){
                System.out.println(clientData + " | " + username
                        + " | User not found.");
                return false;
            }

            //Check if playlist exists
            if (!CheckExistence.playlistExists(playlist)){
                System.out.println(clientData + " | " + username
                        + " | Playlist not found.");
                return false;
            }

            //Check if user owns the playlist
            if (!Permission.ownsPlaylist(username, playlist)){
                System.out.println(clientData + " | " + username
                        + " | You don't own this playlist.");
                return false;
            }

            //Check if music exists
            if (!CheckExistence.musicExists(music)){
                System.out.println(clientData + " | " + username
                        + " | Music not found.");
                return false;
            }

            try{
                //Add playlist_music table
                Statement statement = Connect.connection.createStatement();
                statement.executeUpdate("INSERT INTO playlist_music (playlist_nplaylist, music_nmusic) " +
                        "VALUES ((SELECT nplaylist FROM playlist WHERE p_name=\"" + playlist + "\")," +
                        "(SELECT nmusic FROM music WHERE m_name=\"" + music + "\"));");
                Connect.disconnect(clientData);
                return true;
            } catch (SQLException e) {
                if (e.getMessage() != null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData + " | " + username
                        + " | Failed to add music to playlist " + (optional==null?".":" | " + optional));
                return false;
            }
        }
        return false;
    }
}
