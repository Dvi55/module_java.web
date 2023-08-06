package com.example.moduleservlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;

@WebServlet("/race/*")
public class RaceIdStatServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;
    private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/race";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123";
    private static final String DRIVER_CLASS = "org.mariadb.jdbc.Driver";
    Connection conn;
    int raceIdInt;

    public void init() {
        try {
            Class.forName(DRIVER_CLASS);
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<html><body>");
            out.println("<h3>Race details</h3>");
            String raceId = req.getPathInfo();
            raceId = raceId.substring(5);

            raceIdInt = Integer.parseInt(raceId);
            String sql = "SELECT r.date AS race_date, r.total_horses, h.position AS user_horse_position, r.user_horse_id " +
                    "FROM races r " +
                    "INNER JOIN horses h ON r.id = h.race_id AND h.id = r.user_horse_id " +
                    "WHERE r.id = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, raceIdInt);
            ResultSet resultSet = preparedStatement.executeQuery();
            out.println("<table border=1><tr>" + "<td><b>Race date</b></td>" + "<td><b>Total horses</b></td>"
                    + "<td><b>User horse position</b></td>" + "<td><b>User horse number</b></td></tr>");
            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("race_date").toLocalDate();
                int totalHorse = resultSet.getInt("total_horses");
                int userHorsePosition = resultSet.getInt("user_horse_position");
                int userHorseId = resultSet.getInt("user_horse_id");
                out.println("<tr>" + "<td>" + date + "</td>" + "<td>" + totalHorse + "</td>" + "<td>" + userHorsePosition + "</td>" + "<td>" + userHorseId
                        + "</td></tr>");
            }
            out.println("</table></body></html>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {

        // Close connection object.
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
