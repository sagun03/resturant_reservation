package restaurantbooking.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.TableDAO;

/**
 * Servlet implementation class FetchAvailableTablesServlet
 */
@WebServlet("/FetchAvailableTablesServlet")
public class FetchAvailableTablesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchAvailableTablesServlet() {
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
	    int numberOfPeople = Integer.parseInt(request.getParameter("people"));
	    
	    // Initialize the availableTables list
	    List<HashMap<String, Object>> availableTables = new ArrayList<>();

	    try {
	        // Call the DAO to fetch available tables
	        TableDAO tableDAO = new TableDAO();
	        availableTables = tableDAO.getAvailableTablesWithoutModel(date, numberOfPeople);
	    } catch (SQLException e) {
	        // Log the exception for debugging
	        e.printStackTrace();
	        
	        // Set an error message as a request attribute
	        request.setAttribute("errorMessage", "An error occurred while fetching available tables. " + "error: " + e.getMessage());
	        
	        // Forward to an error page (you could also redirect to an error page instead)
	        request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
	        return;  // Exit the method
	    }

	    // Pass the data to JSP for display
	    request.setAttribute("availableTables", availableTables);
	    request.setAttribute("date", date);
	    request.setAttribute("numberOfPeople", numberOfPeople);
	    
	    // Forward the request to the JSP for rendering
	    request.getRequestDispatcher("/bookTable.jsp").forward(request, response);
	}
}
