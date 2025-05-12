<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="taglib.jsp" %>
<%@ include file="Header.jsp" %>
<html>
<head>
    <title>Comparison Results</title>
    <link rel="stylesheet" href="style/main.css" type="text/css" />
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<h2 style="text-align:center; margin-top: 1em;">Your Commute Cost Comparison</h2>

<c:if test="${not empty costSummaryMap}">
    <div style="width:80%; margin: 0 auto;">

        <!-- Data Table -->
        <table border="1" style="width:100%; text-align: center; margin-bottom: 2em;">
            <thead>
            <tr>
                <th>Commute Type</th>
                <th>Total Time (min)</th>
                <th>Cost of Use (gas or fees)</th>
                <th>Maintenance Cost</th>
                <th>Insurance</th>
                <th>Total Payments</th>
                <th>1 Year Cost</th>
                <th>2 Year Cost</th>
                <th>5 Year Cost</th>
                <th>Total Cost So Far</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="entry" items="${costSummaryMap}">
                <tr>
                    <td>${entry.key}</td>
                    <td>${timeSpentMap[entry.key]}</td>
                    <!-- always show gasCostMap value -->
                    <td>
                        $${empty gasCostMap[entry.key] ? 0 : gasCostMap[entry.key]}
                    </td>
                    <td>$${maintenanceCostMap[entry.key]}</td>
                    <td>$${insuranceCostMap[entry.key]}</td>
                    <td>$${paymentCostMap[entry.key]}</td>
                    <td>$${entry.value.oneYearCost}</td>
                    <td>$${entry.value.twoYearCost}</td>
                    <td>$${entry.value.fiveYearCost}</td>
                    <td>$${entry.value.totalCost}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <!-- Charts Container -->
        <div style="display:flex; justify-content: space-between; gap: 1em;">
            <div style="flex:1;">
                <h3 style="text-align:center;">Cost Breakdown by Commute Type</h3>
                <canvas id="perTypeChart" style="width:100%; height:300px;"></canvas>
            </div>
            <div style="flex:1;">
                <h3 style="text-align:center;">Overall Cost Distribution</h3>
                <canvas id="overallChart" style="width:100%; height:300px;"></canvas>
            </div>
        </div>
    </div>

    <!-- JS to serialize maps and draw charts -->
    <script>
        const labels = [
            <c:forEach var="entry" items="${costSummaryMap}" varStatus="st">
            '${entry.key}'<c:if test="${!st.last}">,</c:if>
            </c:forEach>
        ];

        const times = [
            <c:forEach var="entry" items="${costSummaryMap}" varStatus="st">
            ${timeSpentMap[entry.key]}<c:if test="${!st.last}">,</c:if>
            </c:forEach>
        ];
        const gasCosts = [
            <c:forEach var="entry" items="${costSummaryMap}" varStatus="st">
            ${empty gasCostMap[entry.key] ? 0 : gasCostMap[entry.key]}<c:if test="${!st.last}">,</c:if>
            </c:forEach>
        ];
        const maintenanceCosts = [
            <c:forEach var="entry" items="${costSummaryMap}" varStatus="st">
            ${maintenanceCostMap[entry.key]}<c:if test="${!st.last}">,</c:if>
            </c:forEach>
        ];
        const insuranceCosts = [
            <c:forEach var="entry" items="${costSummaryMap}" varStatus="st">
            ${insuranceCostMap[entry.key]}<c:if test="${!st.last}">,</c:if>
            </c:forEach>
        ];
        const totalCosts = [
            <c:forEach var="entry" items="${costSummaryMap}" varStatus="st">
            ${entry.value.totalCost}<c:if test="${!st.last}">,</c:if>
            </c:forEach>
        ];

        // Per‑type bar chart
        new Chart(
            document.getElementById('perTypeChart').getContext('2d'),
            {
                type: 'bar',
                data: {
                    labels,
                    datasets: [
                        { label: 'Time Spent (min)', data: times },
                        { label: 'Cost of Use (gas or fees) ($)',    data: gasCosts },
                        { label: 'Maintenance ($)', data: maintenanceCosts },
                        { label: 'Insurance ($)',   data: insuranceCosts }
                    ]
                },
                options: {
                    responsive: true,
                    scales: { y: { beginAtZero: true } }
                }
            }
        );

        // Overall pie chart
        new Chart(
            document.getElementById('overallChart').getContext('2d'),
            {
                type: 'pie',
                data: {
                    labels,
                    datasets: [{
                        data: totalCosts,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.5)',
                            'rgba(54, 162, 235, 0.5)',
                            'rgba(255, 206, 86, 0.5)',
                            'rgba(75, 192, 192, 0.5)',
                            'rgba(153, 102, 255, 0.5)'
                        ]
                    }]
                },
                options: {
                    responsive: true,
                    plugins: { legend: { position: 'bottom' } }
                }
            }
        );
    </script>

</c:if>

<c:if test="${empty costSummaryMap}">
    <p style="text-align:center;">No cost analysis data available yet, please add some commute logs.</p>
</c:if>

</body>
</html>
