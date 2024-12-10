<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.TableDAO, dto.Table" %>
<%@ page import="java.util.List, java.util.HashMap" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book a Table</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    
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
        .back-button {
            width: fit-content;
            left: 0;
            top: -65px;
            margin: 10px; 
            position: absolute;
            font-size: 18px;
            color: #4CAF50;
            text-decoration: none;
            padding: 10px 20px;
            border: 1px solid #4CAF50;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .back-button:hover {
            background-color: #4CAF50;
            color: white;
        }
        .container {
            width: 40%;
            margin: 100px auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            position: relative;
            display: flex;
            align-items: center;
            flex-direction: column;
        }
        .table {
            border: 1px solid #ccc;
            margin: 10px 0;
            padding: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background-color: #e9f7e9;
            width: 100%;
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
        .error {
            color: red;
        }
        .form {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 50px;
        }
    </style>
</head>
<body>

<!-- Navigation Bar -->
<%@ include file="navbar.jsp" %>

<div class="container">
<a href="DashboardDataServlet" class="back-button"><i class="fas fa-arrow-left"></i> Back</a>

    <h1>Book a Table</h1>
   <form action="FetchAvailableTablesServlet" method="POST" class="form">
        <div>
            <label for="date">Booking Date: <span style="color: red;">*</span></label>
            <input type="date" id="date" name="date" required><br><br>
        </div>
        <div>
            <label for="people">Number of People: <span style="color: red;">*</span></label>
            <input type="number" id="people" name="people" required min="1"><br><br>
        </div>
        <input type="submit" value="Search Available Tables" class="btn-book">
    </form>


    <%
        String date = request.getParameter("date");
        int numberOfPeople = 0;
        if (request.getParameter("people") != null) {
            numberOfPeople = Integer.parseInt(request.getParameter("people"));
        }

        if (date != null && numberOfPeople > 0) {
            TableDAO tableDAO = new TableDAO();
            List<HashMap<String, Object>> availableTables = tableDAO.getAvailableTablesWithoutModel(date, numberOfPeople);

            if (availableTables.isEmpty()) {
                out.println("<p class='error'>No available tables for the selected date and number of people.</p>");
            } else {
                for (HashMap<String, Object> table : availableTables) {
                    int tableId = (int) table.get("id");
                    String tableNumber = (String) table.get("table_number");
                    int capacity = (int) table.get("capacity");

                    out.println("<div class='table'>");
                    out.println("<span>Table: " + tableNumber + " | Capacity: " + capacity + "</span>");
                    out.println("<a  href='confirmBooking.jsp?tableId=" + tableId + "&date=" + date + "&people=" + numberOfPeople + "' class='btn-book'>Book Now</a>");
                    out.println("</div>");
                }
            }
        }
    %>
</div>

</body>
</html>
