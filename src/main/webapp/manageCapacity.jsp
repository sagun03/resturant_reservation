<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.TableDAO, dto.Table" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeParseException" %>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Daily Table Capacity</title>
    <!-- Font Awesome CDN -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
<script>
    // Function to open the edit modal and populate its fields
    function openEditModal(id, tableNumber, capacity, status) {
        document.getElementById('editModal').style.display = 'block';
        document.getElementById('editTableId').value = id;
        document.getElementById('editTableNumber').value = tableNumber;
        document.getElementById('editTableCapacity').value = capacity;
        document.getElementById('editTableStatus').value = status;
    }

    // Function to open the delete modal and populate its fields
    function openDeleteModal(id, tableNumber) {
        document.getElementById('deleteModal').style.display = 'block';
        document.getElementById('deleteTableId').value = id;
        document.getElementById('deleteTableName').textContent = tableNumber;
    }

    // Function to close the modal
    function closeModal(modalId) {
        document.getElementById(modalId).style.display = 'none';
    }
</script>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .back-button {
            width: fit-content;
            left: 0;
            top: -65px;
            margin: 10px;
            position: absolute;
            font-size: 18px;
            color: #4CAF50;
            text-decoration: none;
            padding: 10px 20px;
            border: 1px solid #4CAF50;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .back-button:hover {
            background-color: #4CAF50;
            color: white;
        }

        .container {
            margin: 100px auto;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            width: 60%;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            display: flex;
            align-items: center;
            flex-direction: column;
            position: relative;
        }

        h2 {
            color: #333;
            margin-bottom: 20px;
        }

        form {
            margin-top: 20px;
            width: 100%;
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
        }

        input[type="date"], input[type="number"], select, input[type="text"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        button {
            padding: 10px 15px;
            background: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background: #45a049;
        }

        .success {
            color: green;
        }

        .error {
            color: red;
        }

        .message {
            margin-bottom: 20px;
        }

        .table-container {
            width: 80%;
            margin: 20px;
            border-collapse: collapse;
        }

        .table-container th, .table-container td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }

        .action-buttons a {
            margin: 5px;
            padding: 5px 10px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        .action-buttons a:hover {
            background-color: #45a049;
        }

        .add-table-btn {
            padding: 10px 15px;
            background: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .add-table-btn:hover {
            background: #45a049;
        }

        .add-table-form {
            display: none;
            margin-top: 20px;
            width: 50%;
            align-items: center;
            justify-content: center;
            flex-direction: column;
        }
        .form {
        width: 50%;
        align-items: center;
        }
        .modal {
        display: none; 
        position: fixed; 
        z-index: 1; 
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        overflow: auto;
        background-color: rgb(0,0,0); 
        background-color: rgba(0,0,0,0.4); 
        padding: 60px;
    }

    .modal-content {
        background-color: #fefefe;
        margin: 5% auto;
        padding: 20px;
        border: 1px solid #888;
        width: 80%;
        max-width: 400px;
        border-radius: 5px;
    }

    .close {
        color: #aaa;
        float: right;
        font-size: 28px;
        font-weight: bold;
    }

    .close:hover,
    .close:focus {
        color: black;
        text-decoration: none;
        cursor: pointer;
    }
    .edit-form {
    width: 90%
    }
    </style>
</head>

<body>

<%@ include file="navbar.jsp" %>


<div class="container">
    <a href="DashboardDataServlet" class="back-button"><i class="fas fa-arrow-left"></i> Back</a>

    <h2>Update Table Capacity</h2>

    <% 
        String message = (String) request.getAttribute("message");
        String messageType = (String) request.getAttribute("messageType");
        if (message != null) {
            out.print("<div class='message " + messageType + "'>" + message + "</div>");
        }

        // Fetch the selected date
        String selectedDate = request.getParameter("date");
LocalDate date = null;
if (selectedDate != null && !selectedDate.trim().isEmpty()) {
    try {
        date = LocalDate.parse(selectedDate);
    } catch (DateTimeParseException e) {
        // Handle invalid date formats gracefully (optional logging)
        request.setAttribute("message", "Invalid date format. Please select a valid date.");
        request.setAttribute("messageType", "error");
    }
}
        TableDAO tableDAO = new TableDAO();
        List<Table> tables = null;
        
        // Only fetch tables if the date is selected
        if (date != null) {
            tables = tableDAO.getTablesByDate(date);  // Pass LocalDate to the DAO
        }
    %>

    <!-- Date Selection -->
    <form method="GET" class="form" action="manageCapacity.jsp">
        <label for="date">Select Date</label>
        <input type="date" id="date" name="date" value="<%= selectedDate != null ? selectedDate : "" %>" required onchange="this.form.submit()">
    </form>

    <hr>

    <% if (date == null) { %>
        <!-- Show a message if no date is selected -->
        <p>Please select a date to manage the table capacity.</p>
    <% } else { %>
        <!-- Display the tables if available -->
        
        <% if (tables != null && !tables.isEmpty()) { %>
        
            <h3>Existing Tables for <%= selectedDate %>:</h3>
            <table class="table-container">
                <thead>
                    <tr>
                        <th>Table Number</th>
                        <th>Capacity</th>
                        <th>Status</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
    if (tables != null && !tables.isEmpty()) {
        for (Table table : tables) {
%>
            <tr>
                <td><%= table.getTableNumber() %></td>
                <td><%= table.getCapacity() %></td>
                <td><%= table.getStatus() %></td>
                <td><%= table.getDate() %></td>
                <td class="action-buttons">
                    <a href="javascript:void(0);" onclick="openEditModal(<%= table.getId() %>, '<%= table.getTableNumber() %>', <%= table.getCapacity() %>, '<%= table.getStatus() %>')">Edit</a>
               	 <a href="javascript:void(0);" onclick="openDeleteModal(<%= table.getId() %>, '<%= table.getTableNumber() %>')">Delete</a>
                </td>
            </tr>
<%
        }

    } else {
%>
        <tr>
            <td colspan="5">No tables available for the selected date.</td>
        </tr>
<%
    }
                    
%>
                </tbody>
            </table>
            
           <button class="add-table-btn" onclick="document.getElementById('addTableForm').style.display = 'flex';">Add Table</button>
            
        <% } else { %>
            <!-- Show Add Table Button if no tables are available -->
            <p>No tables available for the selected date.</p>
            <button class="add-table-btn" onclick="document.getElementById('addTableForm').style.display = 'flex';">Add Table</button>
        <% } %>

        <!-- Add Table Form -->
        <div class="add-table-form" id="addTableForm">
            <h3>Create a New Table</h3>
			<form method="POST" class="form" action="UpdateCapacityServlet">
			    <input type="hidden" name="action" value="add">
			    <input type="hidden" name="date" value="<%= selectedDate %>">
			
			    <label for="newTableNumber">New Table Number <span style="color: red;">*</span></label>
			    <input type="text" id="newTableNumber" name="newTableNumber" required>
			
			    <label for="newTableCapacity">New Table Capacity <span style="color: red;">*</span></label>
			    <input type="number" id="newTableCapacity" name="newTableCapacity" min="1" required>
			
			    <label for="newTableStatus">Table Status <span style="color: red;">*</span></label>
			    <select id="newTableStatus" name="newTableStatus" required>
			        <option value="available">Available</option>
			        <option value="booked">Booked</option>
			    </select>
			
			    <button type="submit" name="action" value="add">Create New Table</button>
			</form>
        </div>
    <% } %>
</div>

<!-- Edit Table Modal -->
<div id="editModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('editModal')">&times;</span>
        <h3>Edit Table</h3>
        <form method="POST" class="edit-form" action="UpdateCapacityServlet">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="date" value="<%= selectedDate %>">
            <input type="hidden" id="editTableId" name="tableId">

            <label for="editTableNumber">Table Number</label>
            <input type="text" id="editTableNumber" name="newTableNumber" required>

            <label for="editTableCapacity">Table Capacity</label>
            <input type="number" id="editTableCapacity" name="newTableCapacity" min="1" required>

            <label for="editTableStatus">Table Status</label>
            <select id="editTableStatus" name="newTableStatus" required>
                <option value="available">Available</option>
                <option value="booked">Booked</option>
            </select>

            <button type="submit">Update Table</button>
        </form>
    </div>
</div>

<!-- Delete Table Modal -->
<div id="deleteModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('deleteModal')">&times;</span>
        <h3>Are you sure you want to delete this table?</h3>
        <p id="deleteTableName"></p>
        <form method="POST" action="UpdateCapacityServlet">
            <input type="hidden" name="action" value="remove">
            <input type="hidden" name="date" value="<%= selectedDate %>">
            <input type="hidden" id="deleteTableId" name="removeTable">
            <button type="submit">Yes, Delete</button>
            <button type="button" onclick="closeModal('deleteModal')">Cancel</button>
        </form>
    </div>
</div>

</body>
</html>
