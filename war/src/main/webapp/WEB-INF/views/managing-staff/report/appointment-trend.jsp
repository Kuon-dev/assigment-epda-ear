
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Monthly Appointment Trends Report</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<div style="width: 80%; margin: auto;">
    <canvas id="monthlyAppointmentTrendsChart"></canvas>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        fetch('/api/managing-staff/report/monthly-appointment-trends')
            .then(response => response.json())
            .then(data => {
                const months = data.map(entry => `${entry[1]}/${entry[0]}`); // Format: MM/YYYY
                const counts = data.map(entry => entry[2]);

                const ctx = document.getElementById('monthlyAppointmentTrendsChart').getContext('2d');
                const monthlyAppointmentTrendsChart = new Chart(ctx, {
                    type: 'line', // Line chart to show trends over time
                    data: {
                        labels: months,
                        datasets: [{
                            label: 'Number of Appointments',
                            data: counts,
                            backgroundColor: 'rgba(54, 162, 235, 0.2)',
                            borderColor: 'rgba(54, 162, 235, 1)',
                            borderWidth: 1,
                            fill: false, // No fill for a clearer view of the trend
                        }]
                    },
                    options: {
                        scales: {
                            y: {
                                beginAtZero: true
                            }
                        },
                        responsive: true,
                        plugins: {
                            legend: {
                                position: 'bottom',
                            },
                            title: {
                                display: true,
                                text: 'Monthly Appointment Trends'
                            }
                        }
                    },
                });
            });
    });
</script>

</body>
</html>
