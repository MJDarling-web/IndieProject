<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="taglib.jsp" %>
<%@include file="Header.jsp" %>

<html>
<head>
    <title>Comparison Results</title>
    <link rel="stylesheet" href="style/main.css" type="text/css" />
</head>
<body>
<p>DEBUG: Cost Summary Map = ${costSummaryMap}</p>

<h2>Your Commute Cost Comparison</h2>

<c:if test="${not empty costSummaryMap}">
    <table border="1" style="width:80%; text-align: center;">
        <thead>
        <tr>
            <th>Commute Type</th>
            <th>1 year of ownership</th>
            <th>2 years of ownership</th>
            <th>5 years of ownership</th>
            <th>Total Cost So Far</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="entry" items="${costSummaryMap}">
            <tr>
                <td>${entry.key}</td>
                <td>$${entry.value.oneYearCost}</td>
                <td>$${entry.value.twoYearCost}</td>
                <td>$${entry.value.fiveYearCost}</td>
                <td>$${entry.value.totalCost}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<c:if test="${empty costSummaryMap}">
    <p>No cost analysis data available yet, please add some commute logs.</p>
</c:if>

</body>
</html>
