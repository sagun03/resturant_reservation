package dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private int id;
    private int userId;
    private int tableId;
    private LocalDateTime bookingDate;
    private int numberOfPeople;
    private String status;
    private LocalDateTime createdAt;
    private String userName;

    // Constructors
    public Booking() {}

    public Booking(int userId, int tableId, LocalDateTime bookingDate, int numberOfPeople, String status) {
        this.userId = userId;
        this.tableId = tableId;
        this.bookingDate = bookingDate;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }
    
    public LocalDate getBookingLocalDate() {
        return bookingDate.toLocalDate();
    }
    
    public String getBookingDateAsString() {
        if (bookingDate != null) {
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return bookingDate.format(formatter);
        }
        return null; 
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUserName() {
        return userName;  // Getter for user name
    }

    public void setUserName(String userName) {
        this.userName = userName; 
    }
}
