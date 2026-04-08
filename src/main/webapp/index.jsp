<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Commuter Cost Calculator</title>
    <link rel="stylesheet" href="style/main.css" type="text/css" />
</head>
<body>
<%@include file="Header.jsp" %>
<%@include file="taglib.jsp" %>

<main class="landing-page">

    <section class="hero-section">
        <div class="hero-icon-circle">
            <!--bootstrap icon-->
            <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="white" class="bi bi-map" viewBox="0 0 16 16">
                <path fill-rule="evenodd" d="M15.817.113A.5.5 0 0 1 16 .5v14a.5.5 0 0 1-.402.49l-5 1a.5.5 0 0 1-.196 0L5.5 15.01l-4.902.98A.5.5 0 0 1 0 15.5v-14a.5.5 0 0 1 .402-.49l5-1a.5.5 0 0 1 .196 0L10.5.99l4.902-.98a.5.5 0 0 1 .415.103M10 1.91l-4-.8v12.98l4 .8zm1 12.98 4-.8V1.11l-4 .8zm-6-.8V1.11l-4 .8v12.98z"/>
            </svg>
        </div>

        <h1 class="hero-title">Welcome to Commuter</h1>

        <p class="hero-subtitle">
            Your comprehensive solution for tracking and optimizing commuting costs.
            Make informed decisions about your daily travel expenses.
        </p>

        <div class="hero-actions">
            <a href="#calculator" class="btn btn-primary">Get Started</a>
            <c:choose>
                <c:when test="${empty sessionScope.userName}">
                    <a href="${pageContext.request.contextPath}/logIn" class="btn btn-outline">View Analytics</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/comparisonCost" class="btn btn-outline">View Analytics</a>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

    <section class="feature-cards">
        <div class="feature-card">
            <div class="feature-icon-box">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="#ff6b35" class="bi bi-bar-chart-steps" viewBox="0 0 16 16">
                    <path d="M.5 0a.5.5 0 0 1 .5.5v15a.5.5 0 0 1-1 0V.5A.5.5 0 0 1 .5 0M2 1.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-4a.5.5 0 0 1-.5-.5zm2 4a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-7a.5.5 0 0 1-.5-.5zm2 4a.5.5 0 0 1 .5-.5h6a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-6a.5.5 0 0 1-.5-.5zm2 4a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-7a.5.5 0 0 1-.5-.5z"/>
                </svg>
            </div>
            <h2>Track Commuting Costs</h2>
            <p>Log and monitor your daily commuting expenses with ease.</p>
            <a href="#calculator" class="feature-link">Learn More</a>
        </div>

        <div class="feature-card">
            <div class="feature-icon-box">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="#ff6b35" class="bi bi-truck-front" viewBox="0 0 16 16">
                    <path d="M5 11a1 1 0 1 1-2 0 1 1 0 0 1 2 0m8 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0m-6-1a1 1 0 1 0 0 2h2a1 1 0 1 0 0-2zM4 2a1 1 0 0 0-1 1v3.9c0 .625.562 1.092 1.17.994C5.075 7.747 6.792 7.5 8 7.5s2.925.247 3.83.394A1.008 1.008 0 0 0 13 6.9V3a1 1 0 0 0-1-1zm0 1h8v3.9q0 .002 0 0l-.002.004-.005.002h-.004C11.088 6.761 9.299 6.5 8 6.5s-3.088.26-3.99.406h-.003l-.005-.002L4 6.9q0 .002 0 0z"/>
                    <path d="M1 2.5A2.5 2.5 0 0 1 3.5 0h9A2.5 2.5 0 0 1 15 2.5v9c0 .818-.393 1.544-1 2v2a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5V14H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2a2.5 2.5 0 0 1-1-2zM3.5 1A1.5 1.5 0 0 0 2 2.5v9A1.5 1.5 0 0 0 3.5 13h9a1.5 1.5 0 0 0 1.5-1.5v-9A1.5 1.5 0 0 0 12.5 1z"/>
                </svg>
            </div>
            <h2>Manage Vehicles</h2>
            <p>Keep track of all your vehicles and their specifications.</p>
            <a href="${pageContext.request.contextPath}/${empty sessionScope.userName ? 'logIn' : 'saveVehicleProfile'}" class="feature-link">Learn More</a>
        </div>

        <div class="feature-card">
            <div class="feature-icon-box">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="#ff6b35" class="bi bi-bar-chart-line" viewBox="0 0 16 16">
                    <path d="M11 2a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v12h.5a.5.5 0 0 1 0 1H.5a.5.5 0 0 1 0-1H1v-3a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v3h1V7a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v7h1zm1 12h2V2h-2zm-3 0V7H7v7zm-5 0v-3H2v3z"/>
                </svg>
            </div>
            <h2>Compare Results</h2>
            <p>Analyze and compare costs across different time periods.</p>
            <a href="${pageContext.request.contextPath}/${empty sessionScope.userName ? 'logIn' : 'comparisonCost'}" class="feature-link">Learn More</a>
        </div>
    </section>

    <section id="calculator" class="calculator-section">
        <div class="calculator-card">
            <h2 class="section-title">Quick Commute Calculator</h2>
            <p class="section-subtitle">
                Get an estimated weekly transportation cost below.
            </p>

            <form id="commuteForm" class="calculator-form">
                <div class="form-group">
                    <label for="miles">Enter your commute distance (miles)</label>
                    <input type="number" id="miles" name="miles" required>
                </div>

                <div class="form-group">
                    <label for="daysPerWeek">How many days per week do you commute to work?</label>
                    <input type="number" id="daysPerWeek" name="daysPerWeek" required>
                </div>

                <div class="form-group">
                    <label>Choose your mode of transportation</label>
                    <div class="transport-buttons">
                        <button type="button" onclick="selectTransport('car')" class="transport-btn">Car</button>
                        <button type="button" onclick="selectTransport('bus')" class="transport-btn">Bus</button>
                        <button type="button" onclick="selectTransport('bike')" class="transport-btn">Bike</button>
                        <button type="button" onclick="selectTransport('walk')" class="transport-btn">Walk</button>
                    </div>
                </div>

                <input type="hidden" id="transportation" name="transportation" />

                <div class="calculator-actions">
                    <button type="button" class="btn btn-primary" onclick="calculateCommuteCost()">Calculate</button>
                </div>
            </form>

            <div id="result" class="result-box"></div>
        </div>
    </section>

    <section class="value-section">
        <div class="value-item">
            <h2>Save Money</h2>
            <p>Optimize your commuting expenses</p>
        </div>

        <div class="value-item">
            <h2>Track Progress</h2>
            <p>Monitor trends over time</p>
        </div>

        <div class="value-item">
            <h2>Make Decisions</h2>
            <p>Data-driven insights</p>
        </div>
    </section>

</main>

<script>
    function calculateCommuteCost() {
        var miles = document.getElementById("miles").value;
        var daysPerWeek = document.getElementById("daysPerWeek").value;
        var transportation = document.getElementById("transportation").value;
        var cost = 0;
        var resultText = '';

        if (!miles || miles <= 0 || !daysPerWeek || daysPerWeek <= 0) {
            resultText = 'Please enter valid commute distance and days per week.';
        } else {
            var roundTripDistance = miles * 2;
            var totalDistancePerWeek = roundTripDistance * daysPerWeek;

            switch (transportation) {
                case "car":
                    cost = totalDistancePerWeek * 0.58;
                    resultText = 'Your estimated weekly cost for driving is: $' + cost.toFixed(2);
                    break;
                case "bus":
                    cost = 2.00 * 1 * daysPerWeek;
                    resultText = 'The Madison bus cost about $2.00 a trip. Your estimated weekly cost for taking the bus is: $' + cost.toFixed(2);
                    break;
                case "bike":
                    cost = 0.10 * 2 * daysPerWeek;
                    resultText = 'Your estimated weekly cost for biking is: $' + cost.toFixed(2);
                    break;
                case "walk":
                    cost = 0.02 * 2 * daysPerWeek;
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

        const buttons = document.querySelectorAll(".transport-btn");
        buttons.forEach(btn => btn.classList.remove("active"));

        const selected = [...buttons].find(btn => btn.textContent.toLowerCase().includes(value));
        if (selected) selected.classList.add("active");
    }
</script>
</body>
<%@include file="Footer.jsp" %>

</html>