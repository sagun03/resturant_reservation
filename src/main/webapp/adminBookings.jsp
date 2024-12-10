<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.BookingDAO, dto.Booking, java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Bookings</title>
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
        h1 {
            color: #28a745;
        }
        .container {
            background-color: #fff;
            padding: 40px;
            margin: 100px auto;
            border-radius: 10px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
            max-width: 80%;
            position: relative;
        }
        table {
            width: 100%;
            margin-top: 20px;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .bookings-table th {
            background-color: #4CAF50;
            color: white;
        }
        .status-pending {
            color: orange;
        }
        .status-confirmed {
            color: green;
        }
        .status-canceled {
            color: red;
        }
        .btn {
            padding: 5px 10px;
            background-color: #28a745;
            color: white;
            text-decoration: none;
            border-radius: 3px;
        }
        .btn:hover {
            background-color: #218838;
        }
        .btn-cancel {
            background-color: red;
        }
        .btn-cancel:hover {
            background-color: darkred;
        }

        /* Style for messages */
        .alert {
            padding: 15px;
            margin: 20px 0;
            border-radius: 5px;
            font-size: 16px;
        }
        .success {
            background-color: #4CAF50;
            color: white;
        }
        .error {
            background-color: #f44336;
            color: white;
        }
    </style>
</head>
<body>

<!-- Navigation Bar -->
<%@ include file="navbar.jsp" %>

    <div class="container">
    <a href="DashboardDataServlet" class="back-button"><i class="fas fa-arrow-left"></i> Back</a>
    
        <h1>All Bookings</h1>

        <%-- Display the message if it exists --%>
        <%
            String message = (String) request.getAttribute("message");
            String messageType = (String) request.getAttribute("messageType");
            if (message != null && !message.isEmpty()) {
        %>
            <div class="alert <%= messageType %>">
                <%= message %>
            </div>
        <%
            }
        %>

        <%
            BookingDAO bookingDAO = new BookingDAO();
            List<Booking> bookings = bookingDAO.getAllBookings();

            if (bookings.isEmpty()) {
                out.println("<p>No bookings available.</p>");
            } else {
        %>

        <table class="bookings-table">
            <thead>
                <tr>
                    <th>Booking ID</th>
                    <th>User Name</th>  <!-- Display user name -->
                    <th>Table ID</th>
                    <th>Booking Date</th>
                    <th>Number of People</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Booking booking : bookings) {
                        String statusClass = "";
                        if (booking.getStatus().equals("pending")) {
                            statusClass = "status-pending";
                        } else if (booking.getStatus().equals("confirmed")) {
                            statusClass = "status-confirmed";
                        } else if (booking.getStatus().equals("canceled")) {
                            statusClass = "status-canceled";
                        }

                        // Format booking date
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String formattedDate = booking.getBookingDate().format(formatter);
                %>
                <tr>
                    <td><%= booking.getId() %></td>
                    <td><%= booking.getUserName() %></td>
                    <td><%= booking.getTableId() %></td>
                    <td><%= formattedDate %></td>
                    <td><%= booking.getNumberOfPeople() %></td>
                    <td class="<%= statusClass %>"><%= booking.getStatus() %></td>
                    <td>
                        <% if (booking.getStatus().equals("pending")) { %>
                             <a href="ConfirmBookingServlet?bookingId=<%= booking.getId() %>" class="btn">Confirm</a>
            				 <a href="CancelBookingServlet?bookingId=<%= booking.getId() %>" class="btn btn-cancel">Cancel</a>
                        <% } else if (booking.getStatus().equals("confirmed")) { %>
                            <span>Confirmed</span>
                        <% } else { %>
                            <span>Canceled</span>
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <% } %>
        
    </div>
</body>
</html>
