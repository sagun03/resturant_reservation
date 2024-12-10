package restaurantbooking.servlets;

import jakarta.servlet.ServletException;
import dao.BookingDAO;
import dao.CapacityDAO;
import dao.TableDAO;
import dto.Booking;
import dto.Table;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ConfirmBookingServlet
 */
@WebServlet("/ConfirmBookingServlet")
public class ConfirmBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ConfirmBookingServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        String message = "";  // Default message
        String messageType = "";  // Default message type (success/error)
        
        try {
            // Get the booking details from the database
            BookingDAO bookingDAO = new BookingDAO();
            Booking booking = bookingDAO.getBookingById(bookingId);
            
            TableDAO tableDAO = new TableDAO();

            // Confirm the booking
            boolean isConfirmed = bookingDAO.confirmBooking(bookingId);

            if (isConfirmed) {
                // After confirming the booking, update the table status to 'booked'
                boolean tableUpdated = tableDAO.updateTableStatus(booking.getTableId(), "booked", booking.getBookingLocalDate());
                
                if (tableUpdated) {
                    // Set success message if both booking and table status update are successful
                    message = "Booking confirmed and table status updated to 'booked'.";
                    messageType = "success";
                } else {
                    // Set error message if the table status couldn't be updated
                    message = "Error updating the table status.";
                    messageType = "error";
                }
            } else {
                // Error confirming the booking
                message = "Error confirming the booking.";
                messageType = "error";
            }

            // Set the message and message type in the request
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType);
            
            // Forward the request to the manageCapacity.jsp page
            String date = request.getParameter("date");  // Assuming you want to keep the date parameter
            request.getRequestDispatcher("adminBookings.jsp?date=" + date).forward(request, response);
            
        } catch (SQLException e) {
            // Handle any SQL exceptions and forward to error page
            message = "Error confirming the booking: " + e.getMessage();
            messageType = "error";
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType);
            request.getRequestDispatcher("errorPage.jsp").forward(request, response);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Calls the doGet method for simplicity
        doGet(request, response);
    }
}
