package server;

import request.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckExistence {
    public static boolean userExists(String username){
        //Connection will exist
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username=\"" + username + "\";");
            if (!rs.next()){
                return false; //User doesn't exist
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return true;
    }

    public static boolean userIsEditor(String username){
        //Connection will exist
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT editor FROM users WHERE username=\"" + username + "\";");
            if (rs.next()){
                if (rs.getInt("editor")==1) return true;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return false;
    }

    public static boolean albumExists(String albumName){
        //Connection will exist
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM album WHERE album_name=\"" + albumName + "\";");
            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return false;
    }

    public static boolean critiqueExists(String username, String albumName){
        //Connection will exist
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM critique " +
                    "WHERE users_user_id=(" +
                    "SELECT user_id FROM users WHERE username=\"" + username + "\") AND album_nalbum=(" +
                    "SELECT nalbum FROM album WHERE album_name=\"" + albumName + "\");");
            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return false;
    }

    public static boolean playlistExists(String name){
        //Connection will exist
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM playlist WHERE p_name=\"" + name + "\";");
            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return false;
    }

    public static boolean musicExists(String music){
        //Connection will exist
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM music WHERE m_name=\"" + music + "\";");
            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return false;
    }

    public static int[] playlistHasMusic(String playlist, String music){
        //Connection will exist
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM playlist_music WHERE playlist_nplaylist=" +
                    "(SELECT nplaylist FROM playlist WHERE p_name=\"" + playlist + "\") AND music_nmusic=(SELECT nmusic FROM music WHERE m_name=\"" + music + "\");");
            if (rs.next()){
                //Exists
                int[] keys = new int[2];
                keys[0] = rs.getInt("playlist_nplaylist");
                keys[1] = rs.getInt("music_nmusic");
                return keys;
            }
            return null;
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return null;
    }
}
