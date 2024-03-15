
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Customer Age Distribution Report</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<div style="width: 60%; margin: auto;">
    <canvas id="customerAgeChart"></canvas>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        fetch('/api/managing-staff/report/customer-age-distribution')
            .then(response => response.json())
            .then(data => {
                const ageGroups = data.map(entry => entry[0]);
                const counts = data.map(entry => entry[1]);

                const ctx = document.getElementById('customerAgeChart').getContext('2d');
                const customerAgeChart = new Chart(ctx, {
                    type: 'bar', // This type can be changed as needed
                    data: {
                        labels: ageGroups,
                        datasets: [{
                            label: 'Customer Age Distribution',
                            data: counts,
                            backgroundColor: [
                                // Define colors for each age group
                            ],
                            borderColor: [
                                // Define border colors for each age group
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
                                text: 'Distribution of Customer Ages'
                            }
                        }
                    },
                });
            });
    });
</script>

</body>
</html>
