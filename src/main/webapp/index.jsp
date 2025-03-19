<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Commuter Cost Calculator</title>
    <style>
        body {
            text-align: center;
            font-family: Arial, sans-serif;
            margin: 30px;
        }
        label {
            display: block;
            margin-top: 10px;
        }
        input, select {
            margin-top: 5px;
            padding: 5px;
            font-size: 16px;
        }
        #result {
            margin-top: 20px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<%@include file="Header.jsp" %>
<h2>Welcome to Commuter</h2>
<h3>A commuting cost calcultor and comparison tracker for your true cost of transportation needs</h3>

<form id="commuteForm">
    <label for="miles">Enter your commute distance (miles):</label>
    <input type="number" id="miles" name="miles" required>

    <label for="daysPerWeek">How many days per week do you commute to work?</label>
    <input type="number" id="daysPerWeek" name="daysPerWeek" required>

    <label for="transportation">Choose your mode of transportation:</label>
    <select id="transportation" name="transportation">
        <option value="car">Car</option>
        <option value="bus">Bus</option>
        <option value="bike">Bike</option>
        <option value="walk">Walk</option>
    </select>

    <button type="button" onclick="calculateCommuteCost()">Calculate</button>
</form>

<div id="result"></div>

<script>
    function calculateCommuteCost() {
        var miles = document.getElementById("miles").value;
        var daysPerWeek = document.getElementById("daysPerWeek").value;
        var transportation = document.getElementById("transportation").value;
        var cost = 0;
        var resultText = '';

        // Check if miles and days per week are valid
        if (!miles || miles <= 0 || !daysPerWeek || daysPerWeek <= 0) {
            resultText = 'Please enter valid commute distance and days per week.';
        } else {
            // Calculate round trip distance (commute to and from work)
            var roundTripDistance = miles * 2;
            // Calculate total distance per week (round trip distance * days per week)
            var totalDistancePerWeek = roundTripDistance * daysPerWeek;

            switch (transportation) {
                case "car":
                    // Assume average cost of driving per mile, e.g., $0.58 per mile (this can vary based on fuel, maintenance, etc.)
                    cost = totalDistancePerWeek * 0.58;
                    resultText = 'Your estimated weekly cost for driving is: $' + cost.toFixed(2);
                    break;
                case "bus":
                    // Assume a bus ticket costs $2.50 per ride, and a person commutes for the specified days per week.
                    cost = 2.50 * 2 * daysPerWeek;  // Round trip each day
                    resultText = 'Your estimated weekly cost for taking the bus is: $' + cost.toFixed(2);
                    break;
                case "bike":
                    // Biking is free, but might need a one-time cost for equipment like a bike.
                    cost = .10 * 2 * daysPerWeek;  // We assume no weekly costs unless you want to account for maintenance.
                    resultText = 'Your estimated weekly cost for biking is: $' + cost.toFixed(2);
                    break;
                case "walk":
                    // Walking is free.
                    cost = .02 * 2 * daysPerWeek;
                    resultText = 'Your estimated weekly cost for walking is: $' + cost.toFixed(2);
                    break;
                default:
                    resultText = 'Please select a valid mode of transportation.';
            }
        }

        document.getElementById("result").innerText = resultText;
    }
</script>
</body>
</html>
