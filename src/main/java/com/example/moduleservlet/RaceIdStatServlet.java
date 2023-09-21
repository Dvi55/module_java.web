package com.example.moduleservlet;

import database.DataBaseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/race/*")
public class RaceIdStatServlet extends HttpServlet {
    Connection conn;
    int raceIdInt;

    public void init() {
        conn = DataBaseUtil.getConnection();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        try {
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<html><body>");
            out.println("<h3>Race details</h3>");

            String raceId = req.getPathInfo().substring(5);
            raceIdInt = Integer.parseInt(raceId);

            String sql = "SELECT r.date AS race_date, r.total_horses, h.position AS user_horse_position, r.user_horse_id " +
                    "FROM races r " +
                    "INNER JOIN horses h ON r.id = h.race_id AND h.id = r.user_horse_id " +
                    "WHERE r.id = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, raceIdInt);
            ResultSet resultSet = preparedStatement.executeQuery();

            out.println(getTable(resultSet));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getHorseId(int raceId, int userHorseId) {
        String stringRaceId = String.valueOf(raceId);
        String stringUserHorseId = String.valueOf(userHorseId);
        int commonPrefixLength = 0;
        while (commonPrefixLength < stringRaceId.length() && commonPrefixLength < stringUserHorseId.length() &&
                stringRaceId.charAt(commonPrefixLength) == stringUserHorseId.charAt(commonPrefixLength)) {
            commonPrefixLength++;
        }

        if (commonPrefixLength > 0) {
            stringUserHorseId = stringUserHorseId.substring(commonPrefixLength);
        }
        return Integer.parseInt(stringUserHorseId);
    }

    private String getTable(ResultSet resultSet) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border=1><tr>").append("<td><b>Race date</b></td>").append("<td><b>Total horses</b></td>")
                .append("<td><b>User horse position</b></td>").append("<td><b>User horse number</b></td></tr>");
        while (resultSet.next()) {
            LocalDate date = resultSet.getDate("race_date").toLocalDate();
            int totalHorse = resultSet.getInt("total_horses");
            int userHorsePosition = resultSet.getInt("user_horse_position");
            int userHorseId = getHorseId(raceIdInt, resultSet.getInt("user_horse_id"));
            sb.append("<tr>" + "<td>").append(date).append("</td>").append("<td>").append(totalHorse).append("</td>")
                    .append("<td>").append(userHorsePosition).append("</td>").append("<td>").append(userHorseId).append("</td></tr>");
        }
        sb.append("</table></body></html>");
        return sb.toString();
    }
}
