<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="restaurantbooking.servlets.DashboardDataServlet" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .dashboard-container {
            background-color: #fff;
            padding: 40px;
            margin: 50px auto;
            border-radius: 10px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
            max-width: 80%;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
            text-align: center;
            font-size: 40px;
        }

        .welcome-message {
            text-align: center;
            color: #777;
            font-size: 28px;
            margin-bottom: 30px;
        }

        .user-info {
            background-color: #f9f9f9;
            padding: 20px;
            margin-bottom: 50px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            display: flex;
            justify-content: center;
            flex-direction: column;
            align-items: center;
        }

        .user-info p {
            font-size: 16px;
            color: #555;
            margin: 8px 0;
        }

        .role-section {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: space-around;
            margin: 30px auto;
            align-items: center;
        }

        .card {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
            width: 280px;
            padding: 20px;
            text-align: center;
            transition: transform 0.3s ease;
        }

        .card img {
            width: 100%;
            height: 200px;
            object-fit: cover;
            border-radius: 10px;
        }

        .card h3 {
            color: #4CAF50;
            font-size: 20px;
            margin-top: 15px;
        }

        .card p {
            font-size: 16px;
            color: #555;
        }

        .card a {
            text-decoration: none;
            color: #fff;
            background-color: #4CAF50;
            padding: 10px 20px;
            border-radius: 5px;
            margin-top: 10px;
            display: inline-block;
            transition: background-color 0.3s ease;
        }

        .card a:hover {
            background-color: #45a049;
        }

        .role-section .card:hover {
            transform: translateY(-10px);
        }

        #kpiChart {
            width: 300px !important;
            height: 300px !important;
        }

        .chart {
            display: flex;
            justify-content: space-around;
            align-items: center;
        }
    </style>

</head>

<body>

    <!-- Navigation Bar -->
    <%@ include file="navbar.jsp" %>

    <!-- Dashboard Content -->
    <div class="dashboard-container">
        <div class="chart">
            <div>
                <h1>Dashboard</h1>

                <div class="welcome-message">
                    Welcome, <strong><%= session.getAttribute("username") %></strong>!
                </div>
            </div>

            <!-- User Info Section -->
            <div class="user-info card">
                <% 
                    String role = (String) session.getAttribute("role");
                    if ("admin".equals(role)) {
                %>
                <canvas id="kpiChart" width="400px" height="300px"></canvas>
                <script>
                    // Parse JSON data from the servlet
                    const ctx = document.getElementById('kpiChart').getContext('2d');
                    const kpiData = JSON.parse('<%= request.getAttribute("kpiData") %>');
                    console.log(kpiData);

                    new Chart(ctx, {
                        type: 'pie', // Change type to 'pie' for a pie chart
                        data: {
                            labels: ['Today\'s Capacity', 'Booked Tables', 'Remaining Tables'], // Labels for the bars
                            datasets: [{
                                label: 'KPI Metrics', // Label for the entire dataset
                                data: [kpiData.totalCapacity, kpiData.confirmedTables, kpiData.remainingSpace],
                                backgroundColor: ['#4CAF50', '#FF5722', '#FFC107'], // Colors for each slice
                                borderColor: ['#388E3C', '#D32F2F', '#FF9800'],  // Border color for each slice (optional)
                                borderWidth: 1 // Border thickness for each slice
                            }]
                        },
                        options: {
                            responsive: true, // Ensures chart resizes with window size
                            plugins: {
                                legend: {
                                    display: true, // Enables the legend
                                    position: 'bottom'
                                },
                                title: {
                                    display: true, // Display the title
                                    text: 'Today\'s KPI'
                                }
                            }
                        }
                    });
                </script>
                <% } else if ("customer".equals(role)) { %>
                <canvas id="kpiChart" width="400" height="300"></canvas>

<script>
    const ctx = document.getElementById('kpiChart').getContext('2d');

    const kpiData = JSON.parse('<%= request.getAttribute("kpiData") %>');
    console.log(kpiData)
    new Chart(ctx, {
        type: 'bar', // Use a bar chart
        data: {
            labels: ['Bookings'], // Single label for the booking comparison
            datasets: [
                {
                    label: 'Upcoming Bookings',
                    data: [kpiData.upcomingBookings], // Upcoming bookings value
                    backgroundColor: '#4CAF50', // Green color
                    borderColor: '#388E3C',
                    borderWidth: 1
                },
                {
                    label: 'Past Bookings',
                    data: [kpiData.pastBookings], // Past bookings value
                    backgroundColor: '#FF5722', // Red color
                    borderColor: '#D32F2F',
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true, // Ensure chart resizes with window size
            plugins: {
                legend: {
                    display: true,
                    position: 'bottom'
                },
                title: {
                    display: true,
                    text: 'Upcoming vs Past Bookings'
                }
            },
            scales: {
                y: {
                    beginAtZero: true // Ensures the bars start at zero
                }
            },
            elements: {
                bar: {
                    stacked: true // Enable stacked bars
                }
            }
        }
    });
</script>
              
                <% } %>
            </div>
        </div>

        <% if ("admin".equals(role)) { %>
        <!-- Admin Section -->
        <div class="role-section">
            <div class="card">
                <img src="https://jkblobstore.blob.core.windows.net/jk-images/mangaedailycapacity?text=Manage+Capacity" alt="Manage Capacity">
                <h3>Manage Daily Capacity</h3>
                <p>View and manage the daily seat capacity for the restaurant.</p>
                <a href="manageCapacity.jsp">Go to Manage Capacity</a>
            </div>

            <div class="card">
                <img src="https://jkblobstore.blob.core.windows.net/jk-images/viewormangebookings?text=View+Bookings" alt="View Bookings">
                <h3>View All Bookings</h3>
                <p>Manage and view all restaurant bookings at any time.</p>
                <a href="adminBookings.jsp">View Bookings</a>
            </div>
        </div>
        <% } else if ("customer".equals(role)) { %>
        <!-- Customer Section -->
        <div class="role-section">
            <div class="card">
                <img src="https://jkblobstore.blob.core.windows.net/jk-images/bookatable?text=Book+Table" alt="Book a Table">
                <h3>Book a Table</h3>
                <p>Reserve your favorite spot at our restaurant quickly and easily.</p>
                <a href="bookTable.jsp"><i class="fas fa-calendar-alt"></i> Book Now</a>
            </div>

            <div class="card">
                <img src="https://jkblobstore.blob.core.windows.net/jk-images/sss.png?text=View+Bookings" alt="View Bookings">
                <h3>View Bookings</h3>
                <p>Review your past and upcoming reservations in one place.</p>
                <a href="viewBookingsServlet"><i class="fas fa-eye"></i> View Bookings</a>
            </div>

            <div class="card">
                <img src="https://jkblobstore.blob.core.windows.net/jk-images/profile?text=Edit+Profile" alt="Edit Profile">
                <h3>Edit Profile</h3>
                <p>Keep your information up-to-date for a personalized experience.</p>
                <a href="profile.jsp"><i class="fas fa-user-edit"></i> Edit Profile</a>
            </div>
        </div>
        <% } %>
    </div>

</body>

</html>
