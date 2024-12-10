package restaurantbooking.servlets;
import java.time.LocalDate;

import dao.CapacityDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class CheckAvailabilityServlet
 */
@WebServlet("/CheckAvailabilityServlet")
public class CheckAvailabilityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckAvailabilityServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String date = request.getParameter("date");
        LocalDate inputDate = LocalDate.parse(date);

        if (inputDate.isBefore(LocalDate.now())) {
            // If it's a past date, set an error message
            request.setAttribute("availability", "Cannot check availability for past dates.");
        } else {
	        try {
	            // Check capacity for the date
	            CapacityDAO capacityDAO = new CapacityDAO();
	            int remainingCapacity = capacityDAO.getRemainingCapacity(date);
	            
	            String availability;
	            if (remainingCapacity == -1) {
	                availability = "Capacity not yet defined for " + date + ". Please try again later.";
	            } else if (remainingCapacity > 0) {
	                availability = "Available slots for " + date + ": " + remainingCapacity;
	            } else {
	                availability = "Fully booked for " + date + ".";
	            }
	            request.setAttribute("availability", availability);
	        } catch (Exception e) {
	            request.setAttribute("availability", "Error: " + e.getMessage());
	        }
        }

        request.getRequestDispatcher("checkAvailability.jsp").forward(request, response);
    }

}
