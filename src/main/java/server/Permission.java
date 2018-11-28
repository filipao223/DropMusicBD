package server;

import request.Request;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class that checks if a user permissions
 */
public class Permission {

    /**
     * Checks if given user owns given playlist
     * @param username name of the user
     * @param playlist name of the playlist
     * @return true/false
     */
    public static boolean ownsPlaylist(String username, String playlist){
        //Database connection will exist
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM playlist WHERE users_user_id=" +
                    "(SELECT user_id FROM users WHERE username=\"" + username + "\") " +
                    "AND p_name=\"" + playlist + "\";");
            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return false;
    }

    public static boolean ownsMusic(String username, String music){
        //Database connection will exist
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM music WHERE nmusic=" +
                    "(SELECT nmusic FROM music WHERE m_name=\"" + music + "\") AND users_user_id=" +
                    "(SELECT user_id FROM users WHERE username=\"" + username + "\");");
            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return false;
    }
}
