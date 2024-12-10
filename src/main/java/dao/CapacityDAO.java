package dao;

import java.sql.*;

import dto.DatabaseConnection;

public class CapacityDAO {
    private Connection connection;

    public CapacityDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean updateCapacity(String date, int capacity) throws SQLException {
        
    	String query = "INSERT INTO daily_capacity (date, total_capacity) " +
                "VALUES (?, ?) ON DUPLICATE KEY UPDATE total_capacity = ?";
		 try (PreparedStatement stmt = connection.prepareStatement(query)) {
		     stmt.setString(1, date);
		     stmt.setInt(2, capacity);
		     stmt.setInt(3, capacity);
		     return stmt.executeUpdate() > 0;
		 }
    }
    
    public int getRemainingCapacity(String date) throws SQLException {
        String query = "SELECT total_capacity - (SELECT SUM(t.capacity) FROM bookings b " +
                       "JOIN tables t ON b.table_id = t.id WHERE DATE(b.booking_date) = ?) AS remaining " +
                       "FROM daily_capacity WHERE date = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, date);
            stmt.setString(2, date);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("remaining");
            }	
            return -1;
        }
    }
}
