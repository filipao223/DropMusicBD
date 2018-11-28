package server;

import request.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class that handles registers
 */
public class Register {
    /**
     * Creates given user in the database, first checking if the user already exists
     * @param username username of the user
     * @param password password of the user
     * @param firstname user's first name
     * @param lastname user's last name
     * @param clientData basic client data, such as IP and port.
     * @return true/false
     */
    public static boolean register(String username, String password, String firstname, String lastname, String clientData){
        //server.Connect to database
        String optional = null;
        int editor = 0;
        if (Connect.connect(clientData)){
            try{
                //Check if user already exists
                if (CheckExistence.userExists(username)){
                    System.out.println(clientData + " | " + username
                            + " | User already exists.");
                    return false;
                }
                //Check if first user
                if (isFirstUser()){
                    editor=1;
                }
                Statement statement = Connect.connection.createStatement();
                statement.executeUpdate("INSERT INTO users " +
                        "(username, user_password, firstname, lastname, editor) VALUES " +
                        "(\"" + username + "\",\"" + password + "\"," +
                        "\"" + firstname + "\",\"" + lastname + "\"," + editor + ");");
                statement.close();
                Connect.disconnect(clientData);
                return true;
            } catch (SQLException e) {
                if (e.getMessage()!=null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData + " | " + username
                        + " | Failed to register " + (optional==null?".":" | " + optional));
            }
        }
        return false;
    }

    /**
     * Checks if there are any users on the database, so that the first user to be entered is an editor.
     * @return true/false
     */
    private static boolean isFirstUser(){
        //Already has database connection
        try{
            Statement stmt = Connect.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users;");
            if (!rs.next() ) {
                rs.close();
                stmt.close();
                if (Request.DEV_MODE) System.out.println("First user added.");
                return true;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return false;
    }
}
