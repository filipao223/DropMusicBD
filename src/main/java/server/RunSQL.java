package server;

import request.Request;

import java.sql.SQLException;
import java.sql.Statement;

public class RunSQL {
    public static boolean runSQL(String sql, String clientData){
        String optional = null;
        //OPen a database connection
        if (Connect.connect(clientData)){
            try{
                Statement statement = Connect.connection.createStatement();
                statement.executeUpdate(sql);
                Connect.disconnect(clientData);
                return true;
            } catch (SQLException e) {
                if (e.getMessage()!=null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData  + " | Failed to manage with query: " + sql
                        + (optional==null?".":" | " + optional));
            }
        }
        return false;
    }
}
