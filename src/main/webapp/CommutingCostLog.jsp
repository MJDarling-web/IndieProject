<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@include file="taglib.jsp" %>
<html>
<head>
    <title>Commuting Cost Log</title>
</head>
<body>
<%@include file="taglib.jsp" %>
<%@include file="Header.jsp" %>

<h2>Your Commuting Logs</h2>

<!-- Display Debug Logs -->
<h3>Debug Log:</h3>
<c:if test="${not empty logMessages}">
    <ul>
        <c:forEach var="log" items="${logMessages}">
            <li>${log}</li>
        </c:forEach>
    </ul>
</c:if>

<!-- Display Commuting Logs -->
<c:if test="${not empty commutingLogs}">
    <table border="1">
        <thead>
        <tr>
            <th>Date</th>
            <th>Commute Type</th>
            <th>Time Spent (minutes)</th>
            <th>Distance (miles)</th>
            <th>Cost</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="log" items="${commutingLogs}">
            <tr>
                <td>${log.date}</td>
                <td>${log.commuteType}</td>
                <td>${log.timeSpent}</td>
                <td>${log.distanceInMiles}</td>
                <td>${log.cost}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<c:if test="${empty commutingLogs}">
    <p>No commuting logs found.</p>
</c:if>


<h3>Add a New Commute Log</h3>
<form action="addCommutingLog" method="post">
    <label for="commuteType">Commute Type:</label>
    <input type="text" id="commuteType" name="commuteType" required><br><br>

    <label for="timeSpent">Time Spent (minutes):</label>
    <input type="number" id="timeSpent" name="timeSpent" required><br><br>

    <label for="distanceInMiles">Distance (miles):</label>
    <input type="number" id="distanceInMiles" name="distanceInMiles" required><br><br>

    <label for="cost">Cost:</label>
    <input type="number" id="cost" name="cost" required><br><br>

    <button type="submit">Add Log</button>
</form>

</body>
</html>
