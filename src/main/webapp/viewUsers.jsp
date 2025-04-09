<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@include file="taglib.jsp" %>

<!DOCTYPE html>
<html lang="en">
head>
<title>Login</title>
<link rel="stylesheet" href="style/main.css" type="text/css" />

</head>
<body>
<c:choose>
    <c:when test="${empty userName}">
        <a href = "logIn">Log in</a>
    </c:when>
    <c:otherwise>
        <h3>Welcome ${userName}</h3>
    </c:otherwise>
</c:choose>
</body>
</html>
