import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Search {
    public static boolean search(String query, String clientData){
        String optional = null;
        //Connect to database
        if (Connect.connect(clientData)){
            try{
                Statement statement = Connect.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = statement.executeQuery(query);
                ResultSetMetaData meta = rs.getMetaData();
                if (rs.next()){
                    ArrayList<Object> values = new ArrayList<>();

                    rs.beforeFirst();
                    while(rs.next()){
                        for (int i=1; i<meta.getColumnCount(); i++){
                            values.add(rs.getObject(i));
                        }
                    }

                    new ResultFormatter().format(meta, values);
                    rs.close();
                    statement.close();
                    Connect.disconnect(clientData);
                    return true;
                }
            } catch (SQLException e) {
                if (e.getMessage()!=null) optional = e.getMessage();
                if (Request.DEV_MODE) e.printStackTrace();
                System.out.println(clientData  + " | Failed to search with query: " + query
                        + (optional==null?".":" | " + optional));
            }
        }
        return false;
    }
}
