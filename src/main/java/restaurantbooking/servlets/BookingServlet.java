package restaurantbooking.servlets;

import jakarta.servlet.ServletException;
import dao.BookingDAO;
import dto.Booking;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class BookingServlet
 */
@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public BookingServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        int tableId = Integer.parseInt(request.getParameter("tableId"));
        String date = request.getParameter("date");
        int numberOfPeople = Integer.parseInt(request.getParameter("people"));
        Integer userId = null; 
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            userId = (Integer) session.getAttribute("user_id");
            if (userId == null) {
                // If the user is not logged in, redirect to login page
                response.sendRedirect("login.jsp");
                return;
            }
        } else {
            // No session found, possibly redirect to login
            response.sendRedirect("login.jsp");
            return;
        }

        try {            
        	
            BookingDAO bookingDAO = new BookingDAO();
            Booking booking = new Booking();
            booking.setTableId(tableId);
            booking.setBookingDate(LocalDateTime.parse(date + "T00:00:00"));
            booking.setNumberOfPeople(numberOfPeople);
            booking.setStatus("pending");
            booking.setUserId(userId);

            boolean bookingAdded = bookingDAO.addBooking(booking);
            
            if (bookingAdded) {
                // No capacity update yet, as booking is pending
            	request.setAttribute("message", "Booking request sent");
                request.setAttribute("messageType", "success");
                response.sendRedirect("DashboardDataServlet");
            } else {
                request.getRequestDispatcher("errorPage.jsp").forward(request, response);

            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("errorPage.jsp");
        }
        
     
       
    }
}
