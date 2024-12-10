<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            /* background-color: #f8d7da; */
            color: #721c24;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 80%;
            max-width: 600px;
            margin: 100px auto;
            padding: 30px;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .error-header {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .error-message {
            font-size: 18px;
            margin-bottom: 20px;
        }
        .btn-back {
            background-color: #721c24;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
        }
        .btn-back:hover {
            background-color: #5c1a20;
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>

    <div class="container">
        <div class="error-header">
            Error Occurred
        </div>
        <div class="error-message">
            <%= request.getAttribute("errorMessage") %>
        </div>
        <a href="javascript:history.back()" class="btn-back">Go Back</a>
    </div>

</body>
</html>
