<%--
  Created by IntelliJ IDEA.
  User: micahdarling
  Date: 3/4/25
  Time: 8:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All Users will be login page</title>
</head>
<body>
<%@ include file="Header.jsp" %>

<h2>All Users</h2>

<!-- Display all users -->
<c:if test="${not empty users}">
    <table border="1">
        <thead>
        <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.email}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<!-- If no users, display this message -->
<c:if test="${empty users}">
    <p>No users found.</p>
</c:if>

</body>
</html>
