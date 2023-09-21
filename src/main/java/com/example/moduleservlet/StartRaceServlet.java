package com.example.moduleservlet;

import database.DataBaseUtil;
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
    private static List<Thread> horseThreads = new ArrayList<>();
    private List<Horse> horses = new ArrayList<>();
    private static int lastInsertedRaceId;

    Connection conn;

    public void init() {
        conn = DataBaseUtil.getConnection();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
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
            throws IOException {
        horses.clear();
        horseThreads.clear();
        int numHorses = Integer.parseInt(request.getParameter("numHorses"));
        int userHorse = Integer.parseInt(request.getParameter("userHorse"));
        startRace(numHorses);

        String insertRaceSql = "INSERT INTO races (date, total_horses, user_horse_id) VALUES (?, ?, ?)";
        lastInsertedRaceId = 0;
        try {
            PreparedStatement insertRaceStatement = conn.prepareStatement(insertRaceSql, Statement.RETURN_GENERATED_KEYS);
            insertRaceStatement.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
            insertRaceStatement.setInt(2, numHorses);
            insertRaceStatement.setInt(3, userHorse);
            insertRaceStatement.executeUpdate();
            ResultSet rs = insertRaceStatement.getGeneratedKeys();
            if (rs.next()) {
                lastInsertedRaceId = rs.getInt(1);
            }
            rs.close();
            insertRaceStatement.close();

            String updateRaceSql = "UPDATE races SET user_horse_id = ? WHERE id = ?";
            userHorse = Integer.parseInt(lastInsertedRaceId + "" + userHorse);
            try {
                PreparedStatement updateRaceStatement = conn.prepareStatement(updateRaceSql);
                updateRaceStatement.setInt(1, userHorse);
                updateRaceStatement.setInt(2, lastInsertedRaceId);
                int rowsUpdated = updateRaceStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Останній запис у таблиці races оновлено успішно.");
                } else {
                    System.out.println("Не вдалося оновити останній запис у таблиці races.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String sql = "INSERT INTO horses (id, race_id, position) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            for (Horse horse : horses) {

                int uniqueHorseId = Integer.parseInt(lastInsertedRaceId + "" + horse.getId());
                ps.setInt(1, uniqueHorseId);
                ps.setInt(2, lastInsertedRaceId);
                ps.setInt(3, horse.getPlace());

                ps.addBatch();
            }
            ps.executeBatch();
            ps.close();


            response.sendRedirect(request.getContextPath() + "/race/race" + lastInsertedRaceId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void startRace(int numHorses) {
        for (int i = 1; i <= numHorses; i++) {
            Horse horse = new Horse("Horse " + (i));
            horses.add(horse);
            Thread thread = new Thread(horse);
            horseThreads.add(thread);
            thread.start();
        }

        try {
            for (Thread thread : horseThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
