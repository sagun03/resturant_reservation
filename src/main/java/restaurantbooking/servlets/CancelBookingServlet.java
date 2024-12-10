package restaurantbooking.servlets;

import jakarta.servlet.ServletException;
import dao.BookingDAO;
import dao.TableDAO;
import dto.Booking;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet implementation class CancelBookingServlet
 */
@WebServlet("/CancelBookingServlet")
public class CancelBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CancelBookingServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int bookingId = Integer.parseInt(request.getParameter("bookingId"));
    String userType = request.getParameter("userType");
    String message = "";  // Default message
    String messageType = "";
    
    try {
        BookingDAO bookingDAO = new BookingDAO();
        Booking booking = bookingDAO.getBookingById(bookingId);

        boolean isCanceled = bookingDAO.cancelBooking(bookingId);
        TableDAO tableDAO = new TableDAO();
        
        if (isCanceled) {
            // Set a success message
            boolean tableUpdated = tableDAO.updateTableStatus(booking.getTableId(), "available", booking.getBookingLocalDate());

            
            if (tableUpdated) {
                // Set success message if both booking and table status update are successful
                message = "Booking confirmed and table status updated to 'booked'.";
                messageType = "success";
            } else {
                // Set error message if the table status couldn't be updated
                message = "Error updating the table status.";
                messageType = "error";
            }
            
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType);
            // Redirect based on user type
            if ("user".equals(userType)) {
                // If the request is from a user, redirect to the viewBookings.jsp
                request.getRequestDispatcher("viewBookings.jsp").forward(request, response);
            } else {
                // If the request is from an admin, forward to the admin bookings page
                request.getRequestDispatcher("adminBookings.jsp").forward(request, response);
            } 
        } else {
            // Set an error message
            request.setAttribute("message", "Error occurred while canceling the booking.");
            request.setAttribute("messageType", "error"); // Error message class
            
            // Redirect based on user type
            if ("user".equals(userType)) {
            	request.getRequestDispatcher("viewBookings.jsp").forward(request, response);  // Go back to the user view bookings page in case of error
            } else {
            	request.getRequestDispatcher("adminBookings.jsp").forward(request, response);
            }
        }
    } catch (SQLException e) {
        // Set an error message for SQLException
        request.setAttribute("message", "Error canceling the booking: " + e.getMessage());
        request.setAttribute("messageType", "error"); // Error message class
        
        request.getRequestDispatcher("errorPage.jsp").forward(request, response);
    }
}
   /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Call doGet method as POST is not necessary here
        doGet(request, response);
    }
}
