package com.example.moduleservlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Horse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/race/start")
public class StartRaceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/race";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123";
    private static final String DRIVER_CLASS = "org.mariadb.jdbc.Driver";
    private List<Horse> horses = new ArrayList<>();

    Connection conn;

    public void init() {
        try {
            Class.forName(DRIVER_CLASS);
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h1>Horse Race Simulation</h1>");
        out.println("<form method=\"post\">");
        out.println("Number of Horses: <input type=\"number\" name=\"numHorses\"><br>");
        out.println("Your Horse: <input type=\"number\" name=\"userHorse\"><br>");
        out.println("<input type=\"submit\" value=\"Start Race\">");
        out.println("</form>");
        out.println("</body></html>");

        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int numHorses = Integer.parseInt(request.getParameter("numHorses"));
        int userHorse = Integer.parseInt(request.getParameter("userHorse"));
        int lastInsertedRaceId = -1;
        horses.clear();

        for (int i = 1; i <= numHorses; i++) {
            Horse horse = new Horse(i);

            horses.add(horse);
        }

        for (Horse horse : horses) {
            horse.startRace();
        }

        String insertRaceSql = "INSERT INTO races (date, total_horses, user_horse_id) VALUES (?, ?, ?, ?)";
        String getLastInsertedIdSql = "SELECT LAST_INSERT_ID() as last_id";
        try {
            PreparedStatement insertRaceStatement = conn.prepareStatement(insertRaceSql);
            insertRaceStatement.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
            insertRaceStatement.setInt(2, numHorses);
            insertRaceStatement.setInt(3, userHorse);
            insertRaceStatement.executeUpdate();
            insertRaceStatement.close();


            Statement lastIdStatement = conn.createStatement();
            ResultSet resultSet = lastIdStatement.executeQuery(getLastInsertedIdSql);
            if (resultSet.next()) {
                lastInsertedRaceId = resultSet.getInt("last_id");
            }
            lastIdStatement.close();

            for (Horse horse : horses) {
                String insertHorseSql = "INSERT INTO horses (race_id, position) VALUES (?, ?)";
                PreparedStatement insertHorseStatement = conn.prepareStatement(insertHorseSql);
                insertHorseStatement.setInt(1, lastInsertedRaceId);
                insertHorseStatement.setInt(2, horse.getPlace());
                insertHorseStatement.executeUpdate();
                insertHorseStatement.close();
            }

            response.sendRedirect(request.getContextPath() + "/race-results");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        response.sendRedirect(request.getContextPath() + "/race/" + lastInsertedRaceId);
    }
}
