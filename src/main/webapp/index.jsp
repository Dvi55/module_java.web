<%@ page import="database.CreateTable" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hippodrome</title>
</head>
<body>
<h1><%= "Hello in hippodrome!" %>
</h1>
<br/>
<%
    try {
        CreateTable.createRaceTable();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
%>
<%
    try {
        CreateTable.createHorseTable();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
%>
<a href="race">Race statistic</a>
<a href="stat">Overall statistics</a>
<a href="race/start">Start new race</a>
</body>
</html>