package restaurantbooking.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.DatabaseConnection;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserServlet() {
        super();
    }

    /**
     * Handles HTTP GET requests.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            // Invalidate the session and redirect to login page
        	HttpSession session = request.getSession(false);
        	if (session != null) {
                session.invalidate();  // Invalidates the session
            }
          
            response.sendRedirect("login.jsp");  // Redirect to login page
        } else {
            // Default action (redirect to login page or any other page)
            response.sendRedirect("login.jsp");
        }
    }

    /**
     * Handles HTTP POST requests for registration and login.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            // Handle user registration
        	 String name = request.getParameter("name");
             String email = request.getParameter("email");
             String password = request.getParameter("password");
             String phoneNumber = request.getParameter("phone_number");
             String role = request.getParameter("role");
             
             if (password.length() < 3) {
                 // If password is too short, redirect to registration page with an error message
                 request.setAttribute("error", "Password must be at least 3 characters long.");
                 request.getRequestDispatcher("register.jsp").forward(request, response);
                 return; // Stop further execution
             }

            try (Connection connection = DatabaseConnection.getConnection()) {
                // Check if the email already exists in the database
                String checkEmailQuery = "SELECT * FROM users WHERE email = ?";
                PreparedStatement checkStmt = connection.prepareStatement(checkEmailQuery);
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // If email exists, redirect to the registration page with an error message
                    request.setAttribute("error", "Email already exists.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                } else {
                    // Insert the new user into the database
                	String insertUserQuery = "INSERT INTO users (name, email, password, phone_number, role) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = connection.prepareStatement(insertUserQuery);
                    insertStmt.setString(1, name);
                    insertStmt.setString(2, email);
                    insertStmt.setString(3, password);
                    insertStmt.setString(4, phoneNumber);
                    insertStmt.setString(5, role);
                    insertStmt.executeUpdate();

                    // Redirect to the login page after successful registration
                    response.sendRedirect("login.jsp");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("error", "Database error. Please try again later.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }

        } else if ("login".equals(action)) {
            // Handle user login
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            try (Connection connection = DatabaseConnection.getConnection()) {
                // Validate user credentials
                String loginQuery = "SELECT * FROM users WHERE email = ? AND password = ?";
                PreparedStatement stmt = connection.prepareStatement(loginQuery);
                stmt.setString(1, email);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // User found, create a session and set user details
                    HttpSession session = request.getSession();
                    session.setAttribute("user_id", rs.getInt("id"));
                    session.setAttribute("username", rs.getString("name"));
                    session.setAttribute("role", rs.getString("role"));
                    session.setAttribute("email", rs.getString("email"));
                    session.setAttribute("phone_number", rs.getString("phone_number"));
                    
                    // Redirect to dashboard
                    response.sendRedirect("DashboardDataServlet");
                } else {
                    // Invalid credentials, redirect to login page with an error message
                    request.setAttribute("error", "Invalid email or password.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("error", "Database error. Please try again later.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }
    }
}
