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
import java.sql.SQLException;

import dto.DatabaseConnection;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ProfileServlet() {
        super();
    }

    /**
     * Handles HTTP POST requests to update user profile.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String name = request.getParameter("name");
        String phoneNumber = request.getParameter("phone");
        
        // Retrieve user ID from session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int userId = (int) session.getAttribute("user_id");

        // Update the user's profile in the database
        try (Connection connection = DatabaseConnection.getConnection()) {
            String updateQuery = "UPDATE users SET name = ?, phone_number = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(updateQuery);
            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
            
            // Update session attributes with the new data
            session.setAttribute("username", name);
            session.setAttribute("phone_number", phoneNumber);

            // Redirect to dashboard or profile page with success message
            response.sendRedirect("DashboardDataServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            // Redirect to profile page with an error message
            request.setAttribute("error", "Failed to update profile. Please try again.");
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        }
    }
}
