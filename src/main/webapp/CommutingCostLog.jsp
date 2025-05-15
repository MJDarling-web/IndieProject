<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="taglib.jsp" %>
<%@include file="Header.jsp" %>
<html>
<head>
    <title>Commuting Cost Log</title>
    <link rel="stylesheet" href="style/main.css" type="text/css" />
</head>
<body>
<!--TODO current price of fuel from fuelEconomy.gov api -->
<h2>Your Commuting Logs</h2>

<c:if test="${not empty commutingLogs}">
    <table border="1" class="centered-table">
        <thead>
        <tr>
            <th>Actions</th>
            <th>Date</th>
            <th>Commute Type</th>
            <th>Time Spent (minutes)</th>
            <th>Distance (miles)</th>
            <th>Cost in gas</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="log" items="${commutingLogs}">
            <tr>
                <td>
                    <form action="deleteCommutingLog" method="post" style="display:inline">
                        <input type="hidden" name="logId" value="${log.id}" />
                        <button type="submit">Delete</button>
                    </form>
                    <form action="addCommutingLog" method="get" style="display:inline">
                        <input type="hidden" name="editLogId" value="${log.id}" />
                        <button type="submit">Edit</button>
                    </form>
                </td>
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

<hr/>

<h3>
    <c:choose>
        <c:when test="${not empty param.editLogId}">Edit Commute Log</c:when>
        <c:otherwise>Add a New Commute Log</c:otherwise>
    </c:choose>
</h3>

<form action="${not empty param.editLogId ? 'updateCommutingLog' : 'addCommutingLog'}"
      method="post">
    <c:if test="${not empty param.editLogId}">
        <input type="hidden" name="logId" value="${param.editLogId}" />
    </c:if>

    <label for="commuteType">Commute Type:</label>
    <select id="commuteType" name="commuteType" required>
        <c:forEach var="type" items="${commuteTypes}">
            <option value="${type}"
                    <c:if test="${not empty editLog and editLog.commuteType eq type}">selected</c:if>>
                    ${type}
            </option>
        </c:forEach>
    </select>
    <br/><br/>

    <label for="timeSpent">Time Spent (minutes):</label>
    <input type="number" id="timeSpent" name="timeSpent"
           value="${editLog.timeSpent}" step="0.1" required />
    <br/><br/>

    <label for="distanceInMiles">Distance (miles):</label>
    <input type="number" id="distanceInMiles" name="distanceInMiles"
           value="${editLog.distanceInMiles}" step="0.1" required />
    <br/><br/>

    <!-- cost is auto-calculated in the servlet -->

    <button type="submit">
        <c:choose>
            <c:when test="${not empty param.editLogId}">Update Log</c:when>
            <c:otherwise>Add Log</c:otherwise>
        </c:choose>
    </button>
</form>

</body>
</html>
