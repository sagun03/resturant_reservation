package restaurantbooking.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import dao.TableDAO;

/**
 * Servlet implementation class UpdateCapacityServlet
 */
@WebServlet("/UpdateCapacityServlet")
public class UpdateCapacityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateCapacityServlet() {
        super();
    }

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    String message = "";
    String messageType = "";

    try {
        TableDAO tableDAO = new TableDAO();
        String date = request.getParameter("date");
        LocalDate tableDate = LocalDate.parse(date);

        if ("update".equals(action)) {
            // Update table capacity
            int tableId = Integer.parseInt(request.getParameter("tableId"));
            String newTableNumber = request.getParameter("newTableNumber");
            int newCapacity = Integer.parseInt(request.getParameter("newTableCapacity"));
            String newStatus = request.getParameter("newTableStatus");

            // Update the table
            boolean isUpdated = tableDAO.updateTableCapacity(tableId, newTableNumber, newCapacity, newStatus, tableDate);

            if (isUpdated) {
                message = "Table capacity updated successfully!";
                messageType = "success";
            } else {
                message = "Failed to update table capacity.";
                messageType = "error";
            }
        } else if ("add".equals(action)) {
            // Add a new table
            String tableNumber = request.getParameter("newTableNumber");
            int newCapacity = Integer.parseInt(request.getParameter("newTableCapacity"));
            String newStatus = request.getParameter("newTableStatus");
            

            boolean isAdded = tableDAO.addNewTable(tableNumber, newCapacity, newStatus, tableDate);
            if (isAdded) {
                message = "New table added successfully!";
                messageType = "success";
            } else {
                message = "Failed to add new table.";
                messageType = "error";
            }
        } else if ("remove".equals(action)) {
            // Remove an existing table
            int tableId = Integer.parseInt(request.getParameter("removeTable"));
            boolean isRemoved = tableDAO.removeTable(tableId, tableDate);
            if (isRemoved) {
                message = "Table removed successfully!";
                messageType = "success";
            } else {
                message = "Failed to remove table.";
                messageType = "error";
            }
        }

        // Redirect with the message and type
        request.setAttribute("message", message);
        request.setAttribute("messageType", messageType);
        request.getRequestDispatcher("manageCapacity.jsp?date=" + date).forward(request, response);
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("message", e.getMessage());
        request.setAttribute("messageType", "error");
        request.getRequestDispatcher("manageCapacity.jsp?date=" + request.getParameter("date")).forward(request, response);
    }
}

}

