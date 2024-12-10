package restaurantbooking.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.BookingDAO;
import dto.Booking;

/**
 * Servlet implementation class viewBookigs
 */
@WebServlet("/viewBookingsServlet")
public class viewBookingsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public viewBookingsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Fetch the logged-in user's ID from the session
            int userId = (Integer) request.getSession().getAttribute("user_id");

            // Fetch upcoming bookings for the user
            BookingDAO bookingDAO = new BookingDAO();
            List<Booking> upcomingBookings = bookingDAO.getUpcomingBookingsForUser(userId);
            List<Booking> pastBookings = bookingDAO.getPastBookingsForUser(userId);

            
            // Set bookings in request
            request.setAttribute("upcomingBookings", upcomingBookings);
            request.setAttribute("pastBookings", pastBookings);
            
            System.out.println("upcomingBookings" + upcomingBookings);
            // Forward to JSP
            request.getRequestDispatcher("viewBookings.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error fetching bookings", e);
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
 // Handle POST requests for deleting or editing bookings
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String action = request.getParameter("action");

            BookingDAO bookingDAO = new BookingDAO();

            if ("delete".equals(action)) {
                // Delete booking
                bookingDAO.deleteBooking(bookingId);
            } else if ("edit".equals(action)) {
                // Redirect to edit page or logic
                response.sendRedirect("editBooking.jsp?bookingId=" + bookingId);
            }

            // Redirect back to view bookings page
            response.sendRedirect("viewBookings");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error performing action on booking", e);
        }
    }

}
