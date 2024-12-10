package dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import dto.Booking;
import dto.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private Connection connection;

    public BookingDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    // Add a new booking
    public boolean addBooking(Booking booking) throws SQLException {
        String query = "INSERT INTO bookings (user_id, table_id, booking_date, number_of_people, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
        	  System.out.println("user id in add booking : " + booking.getUserId());
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getTableId());
            stmt.setTimestamp(3, Timestamp.valueOf(booking.getBookingDate()));
            stmt.setInt(4, booking.getNumberOfPeople());
            stmt.setString(5, booking.getStatus());

            return stmt.executeUpdate() > 0;
        }
    }

    // Fetch all bookings for a user
    public List<Booking> getUserBookings(int userId) throws SQLException {
        String query = "SELECT * FROM bookings WHERE user_id = ? ORDER BY booking_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            List<Booking> bookings = new ArrayList<>();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setTableId(rs.getInt("table_id"));
                booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                booking.setNumberOfPeople(rs.getInt("number_of_people"));
                booking.setStatus(rs.getString("status"));
                booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                bookings.add(booking);
            }

            return bookings;
        }
    }

    // Cancel a booking
    public boolean cancelBooking(int bookingId) throws SQLException {
        String query = "UPDATE bookings SET status = 'canceled' WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated > 0; // Return true if booking was successfully canceled
        }
    }
    
    // Fetch all bookings (for admin view)
    public List<Booking> getAllBookings() throws SQLException {
    	String query = "SELECT b.id, b.user_id, b.table_id, b.booking_date, b.number_of_people, b.status, u.name AS user_name, b.created_at " +
                "FROM bookings b " +
                "JOIN users u ON b.user_id = u.id " +
                "ORDER BY b.booking_date DESC";
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            List<Booking> bookings = new ArrayList<>();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setTableId(rs.getInt("table_id"));
                booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                booking.setNumberOfPeople(rs.getInt("number_of_people"));
                booking.setStatus(rs.getString("status"));
                booking.setUserName(rs.getString("user_name")); 
                booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                bookings.add(booking);
            }

            return bookings;
        }
    }

    public boolean confirmBooking(int bookingId) throws SQLException {
        String query = "UPDATE bookings SET status = 'confirmed' WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated > 0;
        }
    }
    
    // Get the remaining capacity for today
    public int getRemainingCapacityForToday() throws SQLException {
        String query = "SELECT SUM(t.capacity) - COALESCE(SUM(b.number_of_people), 0) " +
                       "FROM tables t " +
                       "LEFT JOIN bookings b ON t.id = b.table_id " +
                       "WHERE b.booking_date >= CURDATE() AND b.booking_date < CURDATE() + INTERVAL 1 DAY " +
                       "AND b.status != 'canceled'";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;  // Default if no bookings
        }
    }
    
    public int getConfirmedBookingsForToday() throws SQLException {
        String query = "SELECT COUNT(*) FROM bookings WHERE status = 'confirmed' AND booking_date >= CURDATE() AND booking_date < CURDATE() + INTERVAL 1 DAY";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    public int getConfirmedBookingsTotalCapacity() throws SQLException {
        String query = "SELECT SUM(number_of_people) AS total_capacity_confirmed\n"
        		+ "FROM bookings\n"
        		+ "WHERE status = 'confirmed'\n"
        		+ "  AND booking_date >= CURDATE()\n"
        		+ "  AND booking_date < CURDATE() + INTERVAL 1 DAY;";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    public int getTotalDailyCapacity() throws SQLException {
        String query = "SELECT total_capacity FROM daily_capacity WHERE date = CURDATE()";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_capacity");
            }
            return 0;
        }
    }
    
    public Booking getBookingById(int bookingId) throws SQLException {
        String query = "SELECT * FROM bookings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setTableId(rs.getInt("table_id"));
                booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                booking.setNumberOfPeople(rs.getInt("number_of_people"));
                booking.setStatus(rs.getString("status"));
                booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return booking;
            }
            return null;
        }
    }

    public int getUpcomingBookingsCountForUser(int userId) throws SQLException {
        String query = "SELECT COUNT(*) AS booking_count FROM bookings WHERE user_id = ? AND booking_date >= NOW() AND status = 'confirmed'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("booking_count");
            }
        }
        return 0;
    }

    public int getPastBookingsCountForUser(int userId) throws SQLException {
        String query = "SELECT COUNT(*) AS booking_count FROM bookings WHERE user_id = ? AND booking_date < NOW()";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("booking_count");
            }
        }
        return 0;
    }
    
    public List<Booking> getUpcomingBookingsForUser(int userId) throws SQLException {
        List<Booking> upcomingBookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE user_id = ? AND booking_date >= ? AND  (status = 'confirmed' OR status = 'canceled' OR status = 'pending') ORDER BY booking_date ASC";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Use LocalDateTime for precise comparison
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setId(rs.getInt("id"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                    booking.setNumberOfPeople(rs.getInt("number_of_people"));
                    booking.setStatus(rs.getString("status"));
                    upcomingBookings.add(booking);
                }
            }
        }
        return upcomingBookings;
    }
    
    public List<Booking> getPastBookingsForUser(int userId) throws SQLException {
        List<Booking> pastBookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE user_id = ? AND booking_date < ? AND (status = 'confirmed' OR status = 'canceled' OR status = 'pending') ORDER BY booking_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Use LocalDateTime for precise comparison
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setId(rs.getInt("id"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                    booking.setNumberOfPeople(rs.getInt("number_of_people"));
                    booking.setStatus(rs.getString("status"));
                    pastBookings.add(booking);
                }
            }
        }
        return pastBookings;
    }
    
    
    
 // Delete booking by ID
    public void deleteBooking(int bookingId) throws SQLException {
        String query = "DELETE FROM bookings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
        }
    }

}

