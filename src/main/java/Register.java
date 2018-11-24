import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Register {
    public static boolean register(String username, String password, String firstname, String lastname, String clientData){
        //Connect to database
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
