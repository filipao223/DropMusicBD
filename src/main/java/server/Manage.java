package server;

import request.Request;

import java.sql.SQLException;
import java.sql.Statement;

public class Manage {
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
}
