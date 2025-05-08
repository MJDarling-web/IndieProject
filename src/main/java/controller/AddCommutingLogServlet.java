package controller;

import entity.*;
import persistence.FuelApiDao;
import persistence.GenericDao;
import persistence.UserDao;
import util.CommutingCostCalculator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/addCommutingLog")
public class AddCommutingLogServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int userId = getLoggedInUserId(req);

        // Load the user
        User user = new UserDao().getById(userId);

        // Grab form inputs (no more "cost" field)
        String commuteType      = req.getParameter("commuteType");
        double timeSpent        = Double.parseDouble(req.getParameter("timeSpent"));
        double distanceInMiles  = Double.parseDouble(req.getParameter("distanceInMiles"));

        // Fetch the user's vehicle MPG
        GenericDao<TransportationProfile> profDao =
                new GenericDao<>(TransportationProfile.class);
        List<TransportationProfile> profiles = profDao.getByCustomQuery(
                "from TransportationProfile where user.id = " + userId
        );
        double mpg = profiles.stream()
                .filter(p -> p.getVehicleType().equalsIgnoreCase(commuteType))
                .map(TransportationProfile::getMilesPerGallon)
                .findFirst()
                .orElse(25.0);

        //Compute gas cost via our small service
        double cost;
        String typeLower = commuteType.toLowerCase();
        if (typeLower.equals("walk") || typeLower.equals("bike")) {
            cost = 0.0;
        } else if (typeLower.equals("bus")) {
            cost = 2.00;
        } else {
            CommutingCostCalculator calculator =
                    new CommutingCostCalculator(new FuelApiDao());
            try {
                cost = calculator.computeGasCost(distanceInMiles, mpg);
            } catch (Exception e) {
                throw new ServletException("Failed to fetch fuel price", e);
            }
        }

        // Build and persist the CommutingLog
        CommutingLog log = new CommutingLog();
        log.setUser(user);
        log.setCommuteType(commuteType);
        log.setTimeSpent(timeSpent);
        log.setDistanceInMiles(distanceInMiles);
        log.setCost(cost);
        log.setDate(new Date());
        new GenericDao<>(CommutingLog.class).insert(log);

        // 6) Reload all logs for display
        List<CommutingLog> userLogs = new GenericDao<>(CommutingLog.class)
                .getByCustomQuery("from CommutingLog where user.id=" + userId);
        req.setAttribute("commutingLogs", userLogs);

        // Build commute‑type dropdown
        List<String> commuteTypes = new ArrayList<>(Arrays.asList("walk","bike","bus"));
        for (TransportationProfile p : profiles) {
            if (!commuteTypes.contains(p.getVehicleType())) {
                commuteTypes.add(p.getVehicleType());
            }
        }
        if (profiles.isEmpty() && !commuteTypes.contains("car")) {
            commuteTypes.add("car");
            req.setAttribute("vehicleWarning",
                    "No vehicles found—default 'car' added. Please add a vehicle profile.");
        }
        req.setAttribute("commuteTypes", commuteTypes);

        // Recompute and persist CostAnalysis for each type
        GenericDao<CostAnalysis> costDao = new GenericDao<>(CostAnalysis.class);
        Map<String, CostAnalysis> costSummary = new HashMap<>();
        for (String type : commuteTypes) {
            double totalMiles = userLogs.stream()
                    .filter(c -> c.getCommuteType()
                            .equalsIgnoreCase(type))
                    .mapToDouble(CommutingLog::getDistanceInMiles)
                    .sum();

            // find matching vehicle for insurance/maintenance/fuel
            TransportationProfile vehicle = profiles.stream()
                    .filter(p -> p.getVehicleType()
                            .equalsIgnoreCase(type))
                    .findFirst().orElse(null);

            double insurance = vehicle != null ? vehicle.getInsuranceCost() : 0;
            double maintenance = vehicle != null ? vehicle.getMaintenanceCost() : 0;
            double perGallon = vehicle != null ? vehicle.getFuelCostPerGallon() : 3.5;
            double mpgForType = vehicle != null && vehicle.getMilesPerGallon()>0
                    ? vehicle.getMilesPerGallon() : 25;

            double typeGasCost = (totalMiles / mpgForType) * perGallon;
            double oneYear = insurance + maintenance + typeGasCost;
            double twoYear = oneYear * 2;
            double fiveYear = oneYear * 5;

            // load or new
            String hql = "from CostAnalysis where user.id=" + userId +
                    " and commuteType='" + type +
                    "' order by analysisId desc";
            List<CostAnalysis> existing = costDao.getByCustomQuery(hql);

            CostAnalysis ca = existing.isEmpty()
                    ? new CostAnalysis()
                    : existing.get(0);

            if (existing.isEmpty()) {
                ca.setUser(user);
                ca.setCommuteType(type);
            }
            ca.setOneYearCost(oneYear);
            ca.setTwoYearCost(twoYear);
            ca.setFiveYearCost(fiveYear);
            ca.setTotalCost(oneYear + twoYear + fiveYear);

            if (ca.getAnalysisId() > 0) {
                costDao.update(ca);
            } else {
                costDao.insert(ca);
            }
            costSummary.put(type, ca);
        }
        req.setAttribute("costSummaryMap", costSummary);

        // 9) Forward to JSP
        RequestDispatcher rd = req.getRequestDispatcher("CommutingCostLog.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // simply load logs, commute types, and cost summary for display
        int userId = getLoggedInUserId(req);
        User user = new UserDao().getById(userId);

        List<CommutingLog> userLogs = new GenericDao<>(CommutingLog.class)
                .getByCustomQuery("from CommutingLog where user.id=" + userId);
        req.setAttribute("commutingLogs", userLogs);

        List<TransportationProfile> profiles = new GenericDao<>(TransportationProfile.class)
                .getByCustomQuery("from TransportationProfile where user.id=" + userId);
        List<String> commuteTypes = new ArrayList<>(Arrays.asList("walk","bike","bus"));
        for (TransportationProfile p : profiles) {
            if (!commuteTypes.contains(p.getVehicleType())) {
                commuteTypes.add(p.getVehicleType());
            }
        }
        req.setAttribute("commuteTypes", commuteTypes);

        Map<String, CostAnalysis> costSummary = new HashMap<>();
        GenericDao<CostAnalysis> costDao = new GenericDao<>(CostAnalysis.class);
        for (String type : commuteTypes) {
            List<CostAnalysis> list = costDao.getByCustomQuery(
                    "from CostAnalysis where user.id=" + userId +
                            " and commuteType='" + type + "' order by analysisId desc"
            );
            if (!list.isEmpty()) {
                costSummary.put(type, list.get(0));
            }
        }
        req.setAttribute("costSummaryMap", costSummary);

        req.getRequestDispatcher("CommutingCostLog.jsp").forward(req, resp);
    }

    private int getLoggedInUserId(HttpServletRequest req) {
        String email = (String) req.getSession().getAttribute("userName");
        if (email == null) {
            throw new IllegalStateException("No user logged in");
        }
        User user = new UserDao().getByEmail(email);
        if (user == null) {
            throw new IllegalStateException("User not found");
        }
        return user.getId();
    }
}
