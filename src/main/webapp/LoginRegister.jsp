<%--
  Created by IntelliJ IDEA.
  User: micahdarling
  Date: 3/4/25
  Time: 8:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All Users will be login page</title>
</head>
<body>
<%@ include file="taglib.jsp"%>
<%@ include file="Header.jsp" %>

<h2>All Users</h2>
<c:out value="Users from request: ${users}" />
<c:forEach var="user" items="${users}">
    <p>${user.firstName} ${user.lastName} (${user.email})</p>
</c:forEach>

</body>
</html>
