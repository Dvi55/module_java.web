package com.example.moduleservlet;

import database.DataBaseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/race")
public class RaceIdSelectServlet extends HttpServlet {


    Connection conn;
    int totalNumberOfRaces;

    public void init() {
        conn = DataBaseUtil.getConnection();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT COUNT(*) AS total_races FROM races";
            ResultSet resultSet = stmt.executeQuery(sql);
            if (resultSet.next()) {
                totalNumberOfRaces = resultSet.getInt("total_races");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        StringBuilder htmlContent = new StringBuilder("<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<title>Choosing race</title>"
                + "</head>"
                + "<body>"
                + "<h1>Choose your race</h1>"
                + "<form action=\"race\" method=\"post\">"
                + "<label for=\"race-select\">Chose race:</label>"
                + "<select id=\"race-select\" name=\"selected_race\">");

        for (int i = 1; i < totalNumberOfRaces + 1; i++) {
            htmlContent.append("<option value=\"race").append(i).append("\">Race ").append(i).append("</option>");

        }
        htmlContent.append("</select>"
                + "<br>"
                + "<button type=\"submit\">Submit</button>"
                + "</form>"
                + "</body>"
                + "</html>");

        response.getWriter().write(htmlContent.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String selectedRace = request.getParameter("selected_race");

        response.sendRedirect(request.getContextPath() + "/race/" + selectedRace);
    }

    public void destroy() {

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}