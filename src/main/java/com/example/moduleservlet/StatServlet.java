package com.example.moduleservlet;

import database.DataBaseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/stat")
public class StatServlet extends HttpServlet {
    Connection conn;

    public void init() {
        conn = DataBaseUtil.getConnection();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        try {
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<html><body>");
            out.println("<h3>Race details</h3>");
            Statement stmt = conn.createStatement();
            String sql = "SELECT\n" +
                    "  r.id, \n" +
                    "  r.total_horses,\n" +
                    "  h.position AS user_horse_position\n" +
                    "FROM races r\n" +
                    "LEFT JOIN horses h \n" +
                    "  ON r.user_horse_id = h.id AND h.id = r.user_horse_id;";
            ResultSet resultSet = stmt.executeQuery(sql);

            out.println(getStatisticTable(resultSet));

            resultSet.close();
            stmt.close();
            out.close();
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

    private String getStatisticTable(ResultSet resultSet) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border=1><tr>").append("<td><b>Race Id</b></td>").append("<td><b>User horse place</b></td>")
                .append("<td><b>Total horses count</b></td></tr>");
        while (resultSet.next()) {
            int n = resultSet.getInt("id");
            String nm = resultSet.getString("user_horse_position");
            int horseCount = resultSet.getInt("total_horses");
            sb.append("<tr>").append("<td>").append(n).append("</td>").append("<td>")
                    .append(nm).append("</td>").append("<td>").append(horseCount).append("</td></tr>");
        }
        sb.append("</table></body></html>");
        return sb.toString();
    }
}