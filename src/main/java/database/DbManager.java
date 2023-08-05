package database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DbManager {
    private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/race";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123";

    //о количестве проведенных забегов и информации о месте
//лошади, на которую поставил пользователь.
    private static Map<String, Integer> displayRaceStats() {
        String query = "Select h.race_id,  h.position from horses h where h.id == user_horse_id";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            Map<String, Integer> result = new HashMap<>();
            int raceId = resultSet.getInt("id");
            int horsePlace = resultSet.getInt("place");
            result.put("raceId", raceId);
            result.put("horsePlace", horsePlace);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//отображает информацию о забеге: дата проведения, общее кол-во лошадей,
//место каждой из них и номер лошади на которую ставил пользователь.
    private static void displayRaceAndBetsInfo(int raceId) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT r.race_id, r.race_date, r.total_horses, h.horse_id, h.horse_name, h.horse_place, ub.user_id, ub.bet_id " +
                             "FROM races r " +
                             "JOIN horses h ON r.race_id = h.race_id " +
                             "LEFT JOIN user_bets ub ON r.race_id = ub.race_id AND h.horse_id = ub.horse_id " +
                             "WHERE r.race_id = ?"
             )) {

            statement.setInt(1, raceId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int raceIdResult = resultSet.getInt("race_id");
                Date raceDate = resultSet.getDate("race_date");
                int totalHorses = resultSet.getInt("total_horses");
                int horseId = resultSet.getInt("horse_id");
                String horseName = resultSet.getString("horse_name");
                int horsePlace = resultSet.getInt("horse_place");
                int userId = resultSet.getInt("user_id");
                int betId = resultSet.getInt("bet_id");

                System.out.println("Race ID: " + raceIdResult + ", Date: " + raceDate + ", Total Horses: " + totalHorses +
                        ", Horse ID: " + horseId + ", Horse Name: " + horseName + ", Horse Place: " + horsePlace +
                        ", User ID: " + userId + ", Bet ID: " + betId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

