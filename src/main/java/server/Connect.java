package server;

import request.Request;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    public static Connection connection;

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

    public static void disconnect(String clientData){
        try{
            connection.close();
        } catch (SQLException e) {
            if (Request.DEV_MODE) e.printStackTrace();
            System.out.println(clientData + " | Failed to disconnect from database.");
        }
    }
}
