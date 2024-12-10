package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dto.DatabaseConnection;
import dto.Table;

public class TableDAO {
    private Connection connection;

    public TableDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    // Fetch available tables as a HashMap (without using the Table model)
    public List<HashMap<String, Object>> getAvailableTablesWithoutModel(String date, int numberOfPeople) throws SQLException {
    	String query = "SELECT * FROM tables t " +
                "WHERE t.capacity >= ? " +  // Check for the required number of people
                "AND t.status = 'available' " + 
                "AND date = ? " +  // Table must be available on the given date
                "AND NOT EXISTS ( " +  // Ensure table is not booked
                "    SELECT 1 FROM bookings b " + 
                "    WHERE b.table_id = t.id " +
                "    AND b.booking_date = ? " +  // Check for bookings on the given date
                "    AND b.status != 'canceled' " +  // Exclude only active bookings, allow canceled ones
                ")";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, numberOfPeople);
            stmt.setString(2, date);
            stmt.setString(3, date);  

            ResultSet rs = stmt.executeQuery();
            List<HashMap<String, Object>> tables = new ArrayList<>();

            while (rs.next()) {
                HashMap<String, Object> table = new HashMap<>();
                table.put("id", rs.getInt("id"));
                table.put("table_number", rs.getString("table_number"));
                table.put("capacity", rs.getInt("capacity"));
                table.put("status", rs.getString("status"));
                tables.add(table);
            }
            return tables;
        }
    }

    // Fetch table details by ID (new method added)
    public Table getTableById(int tableId) throws SQLException {
        String query = "SELECT * FROM tables WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, tableId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Table table = new Table();
                table.setId(rs.getInt("id"));
                table.setTableNumber(rs.getString("table_number"));
                table.setCapacity(rs.getInt("capacity"));
                table.setStatus(rs.getString("status"));
                table.setDate(rs.getDate("date").toLocalDate());  // Use LocalDate if date is stored as SQL Date
                return table;
            }
            return null; // Return null if no table is found with the given ID
        }
    }

    // Fetch all tables (add this to TableDAO)
    public List<Table> getAllTables() throws SQLException {
        String query = "SELECT * FROM tables";
        List<Table> tables = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Table table = new Table();
                table.setId(rs.getInt("id"));
                table.setTableNumber(rs.getString("table_number"));
                table.setCapacity(rs.getInt("capacity"));
                table.setStatus(rs.getString("status"));
                table.setDate(rs.getDate("date").toLocalDate()); // Convert SQL Date to LocalDate
                tables.add(table);
            }
        }
        return tables;
    }
  

    // Method to check if a table exists for a specific date
    public boolean isTableAvailableForDate(int tableId, LocalDate date) throws SQLException {
        String query = "SELECT * FROM tables WHERE id = ? AND date = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, tableId);
            stmt.setDate(2, java.sql.Date.valueOf(date));  // Convert LocalDate to SQL Date
            ResultSet rs = stmt.executeQuery();
            return rs.next();  // If a record exists, return true
        }
    }

    // Method to fetch tables by date (existing method, updated for consistency)
    public List<Table> getTablesByDate(LocalDate date) throws SQLException {
        String query = "SELECT * FROM tables WHERE date = ?";
        List<Table> tables = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(date));  // Convert LocalDate to SQL Date
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Table table = new Table();
                table.setId(rs.getInt("id"));
                table.setTableNumber(rs.getString("table_number"));
                table.setCapacity(rs.getInt("capacity"));
                table.setStatus(rs.getString("status"));
                table.setDate(rs.getDate("date").toLocalDate());  // Convert SQL Date to LocalDate
                tables.add(table);
            }
        }
        
    	System.out.println("tables" + tables);

        return tables;
    }
    
 // Method to update table capacity for a specific date (using LocalDate)
    public boolean updateTableCapacity(int tableId, String newTableNumber, int newCapacity, String newStatus, LocalDate date) throws SQLException {
        String query = "UPDATE tables SET table_number = ?, capacity = ?, status = ? WHERE id = ? AND date = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newTableNumber);  // Set the new table number
            stmt.setInt(2, newCapacity);         // Set the new capacity
            stmt.setString(3, newStatus);        // Set the new status
            stmt.setInt(4, tableId);             // Set the table ID for identifying which table to update
            stmt.setDate(5, java.sql.Date.valueOf(date));  // Convert LocalDate to java.sql.Date for the date field

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;  // Return true if the update is successful (at least one row affected)
        }
    }
    
    public boolean updateTableStatus(int tableId, String newStatus, LocalDate date) throws SQLException {
        String query = "UPDATE tables SET status = ? WHERE id = ? AND date = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newStatus);        // Set the new status (e.g., 'booked')
            stmt.setInt(2, tableId);             // Set the table ID for identifying which table to update
            stmt.setDate(3, java.sql.Date.valueOf(date));  // Convert LocalDate to java.sql.Date for the date field

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;  // Return true if the update is successful (at least one row affected)
        }
    }

    // Method to remove a table (using LocalDate)
    public boolean removeTable(int tableId, LocalDate date) throws SQLException {
        String query = "DELETE FROM tables WHERE id = ? AND date = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, tableId);
            stmt.setDate(2, java.sql.Date.valueOf(date));  // Use LocalDate directly
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    // Method to add a new table (using LocalDate)
    public boolean addNewTable(String tableNumber, int capacity, String status, LocalDate date) throws SQLException {
        String query = "INSERT INTO tables (table_number, capacity, status, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tableNumber);
            stmt.setInt(2, capacity);
            stmt.setString(3, status);
            stmt.setDate(4, java.sql.Date.valueOf(date)); // Use LocalDate directly
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;  // Return true if insertion is successful
        }
    }
    
 // Method to fetch total number of tables for a specific date
    public int getTotalTablesForDate(LocalDate date) throws SQLException {
        String query = "SELECT COUNT(*) FROM tables WHERE date = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(date));  // Convert LocalDate to SQL Date
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);  // Return the count of tables for the given date
            }
            return 0;  // Return 0 if no tables exist for that date
        }
    }
    
    // Method to fetch number of confirmed tables for a specific date (i.e., tables that have bookings)
    public int getConfirmedTablesForDate(LocalDate date) throws SQLException {
        // Query to count tables where the status is 'available' and there's a confirmed booking on the specified date
        String query = "SELECT COUNT(*) FROM tables t " +
                       "WHERE t.date = ? AND t.status = 'booked' ";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(date));  // Convert LocalDate to SQL Date for the table's date
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);  // Return the count of confirmed tables (with bookings) for the given date
            }
            return 0;  // Return 0 if no confirmed bookings exist for that date
        }
    }
    

}
