package database;

import lombok.Data;
import org.mariadb.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

@Data
public class DataBaseUtil {
    private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/race";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123";
    private static final String DRIVER_CLASS = "org.mariadb.jdbc.Driver";

    public static Connection getConnection() {
        Connection conn;
        try {
            Class.forName(DRIVER_CLASS);
            conn = (Connection) DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
}
