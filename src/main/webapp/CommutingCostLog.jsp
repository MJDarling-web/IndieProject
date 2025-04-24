<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@include file="taglib.jsp" %>
<%@include file="Header.jsp" %>
<html>
<head>
    <title>Commuting Cost Log</title>
    <link rel="stylesheet" href="style/main.css" type="text/css" />
</head>
<body>

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

<h3><c:choose>
    <c:when test="${not empty param.editLogId}">
        Edit Commute Log
    </c:when>
    <c:otherwise>
        Add a New Commute Log
    </c:otherwise>
</c:choose></h3>

<%
    String editLogId = request.getParameter("editLogId");
    entity.CommutingLog editLog = null;

    try {
        if (editLogId != null) {
            java.util.List<entity.CommutingLog> logs = (java.util.List<entity.CommutingLog>) request.getAttribute("commutingLogs");

            if (logs != null) {
                for (entity.CommutingLog log : logs) {
                    if (log.getId() == Integer.parseInt(editLogId)) {
                        editLog = log;
                        break;
                    }
                }
            }
        }
    } catch (Exception e) {
        // Optional: log the error or silently fail
        System.out.println("Error finding edit log: " + e.getMessage());
    }
%>


<form action='<%= (editLog != null) ? "updateCommutingLog" : "addCommutingLog" %>' method="post">
    <c:if test="${not empty param.editLogId}">
        <input type="hidden" name="logId" value="${param.editLogId}" />
    </c:if>

    <label for="commuteType">Commute Type:</label>
    <input type="text" id="commuteType" name="commuteType"
           value="<%= (editLog != null) ? editLog.getCommuteType() : "" %>" required><br><br>

    <label for="timeSpent">Time Spent (minutes):</label>
    <input type="number" id="timeSpent" name="timeSpent"
           value="<%= (editLog != null) ? editLog.getTimeSpent() : "" %>" required><br><br>

    <label for="distanceInMiles">Distance (miles):</label>
    <input type="number" id="distanceInMiles" name="distanceInMiles"
           value="<%= (editLog != null) ? editLog.getDistanceInMiles() : ""%>" required><br><br>

    <label for="cost">Cost:</label>
    <input type="number" id="cost" name="cost"
           value="<%= (editLog != null) ? editLog.getCost() : ""%>" required><br><br>

    <button type="submit"><%= (editLog != null) ? "Update Log" : "Add Log" %></button>
</form>

</body>
</html>
