import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
public class connect {
    private ResultSet resultSet;
    private Statement statement;
    public static List db_list = new ArrayList();
    public static List table_list = new ArrayList();
    public static List column_list = new ArrayList();
    public static List data_list = new ArrayList();

    connect() throws SQLException {
        String url = "jdbc:hive2://bigdata129.depts.bingosoft.net:22129/user35_db";
        Properties properties = new Properties();
        properties.setProperty("driverClassName", "org.apache.hive.jdbc.HiveDriver");
        properties.setProperty("user", "user35");
        properties.setProperty("password", "pass@bingo35");
        Connection connection = DriverManager.getConnection(url, properties);
        statement = connection.createStatement();
    }

    public Object[] get_db_list() throws SQLException {
        resultSet = statement.executeQuery("show databases");
        while (resultSet.next()) {
            String db_name = resultSet.getString(1);
            db_list.add(db_name);
        }
        return db_list.toArray();
    }

    public Object[] get_table_list(String db_name) throws SQLException {
        resultSet = statement.executeQuery("show tables from "+db_name);
        table_list = new ArrayList();
        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            table_list.add(tableName);
        }
        return table_list.toArray();
    }

    public Object[] get_column_list(String table_name) throws SQLException {
        resultSet = statement.executeQuery("show columns from "+table_name);
        column_list = new ArrayList();
        while (resultSet.next()) {
            String columnName = resultSet.getString(1);
            column_list.add(columnName);
        }
        return column_list.toArray();
    }

    public Object[] get_column_name_list(String sql_state) throws SQLException {
        List column_name = new ArrayList();
        resultSet = statement.executeQuery(sql_state);
        int column = resultSet.getMetaData().getColumnCount();
        System.out.println(column);
        for(int i = 1; i <= column; i++){
            column_name.add(resultSet.getMetaData().getColumnName(i));
        }
        return column_name.toArray();
    }

    public ResultSet get_data_resultset(String sql_state) throws SQLException {
        resultSet = statement.executeQuery(sql_state);
        return resultSet;
    }
}
