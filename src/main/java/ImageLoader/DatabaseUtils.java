package ImageLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {
    //"jdbc:sqlserver://LAPTOP-CJ2V6H2P:1433;databaseName=login;user=sa;password=123;encrypt=false;"

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlserver://LAPTOP-CJ2V6H2P:1433;databaseName=login;user=sa;password=123;encrypt=false;");
    }
}

