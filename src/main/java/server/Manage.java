package server;

import request.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class that handles editor privileges
 */
public class Manage {

    /**
     * Makes given target user an editor
     * @param username name of the user who is making the change
     * @param target name of the user which is to be made an user
     * @param clientData basic client data, such as IP and port.
     * @return true/false
     */
    public static boolean makeEditor(String username, String target, String clientData){
        //server.Connect to database
        String optional = null;
        if (Connect.connect(clientData)){
            try{
                //Check if user exists
                if (!CheckExistence.userExists(username)){
                    System.out.println(clientData + " | " + username
                            + " | User not found.");
                    return false;
                }
                //Check if user has editor privilege
                if (!CheckExistence.userIsEditor(username)){
                    System.out.println(clientData + " | " + username
                            + " | User is not editor.");
                    return false;
                }

                //Check if target exists
                if (!CheckExistence.userExists(target)){
                    System.out.println(clientData + " | " + username
                            + " | Target user not found.");
                    return false;
                }

                //Check if target is editor already
                if (CheckExistence.userIsEditor(target)){
                    System.out.println(clientData + " | " + username
                            + " | Target user is already editor.");
                    return false;
                }

                //Make target user an editor
                Statement statement = Connect.connection.createStatement();
                statement.executeUpdate("UPDATE users SET editor=1 WHERE username=\"" + target + "\";");
                Connect.disconnect(clientData);
                return true;

            } catch (SQLException e) {
                if (e.getMessage()!=null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData + " | " + username
                        + " | Failed make target user editor " + (optional==null?".":" | " + optional));
            }
        }
        return false;
    }

    public static boolean addMusicToAlbum(String username, String album, String music, String clientData){
        String optional = null;
        //Open a database connection
        if (Connect.connect(clientData)){
            //Check if user is editor
            if (!CheckExistence.userIsEditor(username)){
                System.out.println(clientData + " | " + username
                        + " | User is not editor.");
                return false;
            }

            //Check if music exists
            if (!CheckExistence.musicExists(music)){
                System.out.println(clientData + " | " + username
                        + " | Music not found.");
                return false;
            }

            //Check if album exists
            if (!CheckExistence.albumExists(album)){
                System.out.println(clientData + " | " + username
                        + " | Album not found.");
                return false;
            }

            //Check if music is already added
            if (CheckExistence.musicInAlbum(music, album)){
                System.out.println(clientData + " | " + username
                        + " | Music already in album.");
                return false;
            }

            //Add music to album
            try{
                Statement statement = Connect.connection.createStatement();
                statement.executeUpdate("INSERT INTO music_album (music_nmusic, album_nalbum) VALUES " +
                        "((SELECT nmusic FROM music WHERE m_name=\"" + music + "\"), " +
                        "(SELECT nalbum FROM album WHERE album_name=\"" + album + "\"));");
                Connect.disconnect(clientData);
                return true;
            } catch (SQLException e) {
                if (e.getMessage()!=null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData + " | " + username
                        + " | Failed to add music to album " + (optional==null?".":" | " + optional));
            }
        }
        return false;
    }

    public static boolean removeMusicFromAlbum(String username, String album, String music, String clientData){
        String optional = null;
        //Open a database connection
        if (Connect.connect(clientData)){
            //Check if user is editor
            if (!CheckExistence.userIsEditor(username)){
                System.out.println(clientData + " | " + username
                        + " | User is not editor.");
                return false;
            }

            //Check if music exists
            if (!CheckExistence.musicExists(music)){
                System.out.println(clientData + " | " + username
                        + " | Music not found.");
                return false;
            }

            //Check if album exists
            if (!CheckExistence.albumExists(album)){
                System.out.println(clientData + " | " + username
                        + " | Album not found.");
                return false;
            }

            //Check if music is in the album
            if (!CheckExistence.musicInAlbum(music, album)){
                System.out.println(clientData + " | " + username
                        + " | Music not in album.");
                return false;
            }

            //Remove the music from the album
            try{
                Statement statement = Connect.connection.createStatement();
                statement.executeUpdate("DELETE FROM music_album WHERE music_nmusic=" +
                        "(SELECT nmusic FROM music WHERE m_name=\"" + music + "\") AND album_nalbum=" +
                        "(SELECT nalbum FROM album WHERE album_name=\"" + album + "\");");
                Connect.disconnect(clientData);
                return true;
            } catch (SQLException e) {
                if (e.getMessage()!=null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData + " | " + username
                        + " | Failed to remove music to album " + (optional==null?".":" | " + optional));
            }
        }
        return false;
    }

    public static boolean deleteItem(String username, String type, String name, String clientData){
        String optional = null;
        //Open a database connection
        if (Connect.connect(clientData)){
            //Check if user is editor
            if (!CheckExistence.userIsEditor(username)){
                System.out.println(clientData + " | " + username
                        + " | User is not editor.");
                return false;
            }

            switch (type){
                case "music":
                    if (deleteMusic(username, name, clientData)){
                        return true;
                    }
                    break;
                case "artist":
                    if (deleteArtist(username, name, clientData)){
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    private static boolean deleteMusic(String username, String music, String clientData){
        //Check if music exists
        if (!CheckExistence.musicExists(music)){
            System.out.println(clientData + " | " + username
                    + " | Music not found.");
            return false;
        }

        //Check if this music is in any album
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM music_album WHERE music_nmusic=" +
                    "(SELECT nmusic FROM music WHERE m_name=\"" + music + "\")");
            if (rs.next()){
                System.out.println(clientData + " | " + username
                        + " | Music belongs to one or more albums.");
                return false;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }

        //Check if this music is in any playlist
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM playlist_music WHERE music_nmusic=" +
                    "(SELECT nmusic FROM music WHERE m_name=\"" + music + "\");");
            if (rs.next()){
                System.out.println(clientData + " | " + username
                        + " | Music belongs to one or more playlists.");
                return false;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }

        //Remove the music
        try{
            Statement statement = Connect.connection.createStatement();
            //Get music ID
            ResultSet rs = statement.executeQuery("SELECT nmusic FROM music WHERE m_name=\"" + music + "\";");
            if (rs.next()){
                int id = rs.getInt("nmusic");
                //Delete music
                statement.executeUpdate("DELETE FROM music WHERE nmusic=" + id + ";");
                return true;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return false;
    }

    private static boolean deleteArtist(String username, String artist, String clientData){
        //Check if artist exists
        if (!CheckExistence.artistExists(artist)){
            System.out.println(clientData + " | " + username
                    + " | Artist not found.");
            return false;
        }

        //Check if any albums have this artist
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM album WHERE artist_nartist=" +
                    "(SELECT nartist FROM artist WHERE a_name=\"" + artist + "\")");
            if (rs.next()){
                System.out.println(clientData + " | " + username
                        + " | Artist has one or more albums.");
                return false;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }

        //Delete the artist
        try{
            Statement statement = Connect.connection.createStatement();
            //Get artist ID
            ResultSet rs = statement.executeQuery("SELECT nartist FROM artist WHERE a_name=\"" + artist + "\";");
            if (rs.next()){
                int id = rs.getInt("nartist");
                //Delete music
                statement.executeUpdate("DELETE FROM artist WHERE nartist=" + id + ";");
                return true;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return false;
    }
}
