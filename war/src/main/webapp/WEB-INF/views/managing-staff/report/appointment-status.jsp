
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Appointment Status Distribution Report</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<div style="width: 60%; margin: auto;">
    <canvas id="appointmentStatusChart"></canvas>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        fetch('/api/managing-staff/report/appointment-status-distribution')
            .then(response => response.json())
            .then(data => {
                const statuses = data.map(entry => entry[0]);
                const counts = data.map(entry => entry[1]);

                const ctx = document.getElementById('appointmentStatusChart').getContext('2d');
                const appointmentStatusChart = new Chart(ctx, {
                    type: 'bar', // Change to 'pie', 'doughnut', etc. as preferred
                    data: {
                        labels: statuses,
                        datasets: [{
                            label: 'Appointment Status Distribution',
                            data: counts,
                            backgroundColor: [
                                // Add as many colors as there are statuses
                            ],
                            borderColor: [
                                // Add as many colors as there are statuses
                            ],
                            borderWidth: 1
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
                                position: 'top',
                            },
                            title: {
                                display: true,
                                text: 'Appointment Status Distribution'
                            }
                        }
                    },
                });
            });
    });
</script>

</body>
</html>
