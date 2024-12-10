<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
    HttpSession currentSession = request.getSession(false);
    if (currentSession == null || currentSession.getAttribute("username") == null) {
        response.sendRedirect("login.jsp"); 
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>navbar</title>

<style>
    .navbar {
        background-color: #4CAF50;
        overflow: hidden;
        padding: 10px 0;
    }

    .navbar a {
        float: left;
        display: block;
        color: white;
        padding: 14px 20px;
        text-align: center;
        text-decoration: none;
        font-size: 16px;
    }

    .navbar a:hover {
        background-color: #45a049;
    }

    /* Highlight the active tab */
    .navbar a.active {
        background-color: #45a049;
        color: white;
    }

    /* Logout styling */
    .navbar a.logout {
        float: right;
    }
</style>
</head>
<body>
<div class="navbar">
    <a href="DashboardDataServlet" class="<%= request.getRequestURI().endsWith("dashboard.jsp") ? "active" : "" %>">Home</a>
    <a href="profile.jsp" class="<%= request.getRequestURI().endsWith("profile.jsp") ? "active" : "" %>">Profile</a>
    <a href="UserServlet?action=logout" class="logout">Logout</a>
</div>
</body>
</html>