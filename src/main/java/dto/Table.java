package dto;

import java.time.LocalDate;

public class Table {
    private int id;
    private String tableNumber;
    private int capacity;
    private String status;
    private LocalDate date;  // Changed to LocalDate if only date is relevant

    // Constructor
    public Table(int id, String tableNumber, int capacity, String status, LocalDate date) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = status;
        this.date = date;
    }

    // Default constructor (optional)
    public Table() {}

    // Getters
    public int getId() {
        return id;
    }

    public String getTableNumber() {
        return tableNumber;
    }
    
    public LocalDate getDate() {  // Updated to LocalDate if needed
        return date;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {  // Updated to LocalDate
        this.date = date;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Table{" +
               "id=" + id +
               ", tableNumber='" + tableNumber + '\'' +
               ", capacity=" + capacity +
               ", status='" + status + '\'' +
               ", date=" + date +
               '}';
    }
}
