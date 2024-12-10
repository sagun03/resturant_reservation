<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.BookingDAO, dto.Booking, dto.Table" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.sql.SQLException" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirm Your Booking</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 0;
        }
        h1 {
            color: #28a745;
        }
        .container {
            width: 60%;
            margin: 50px auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
        }
        .details {
            margin: 20px 0;
        }
        .btn-book {
            background-color: #28a745;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        .btn-book:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
<!-- Navigation Bar -->
<%@ include file="navbar.jsp" %>

    <div class="container">
        <h1>Confirm Your Booking</h1>
        <%
            String tableIdParam = request.getParameter("tableId");
            String date = request.getParameter("date");
            String people = request.getParameter("people");

            if (tableIdParam != null && date != null && people != null) {
                int tableId = Integer.parseInt(tableIdParam);
                int numberOfPeople = Integer.parseInt(people);

                out.println("<div class='details'>");
                out.println("<p><strong>Number of People:</strong> " + numberOfPeople + "</p>");
                out.println("<p><strong>Booking Date:</strong> " + date + "</p>");
                out.println("</div>");
        %>

        <form action="BookingServlet" method="POST">
            <input type="hidden" name="tableId" value="<%= tableId %>">
            <input type="hidden" name="date" value="<%= date %>">
            <input type="hidden" name="people" value="<%= people %>">
            <input type="submit" value="Confirm Booking" class="btn-book">
        </form>

        <%
            } else {
                out.println("<p class='error'>Invalid booking details.</p>");
            }
        %>
    </div>
</body>
</html>
