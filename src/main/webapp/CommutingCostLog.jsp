<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="taglib.jsp" %>
<%@include file="Header.jsp" %>
<html>
<head>
    <title>Commuting Cost Log</title>
    <link rel="stylesheet" href="style/main.css" type="text/css" />
</head>
<body>
<!--TODO css styling of page center all items -->
<!--TODO delete cost section of form-->

<h2>Your Commuting Logs</h2>
<c:if test="${not empty commutingLogs}">
    <table border="1">
        <thead>
        <tr>
            <th>Actions</th>
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
                <td>
                    <form action="deleteCommutingLog" method="post" style="display:inline;">
                        <input type="hidden" name="logId" value="${log.id}" />
                        <button type="submit">Delete</button>
                    </form>
                    <form action="CommutingCostLog.jsp" method="get" style="display:inline;">
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

<h3>
    <c:choose>
        <c:when test="${not empty param.editLogId}">Edit Commute Log</c:when>
        <c:otherwise>Add a New Commute Log</c:otherwise>
    </c:choose>
</h3>

<form action="${not empty param.editLogId ? 'updateCommutingLog' : 'addCommutingLog'}" method="post">
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
    <br><br>

    <label for="timeSpent">Time Spent (minutes):</label>
    <input type="number" id="timeSpent" name="timeSpent" value="${editLog.timeSpent}" required><br><br>

    <label for="distanceInMiles">Distance (miles):</label>
    <input type="number" id="distanceInMiles" name="distanceInMiles" value="${editLog.distanceInMiles}" required><br><br>

    <label for="cost">Cost:</label>
    <input type="number" id="cost" name="cost" value="${editLog.cost}" required><br><br>

    <button type="submit">
        <c:choose>
            <c:when test="${not empty param.editLogId}">Update Log</c:when>
            <c:otherwise>Add Log</c:otherwise>
        </c:choose>
    </button>
</form>

<c:if test="${not empty costSummary}">
    <h3>Most Recent Cost Summary</h3>
    <table border="1">
        <thead>
        <tr>
            <th>Commute Type</th>
            <th>1-Year Cost</th>
            <th>2-Year Cost</th>
            <th>5-Year Cost</th>
            <th>Total Cost</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${costSummary.commuteType}</td>
            <td>$${costSummary.oneYearCost}</td>
            <td>$${costSummary.twoYearCost}</td>
            <td>$${costSummary.fiveYearCost}</td>
            <td><strong>$${costSummary.totalCost}</strong></td>
        </tr>
        </tbody>
    </table>
</c:if>
<c:if test="${empty costSummary}">
    <p>No cost summary available yet. Add a commuting log to generate one.</p>
</c:if>

<p>Debug: ${costSummary}</p>

</body>
</html>
