import java.sql.SQLException;
import java.sql.Statement;

public class Critique {
    public static boolean critique(String username, String text, int rating, String albumName, String clientData){
        // TODO Check if user already made critique
        String optional = null;
        //Connect to database
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
