import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class LoginLogout {
    public static boolean login(String username, String password, String clientData){
        //Connect to database
        String optional = null;
        Date date = new Date();
        if (Connect.connect(clientData)){
            try{
                Statement statement = Connect.connection.createStatement();
                statement.executeUpdate("UPDATE users SET login=1, last_login=\""
                        + new java.sql.Timestamp(date.getTime()) + "\""
                        + " WHERE username=\"" + username + "\""
                        + " AND user_password=\"" + password + "\";");
                Connect.disconnect(clientData);
                return true;
            } catch (SQLException e) {
                if (e.getMessage() != null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData + " | " + username
                        + " | Failed to login " + (optional==null?".":" | " + optional));
                return false;
            }
        }
        return false;
    }

    public static boolean logout(String username, String clientData){
        //Connect to database
        String optional = null;
        if (Connect.connect(clientData)){
            try{
                Statement statement = Connect.connection.createStatement();
                statement.executeUpdate("UPDATE users SET login=0"
                        + " WHERE username=\"" + username + "\";");
                Connect.disconnect(clientData);
                return true;
            } catch (SQLException e) {
                if (e.getMessage() != null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData + " | " + username
                        + " | Failed to log out " + (optional==null?".":" | " + optional));
                return false;
            }
        }
        return false;
    }
}
