<%--
  Created by IntelliJ IDEA.
  User: micahdarling
  Date: 5/1/25
  Time: 4:32 PM
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@include file="taglib.jsp" %>
<%@include file="Header.jsp" %>


<html>
<head>
  <title>My Vehicle Profile</title>
  <link rel="stylesheet" href="style/main.css" type="text/css" />
</head>
<body>

<h2>My Vehicle Profile</h2>

<c:if test="${not empty vehicleWarning}">
  <p style="color: red;">${vehicleWarning}</p>
</c:if>

<c:if test="${not empty successMessage}">
  <p style="color: green;">${successMessage}</p>
</c:if>

<form action="saveVehicleProfile" method="post">

  <label for="vehicleType">Vehicle Type:</label>
  <input type="text" id="vehicleType" name="vehicleType"
         value="${vehicleProfile.vehicleType}" required><br><br>

  <label for="milesPerGallon">Miles Per Gallon:</label>
  <input type="number" step="0.1" id="milesPerGallon" name="milesPerGallon"
         value="${vehicleProfile.milesPerGallon}" required><br><br>

  <label for="monthlyPayment">Monthly Payment ($):</label>
  <input type="number" step="0.01" id="monthlyPayment" name="monthlyPayment"
         value="${vehicleProfile.monthlyPayment}" required><br><br>

  <label for="insuranceCost">Monthly Insurance Cost ($):</label>
  <input type="number" step="0.01" id="insuranceCost" name="insuranceCost"
         value="${vehicleProfile.insuranceCost}" required><br><br>

  <label for="maintenanceCost">Monthly Maintenance Cost ($):</label>
  <input type="number" step="0.01" id="maintenanceCost" name="maintenanceCost"
         value="${vehicleProfile.maintenanceCost}" required><br><br>

  <button type="submit">Save Vehicle Profile</button>
</form>

<c:if test="${not empty vehicleProfile.lastUpdated}">
  <p>Last updated:
    <fmt:formatDate value="${vehicleProfile.lastUpdated}" pattern="MMMM d, yyyy 'at' h:mm a" />
  </p>
</c:if>

</body>
</html>
