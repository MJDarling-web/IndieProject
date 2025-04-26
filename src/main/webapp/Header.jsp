<%--
  Created by IntelliJ IDEA.
  User: micahdarling
  Date: 3/4/25
  Time: 8:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="taglib.jsp" %>

<header>
    <nav>
        <ul style="list-style-type: none; padding: 0;">
            <li style="display: inline; margin-right: 20px;">
                <a href="viewUsers">Profile</a>
            </li>
            <li style="display: inline; margin-right: 20px;">
                <a href="CommutingCostLog">Commute Cost Log</a>
            </li>

            <!-- shows up if user is logged in -->
            <c:if test="${not empty sessionScope.userName}">
                <li style="display: inline; margin-right: 20px">
                    <a href="signout">Sign Out</a>
                </li>
                <li style="display: inline; margin-right: 20px;">
                    <a href="TransportationCostForm">My Vehicles</a>
                </li>
                <li style="display: inline; margin-right: 20px;">
                    <a href="comparisonCost">Comparison Results</a>
                </li>
            </c:if>
        </ul>
    </nav>
</header>