package com.example.moduleservlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/race")
public class RaceIdSelectServlet extends HttpServlet {

    private static final long serialVersionUID = 3L;
    private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/race";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123";
    private static final String DRIVER_CLASS = "org.mariadb.jdbc.Driver";
    Connection conn;
    int totalNumberOfRaces;

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
            throws ServletException, IOException {
        // Отримання вибору гонки з форми
        String selectedRace = request.getParameter("selected_race");

        // Перенаправлення на інший сервлет та передача параметру
        response.sendRedirect(request.getContextPath() + "/race/" + selectedRace);
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