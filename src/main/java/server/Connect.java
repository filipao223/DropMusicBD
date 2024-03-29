package server;

import request.Request;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class that handles database connections
 */
public class Connect {
    public static Connection connection;

    /**
     * Connects to database
     * @param clientData basic client data, such as IP and port.
     * @return true/false
     */
    public static boolean connect(String clientData){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost/dropmusic?" +
                    "user=root&password=ProjetoBD2018&useSSL=false");
            return true;
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
            System.out.println(clientData + " | Failed to connect to database.");
            return false;
        }
    }

    /**
     * Disconnects from the database
     * @param clientData basic client data, such as IP and port.
     */
    public static void disconnect(String clientData){
        try{
            if (connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
            System.out.println(clientData + " | Failed to disconnect from database.");
        }
    }
}
