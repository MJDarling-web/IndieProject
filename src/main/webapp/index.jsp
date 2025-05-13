<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Commuter Cost Calculator</title>
    <link rel="stylesheet" href="style/main.css" type="text/css" />

</head>
<body>
<!--TODO css styling of page center all items -->
<!-- TODO adjust cost analysis calculator ot factor in additional cost of users vehicle -->
<%@include file="Header.jsp" %>
<%@include file="taglib.jsp" %>
<br><br>
<h1>Welcome to Commuter</h1>
<br>
<p>A commuting cost calculator and tracker to help you understand your transportation needs</p>

<form id="commuteForm">
    <label for="miles">Enter your commute distance (miles):</label>
    <input type="number" id="miles" name="miles" required>

    <label for="daysPerWeek">How many days per week do you commute to work?</label>
    <input type="number" id="daysPerWeek" name="daysPerWeek" required>
<!-- TODO use font awesome or another icon library to swap words for icons of transport options -->
    <label>Choose your mode of transportation:</label>
    <div class="transport-buttons">
        <button type="button" onclick="selectTransport('car')" class="transport-btn">Car</button>
        <button type="button" onclick="selectTransport('bus')" class="transport-btn">Bus</button>
        <button type="button" onclick="selectTransport('bike')" class="transport-btn">Bike</button>
        <button type="button" onclick="selectTransport('walk')" class="transport-btn">Walk</button>
    </div>
    <!-- Hidden input to store selected value -->
    <input type="hidden" id="transportation" name="transportation" />

    <br><br>
    <button type="button" class="index-button" onclick="calculateCommuteCost()">Calculate</button>
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
                    cost = 2.00 * 1 * daysPerWeek;  // Round trip each day
                    resultText = 'The Madison bus cost about $2.00 a trip, your estimated weekly cost for taking the bus is: $' + cost.toFixed(2);
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
    function selectTransport(value) {
        document.getElementById("transportation").value = value;

        // Highlight the selected button
        const buttons = document.querySelectorAll(".transport-btn");
        buttons.forEach(btn => btn.classList.remove("active"));
        const selected = [...buttons].find(btn => btn.textContent.toLowerCase().includes(value));
        if (selected) selected.classList.add("active");
    }

</script>
</body>
</html>
