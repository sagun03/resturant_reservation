<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .register-container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 300px;
            text-align: center;
        }
        h2 {
            color: #333;
            margin-bottom: 20px;
        }
        input[type="text"], input[type="email"], input[type="password"], input[type="tel"] {
            width: 100%;
            padding: 12px;
            margin: 8px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type="submit"] {
            width: 100%;
            padding: 12px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
        .error {
            color: red;
            margin-top: 10px;
            font-size: 14px;
        }
        .link {
            display: block;
            margin-top: 10px;
            font-size: 14px;
            text-decoration: none;
            color: #4CAF50;
        }
        .link:hover {
            text-decoration: underline;
        }
        .role {
        	display: flex;
        	margin-bottom: 20px;
        	margin-top: 10px;
        }
        .required {
            color: red;
            margin-left: 5px;
        }
        .form-group {
            text-align: left;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>

<div class="register-container">
    <h2>Register</h2>
    <form action="UserServlet" method="post">
        <div class="form-group">
            <label for="name">User Name<span class="required">*</span></label>
            <input type="text" id="name" name="name" placeholder="User Name" required>
        </div>
        <div class="form-group">
            <label for="email">Email<span class="required">*</span></label>
            <input type="email" id="email" name="email" placeholder="Email" required>
        </div>
        <div class="form-group">
            <label for="password">Password<span class="required">*</span></label>
            <input type="password" id="password" name="password" placeholder="Password" required>
        </div>
        <div class="form-group">
            <label for="phone_number">Phone Number<span class="required">*</span></label>
            <input type="tel" id="phone_number" name="phone_number" placeholder="Phone Number" pattern="[0-9]{10}" required>
        </div>
        <div class="role">
            <label><input type="radio" id="customer" name="role" value="customer" checked> Customer</label>
            <label><input type="radio" id="admin" name="role" value="admin"> Admin</label>
        </div>

        <input type="submit" value="Register">
        <input type="hidden" name="action" value="register">
    </form>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
        <p class="error"><%= error %></p>
    <%
        }
    %>

    <a href="login.jsp" class="link">Already have an account? Login here</a>
</div>

</body>
</html>
