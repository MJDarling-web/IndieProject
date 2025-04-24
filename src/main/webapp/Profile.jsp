<%--
  Created by IntelliJ IDEA.
  User: micahdarling
  Date: 4/7/25
  Time: 9:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@include file="Header.jsp" %>
<%@include file="taglib.jsp" %>
<html>
<head>
  <title>User Profile</title>
  <link rel="stylesheet" href="style/main.css" type="text/css" />
</head>
<body>
<h1>Hello, ${sessionScope.user.userName}!</h1>
<p>Welcome to your profile page.</p>
</body>
</html>