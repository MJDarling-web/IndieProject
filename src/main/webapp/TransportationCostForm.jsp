<%--
  Created by IntelliJ IDEA.
  User: micahdarling
  Date: 4/24/25
  Time: 8:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Transportation Profile</title>
</head>
<body>
  <h2>Enter Your Transportation Costs</h2>
  <form action="saveVehicleProfile" method="post">
    Vehicle Type:
    <select name="vehicleType">
      <option value="car">Car</option>
      <option value="bike">Bike</option>
      <option value="walk">Walk</option>
      <option value="bus">Bus</option>
    </select><br/><br/>
    Monthly Insurance Cost: <input type="number" step="0.01" name="insuranceCost"><br/>
    Monthly Fuel Cost (per gallon): <input type="number" step="0.01" name="fuelCost"><br/>
    Maintenance Cost: <input type="number" step="0.01" name="maintenanceCost"><br/>
    Miles Per Gallon: <input type="number" step="0.1" name="milesPerGallon"><br/>
    Public Transportation Cost: <input type="number" step="0.01" name="publicTransportCost"><br/>
    <input type="submit" value="Save">
  </form>

</body>
</html>
