package restaurantbooking.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import dao.BookingDAO;
import dao.TableDAO;
import dto.Booking;
import org.json.JSONObject;

/**
 * Servlet implementation class DashboardDataServlet
 */
@WebServlet("/DashboardDataServlet")
public class DashboardDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DashboardDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
        String role = (String) request.getSession().getAttribute("role");
        BookingDAO bookingDAO = new BookingDAO();
        TableDAO tableDAO = new TableDAO();

        if ("customer".equals(role)) {
            Integer userId = (Integer) request.getSession().getAttribute("user_id");
            
            System.out.println(" user_id:" + userId);
            if (userId != null) {
            	// Get upcoming and past confirmed bookings count for the customer
                List<Booking> upcomingBookingsList = bookingDAO.getUpcomingBookingsForUser(userId);
                System.out.println("upcomingBookings" + upcomingBookingsList);

                List<Booking> pastBookingsList = bookingDAO.getPastBookingsForUser(userId);
                JSONObject kpiData = new JSONObject();
                kpiData.put("upcomingBookings", upcomingBookingsList.size());
                kpiData.put("pastBookings", pastBookingsList.size());
                // Set counts as attributes for easy display on dashboard
                request.setAttribute("kpiData", kpiData.toString());
                request.setAttribute("upcomingBookings", upcomingBookingsList.size());
                request.setAttribute("pastBookings", pastBookingsList.size());
            }
        } else if ("admin".equals(role)) {
        	LocalDate date = LocalDate.now();
        	
            // Admin-specific data
        	int totalTables = tableDAO.getTotalTablesForDate(date);
        	int confirmedTables = tableDAO.getConfirmedTablesForDate(date);
            int remainingTables = totalTables - confirmedTables;
            
            JSONObject kpiData = new JSONObject();
            kpiData.put("totalCapacity", totalTables);
            kpiData.put("confirmedTables", confirmedTables);
            kpiData.put("remainingSpace", remainingTables);

            
            request.setAttribute("kpiData", kpiData.toString());
            request.setAttribute("totalCapacity", totalTables);
            request.setAttribute("confirmedTables", confirmedTables);
            request.setAttribute("remainingSpace", remainingTables);
        }

        // Forward to dashboard.jsp
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    } catch (SQLException e) {
        throw new ServletException("Error fetching dashboard data", e);
    }
}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
