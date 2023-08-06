package com.example.moduleservlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/stat")
public class StatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/race";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123";
    private static final String DRIVER_CLASS = "org.mariadb.jdbc.Driver";
    Connection conn;

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
            Statement stmt = conn.createStatement();
            String sql = "SELECT r.id, r.total_horses " +
            "FROM races r " +
            "LEFT JOIN horses h ON r.user_horse_id = h.id;";

            ResultSet resultSet = stmt.executeQuery(sql);
            out.println("<table border=1><tr>" + "<td><b>Race Id</b></td>" + "<td><b>User horse place</b></td>"
                    + "<td><b>Total horses count</b></td></tr>");
            while (resultSet.next()) {
                int n = resultSet.getInt("id");
                String nm = resultSet.getString("user_horse_position");
                int horseCount = resultSet.getInt("total_horses");
                out.println("<tr>" + "<td>" + n + "</td>" + "<td>" + nm + "</td>" + "<td>" + horseCount
                        + "</td></tr>");
            }
            out.println("</table></body></html>");
            resultSet.close();
            stmt.close();
            out.close();
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