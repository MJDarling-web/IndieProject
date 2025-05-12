package controller;

import entity.CommutingLog;
import entity.CostAnalysis;
import entity.TransportationProfile;
import entity.User;
import persistence.GenericDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@WebServlet("/comparisonCost")
public class ComparisonCostServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Delegate POST to GET so that all logic lives in one place
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) Session & user lookup
        HttpSession session = req.getSession();
        String userEmail = (String) session.getAttribute("userName");
        if (userEmail == null) {
            resp.sendRedirect("logIn.jsp");
            return;
        }

        GenericDao<User> userDao = new GenericDao<>(User.class);
        User user = userDao.getByPropertyLike("email", userEmail).stream().findFirst().orElse(null);
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User not found");
            return;
        }

        // 2) Load latest CostAnalysis per commuteType
        GenericDao<CostAnalysis> costDao = new GenericDao<>(CostAnalysis.class);
        String hql = "from CostAnalysis where user.id = " + user.getId() + " order by analysisId desc";
        List<CostAnalysis> analyses = costDao.getByCustomQuery(hql);
        Map<String, CostAnalysis> costSummaryMap = new HashMap<>();
        for (CostAnalysis ca : analyses) {
            costSummaryMap.putIfAbsent(ca.getCommuteType(), ca);
        }

        // 3) Load the user's vehicle profile
        GenericDao<TransportationProfile> profileDao = new GenericDao<>(TransportationProfile.class);
        List<TransportationProfile> profiles = profileDao.getByPropertyEqual("user", user);
        if (profiles.isEmpty()) {
            throw new ServletException("No vehicle profile found for user " + user.getId());
        }
        TransportationProfile profile = profiles.get(0);

        // 4) Load all commute logs
        GenericDao<CommutingLog> logDao = new GenericDao<>(CommutingLog.class);
        List<CommutingLog> logs = logDao.getByPropertyEqual("user", user);

        // 5) Compute “Overall” totals & projections
        double totalCommuteCost = logs.stream()
                .mapToDouble(CommutingLog::getCost)
                .sum();
        double lastMonthFixed = profile.getMaintenanceCost()
                + profile.getInsuranceCost()
                + profile.getMonthlyPayment();
        double totalSoFar = totalCommuteCost + lastMonthFixed;

        LocalDate firstLogDate = logs.stream()
                .map(CommutingLog::getDate)
                .map(d -> d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .min(Comparator.naturalOrder())
                .orElse(LocalDate.now());
        long monthsSoFar = ChronoUnit.MONTHS.between(firstLogDate, LocalDate.now());
        monthsSoFar = Math.max(monthsSoFar, 1);

        double avgCommutePerMonth = totalCommuteCost / monthsSoFar;
        double monthlyTotal = avgCommutePerMonth
                + profile.getMaintenanceCost()
                + profile.getInsuranceCost()
                + profile.getMonthlyPayment();

        // CostAnalysis for Car commute type
        Map<String, List<CommutingLog>> logsByType = logs.stream().collect(Collectors.groupingBy(CommutingLog::getCommuteType));

        List<CommutingLog> carLogs = logsByType.getOrDefault("Car", new ArrayList<>());

        if (!carLogs.isEmpty()) {
            double carGasCost = carLogs.stream().mapToDouble(CommutingLog::getCost).sum();

            LocalDate firstCarLogDate = carLogs.stream()
                    .map(CommutingLog::getDate)
                    .map(d -> d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .min(Comparator.naturalOrder())
                    .orElse(LocalDate.now());
            long carMonths = ChronoUnit.MONTHS.between(firstCarLogDate, LocalDate.now());
            carMonths = Math.max(carMonths, 1);

            double carAvgGasPerMonth = carGasCost / carMonths;
            double carMonthlyTotal = carAvgGasPerMonth
                    + profile.getMaintenanceCost()
                    + profile.getInsuranceCost()
                    + profile.getMonthlyPayment();

            CostAnalysis carAnalysis = new CostAnalysis();
            carAnalysis.setCommuteType("Car");
            carAnalysis.setTotalCost(carGasCost + profile.getMaintenanceCost()
                    + profile.getInsuranceCost()
                    + profile.getMonthlyPayment());
            carAnalysis.setOneYearCost(carMonthlyTotal * 12);
            carAnalysis.setTwoYearCost(carMonthlyTotal * 24);
            carAnalysis.setFiveYearCost(carMonthlyTotal * 60);
            costSummaryMap.put("Car", carAnalysis);
        }


        // 6) Build breakdown maps (time, gas, maintenance, insurance, payment)
        logsByType = logs.stream()
                .collect(Collectors.groupingBy(CommutingLog::getCommuteType));

        Map<String, Double> timeSpentMap = new HashMap<>();
        Map<String, Double> gasCostMap = new HashMap<>();
        Map<String, Double> maintenanceMap = new HashMap<>();
        Map<String, Double> insuranceMap = new HashMap<>();
        Map<String, Double> paymentMap = new HashMap<>();

        for (Map.Entry<String, List<CommutingLog>> e : logsByType.entrySet()) {
            String type = e.getKey();
            List<CommutingLog> list = e.getValue();

            double time = list.stream().mapToDouble(CommutingLog::getTimeSpent).sum();
            double gas = list.stream().mapToDouble(CommutingLog::getCost).sum();
            boolean isCar = "Car".equalsIgnoreCase(type);
            timeSpentMap.put(type, time);
            gasCostMap.put(type, gas);
            maintenanceMap.put(type, isCar ? profile.getMaintenanceCost() : 0.0);
            insuranceMap.put(type, isCar ? profile.getInsuranceCost() : 0.0);
            paymentMap.put(type, isCar ? profile.getMonthlyPayment() : 0.0);
        }

        // 7) Set everything on the request
        req.setAttribute("costSummaryMap", costSummaryMap);
        req.setAttribute("timeSpentMap", timeSpentMap);
        req.setAttribute("gasCostMap", gasCostMap);
        req.setAttribute("maintenanceCostMap", maintenanceMap);
        req.setAttribute("insuranceCostMap", insuranceMap);
        req.setAttribute("paymentCostMap", paymentMap);

        // 8) Forward once to the JSP
        RequestDispatcher dispatcher = req.getRequestDispatcher("ComparisonCost.jsp");
        dispatcher.forward(req, resp);
    }
}
