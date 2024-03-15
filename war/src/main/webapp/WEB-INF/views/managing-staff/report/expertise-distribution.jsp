
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Veterinarian Expertise Distribution Report</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<div style="width: 60%; margin: auto;">
    <canvas id="veterinarianExpertiseChart"></canvas>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        fetch('/api/managing-staff/report/veterinarian-expertise-distribution')
            .then(response => response.json())
            .then(data => {
                const expertiseAreas = data.map(entry => entry[0]);
                const counts = data.map(entry => entry[1]);

                const ctx = document.getElementById('veterinarianExpertiseChart').getContext('2d');
                const veterinarianExpertiseChart = new Chart(ctx, {
                    type: 'bar', // Feel free to change the chart type
                    data: {
                        labels: expertiseAreas,
                        datasets: [{
                            label: 'Veterinarian Expertise Distribution',
                            data: counts,
                            backgroundColor: [
                                // Define a color for each expertise area
                            ],
                            borderColor: [
                                // Define border colors if necessary
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
                                text: 'Distribution of Veterinarian Expertise Areas'
                            }
                        }
                    },
                });
            });
    });
</script>

</body>
</html>
