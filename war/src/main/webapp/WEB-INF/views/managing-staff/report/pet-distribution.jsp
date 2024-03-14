<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Pet Distribution Report</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<div style="width: 60%; margin: auto;">
    <canvas id="petDistributionChart"></canvas>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        fetch('/api/managing-staff/report/pet-distribution')
            .then(response => response.json())
            .then(data => {
                const labels = data.map(entry => entry[0]);
                const counts = data.map(entry => entry[1]);

                const ctx = document.getElementById('petDistributionChart').getContext('2d');
                const petDistributionChart = new Chart(ctx, {
                    type: 'pie', // or 'bar', 'doughnut', etc.
                    data: {
                        labels: labels,
                        datasets: [{
                            label: 'Pet Distribution',
                            data: counts,
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.2)',
                                'rgba(54, 162, 235, 0.2)',
                                'rgba(255, 206, 86, 0.2)',
                                'rgba(75, 192, 192, 0.2)',
                                'rgba(153, 102, 255, 0.2)',
                                'rgba(255, 159, 64, 0.2)'
                            ],
                            borderColor: [
                                'rgba(255, 99, 132, 1)',
                                'rgba(54, 162, 235, 1)',
                                'rgba(255, 206, 86, 1)',
                                'rgba(75, 192, 192, 1)',
                                'rgba(153, 102, 255, 1)',
                                'rgba(255, 159, 64, 1)'
                            ],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: {
                                position: 'top',
                            },
                            title: {
                                display: true,
                                text: 'Pet Distribution by Type'
                            }
                        }
                    },
                });
            });
    });
</script>

</body>
</html>
