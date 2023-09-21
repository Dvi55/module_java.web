package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    private static Connection connection = DataBaseUtil.getConnection();

    public static void createHorseTable() throws SQLException {
        try {

            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS horses (" +
                    "id BIGINT PRIMARY KEY, " +
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
        try {
            Statement statement = connection.createStatement();
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

    public static void main(String[] args) throws SQLException {
        createRaceTable();
        createHorseTable();
    }
}