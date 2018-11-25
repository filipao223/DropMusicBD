import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class ResultFormatter {
    public String format(ResultSetMetaData meta, ArrayList<Object> values){
        StringBuilder str = new StringBuilder();
        try{
            for (int i=1; i<meta.getColumnCount(); i++){
                str.append(meta.getColumnName(i));
                str.append("   ");
            }
            str.append('\n');
            while (!values.isEmpty()){
                for (int i=1; i<meta.getColumnCount(); i++){
                    int type = meta.getColumnType(i);
                    switch (type){
                        case Types.INTEGER:
                            str.append(String.valueOf((int)values.get(0)));
                            break;
                        case Types.VARCHAR:
                        case Types.TIMESTAMP:
                            str.append((String)values.get(0));
                            break;
                        default:
                            str.append(values.get(0));
                    }
                    str.append(" ");
                    values.remove(0);
                }
                str.append('\n');
            }
            System.out.println(str.toString());
        } catch (SQLException e){
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return null;
    }
}
