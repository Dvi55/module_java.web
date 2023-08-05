package com.example.moduleservlet;

import java.io.*;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import static java.sql.DriverManager.getConnection;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;
    private static final long serialVersionUID = 1L;
    private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/race";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123";
    private static final String DRIVER_CLASS = "org.mariadb.jdbc.Driver";
    Connection conn = null;

    public void init() {
        message = "Hello World!";
        try {
            Class.forName(DRIVER_CLASS);
            Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html><body>");
        out.println("<h3>Race details</h3>");
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT r.id AS race_id, h.position as user_horse_place " +
                    "FROM horses h " +
                    "INNER JOIN races r ON h.race_id = r.id " +
                    "WHERE h.id = user_horse_id;";
            ResultSet resultSet = stmt.executeQuery(sql);
            out.println("<table border=1><tr>");
            out.println("<th>Race number</th><th>User horse position</th><tr>");
            while (resultSet.next()) {
                int n = resultSet.getInt("id");
                String nm = resultSet.getString("position");
                out.println("<tr><td>" + n + "</td><td>" + nm + "</td><tr>");
            }
            out.println("</table></body></html>");
            resultSet.close();
            stmt.close();
            out.close();
        } catch (Exception e) {
            out.println("error");
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