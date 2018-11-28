package server;

import request.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Permission {
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
}
