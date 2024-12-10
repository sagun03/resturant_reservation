<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.Booking" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Bookings</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
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
        .dashboard-container {
            background-color: #fff;
            padding: 40px;
            margin: 100px auto;
            border-radius: 10px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
            max-width: 1000px;
            display: flex;
            align-items: center;
            flex-direction: column;
            position: relative;
        }
        h2 {
            text-align: center;
            color: #333;
        }
        .bookings-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 30px;
        }
        .bookings-table th, .bookings-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .bookings-table th {
            background-color: #4CAF50;
            color: white;
        }
        .action-btns {
            display: flex;
            gap: 10px;
        }
        .action-btns a {
            text-decoration: none;
            color: #fff;
            padding: 8px 16px;
            border-radius: 5px;
        }
        .action-btns .edit-btn {
            background-color: #FF9800;
        }
        .action-btns .delete-btn {
            background-color: #F44336;
        }
        .action-btns a:hover {
            opacity: 0.8;
        }
        .no-bookings {
            text-align: center;
            color: #888;
        }
        .btn-cancel {
            background-color: red;
        }
        .btn-cancel:hover {
            background-color: darkred;
        }
        .btn {
            padding: 5px 10px;
            color: white;
            text-decoration: none;
            border-radius: 3px;
        }
    </style>
</head>
<body>

<%@ include file="navbar.jsp" %>

<div class="dashboard-container">
    <a href="DashboardDataServlet" class="back-button"><i class="fas fa-arrow-left"></i> Back</a>

    <h2>Your Upcoming Bookings</h2>

    <% 
        List<Booking> upcomingBookings = (List<Booking>) request.getAttribute("upcomingBookings");
        if (upcomingBookings == null || upcomingBookings.isEmpty()) {
    %>
        <p class="no-bookings">You have no upcoming bookings.</p>
    <% } else { %>
        <table class="bookings-table">
            <thead>
                <tr>
                    <th>Booking Date</th>
                    <th>Status</th>
                    <th>Number Of People</th>
                </tr>
            </thead>
            <tbody>
                <% for (Booking booking : upcomingBookings) { %>
                    <tr>
                        <td><%= booking.getBookingDate() %></td>
                        <td><%= booking.getStatus() %></td>
                        <td><%= booking.getNumberOfPeople() %></td>
                        
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>

    <!-- Display Past Bookings -->
    <h2>Your Past Bookings</h2>

    <% 
        List<Booking> pastBookings = (List<Booking>) request.getAttribute("pastBookings");
        if (pastBookings == null || pastBookings.isEmpty()) {
    %>
        <p class="no-bookings">You have no past bookings.</p>
    <% } else { %>
        <table class="bookings-table">
            <thead>
                <tr>
                    <th>Booking Date</th>
                    <th>Status</th>
                    <th>Number Of People</th>
                </tr>
            </thead>
            <tbody>
                <% for (Booking booking : pastBookings) { %>
                    <tr>
                        <td><%= booking.getBookingDate() %></td>
                        <td><%= booking.getStatus() %></td>
                        <td><%= booking.getNumberOfPeople() %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>

</div>

</body>
</html>
