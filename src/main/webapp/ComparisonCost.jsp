<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="taglib.jsp" %>
<%@include file="Header.jsp" %>

<html>
<head>
    <title>Comparison Results</title>
    <link rel="stylesheet" href="style/main.css" type="text/css" />

</head>
<body>
<!--TODO comparison page should pull user data, compile, calculate, and display data in an easy to view format -->
<h2>Your Commute Cost Comparison</h2>

<c:if test="{not empty costSummaryMap}">
    <table border="1" style = "width:80%; text-align: center;">
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
            <td>${entity.value.oneYearCost}</td>
            <td>${entity.value.twoYearCost}</td>
            <td>${entity.value.fiveYearCost}</td>
            <td>${entity.value.totalCost}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<c: if test="{not empty costSummaryMap}">
    <p>No cost analysis data available yet, please add some commute logs </p>
</c:>
</body>
</html>
