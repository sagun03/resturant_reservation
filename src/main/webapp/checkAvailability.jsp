<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Check Availability</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .container {
            margin: 50px auto;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            width: 60%;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #333;
            margin-bottom: 20px;
        }
        form {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
        }
        input[type="date"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        button {
            padding: 10px 15px;
            background: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background: #45a049;
        }
        .message {
            font-size: 16px;
            color: blue;
        }
    </style>
</head>
<body>

<%@ include file="navbar.jsp" %>

<div class="container">
    <h2>Check Availability</h2>
    <form action="CheckAvailabilityServlet" method="post">
        <label for="date">Select Date:</label>
        <input type="date" id="date" name="date" required>
        <button type="submit">Check</button>
    </form>

    <% 
        String availability = (String) request.getAttribute("availability");
        if (availability != null) {
    %>
        <p class="message"><%= availability %></p>
    <% } %>
</div>
</body>
</html>
