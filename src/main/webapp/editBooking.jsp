<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.Booking" %>
<%@ page import="dao.BookingDAO" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Booking</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .dashboard-container {
            background-color: #fff;
            padding: 40px;
            margin: 50px auto;
            border-radius: 10px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
            max-width: 600px;
        }
        h2 {
            text-align: center;
            color: #333;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            font-weight: bold;
        }
        .form-group input {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .submit-btn {
            background-color: #4CAF50;
            color: #fff;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .submit-btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>

<div class="dashboard-container">
    <h2>Edit Your Booking</h2>
    <% 
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        BookingDAO bookingDAO = new BookingDAO();
        Booking booking = bookingDAO.getBookingById(bookingId);
    %>
    <form action="updateBooking.jsp" method="post">
        <input type="hidden" name="bookingId" value="<%= booking.getId() %>" />
        <div class="form-group">
            <label for="bookingDate">Booking Date:</label>
            <input type="date" id="bookingDate" name="bookingDate" value="<%= booking.getBookingDate() %>" required />
        </div>
     <%--    <div class="form-group">
            <label for="seats">Number of Seats:</label>
            <input type="number" id="seats" name="seats" value="<%= booking.getSeats() %>" required min="1" />
        </div> --%>
        <button type="submit" class="submit-btn">Update Booking</button>
    </form>
</div>

</body>
</html>
