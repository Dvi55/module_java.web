package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/race";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123";

    public static void createHorseTable() throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS horses (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "race_id INT NOT NULL," +
                    "position INT NOT NULL," +
                    "FOREIGN KEY (race_id) REFERENCES races(id)" +
                    ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createRaceTable() throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS races " +
                    "(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                    "date DATE NOT NULL, " +
                    "total_horses INT NOT NULL, " +
                    "user_horse_id INT NOT NULL)";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void createUserBetsTable() throws SQLException {
//        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
//             Statement statement = connection.createStatement()) {
//
//            String sql = "CREATE TABLE IF NOT EXISTS user_bets " +
//                    "(id INT NOT NULL AUTO_INCREMENT, " +
//                    "race_id INT NOT NULL, " +
//                    "horse_id INT NOT NULL, " +
//                    "FOREIGN KEY (race_id) REFERENCES races(id), " +
//                    "FOREIGN KEY (horse_id) REFERENCES horses(id), " +
//                    "PRIMARY KEY (id))";
//            statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws SQLException {
        createHorseTable();
        createRaceTable();
    }
}