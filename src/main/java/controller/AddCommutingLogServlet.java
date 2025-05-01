package controller;

import entity.CommutingLog;
import entity.CostAnalysis;
import entity.TransportationCost;
import entity.User;
import persistence.GenericDao;
import persistence.UserDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/addCommutingLog")
public class AddCommutingLogServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = getLoggedInUserId(req);

        // Grab form inputs
        String commuteType = req.getParameter("commuteType");
        double timeSpent = Double.parseDouble(req.getParameter("timeSpent"));
        double distanceInMiles = Double.parseDouble(req.getParameter("distanceInMiles"));
        double cost = Double.parseDouble(req.getParameter("cost"));

        // Create and populate log
        CommutingLog log = new CommutingLog();
        log.setCommuteType(commuteType);
        log.setTimeSpent(timeSpent);
        log.setDistanceInMiles(distanceInMiles);
        log.setCost(cost);
        log.setDate(new java.util.Date());

        // Assign user by ID
        UserDao userDao = new UserDao();
        User user = userDao.getById(userId);
        log.setUser(user);

        // Save log
        GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);
        commutingLogDao.insert(log);

        // Load all logs
        String hql = "from CommutingLog where user.id=" + user.getId();
        List<CommutingLog> userLogs = commutingLogDao.getByCustomQuery(hql);
        req.setAttribute("commutingLogs", userLogs);

        // Load commute type options
        GenericDao<TransportationCost> transportationCostDao = new GenericDao<>(TransportationCost.class);
        String vehicleHql = "from TransportationCost where user.id = " + user.getId();
        List<TransportationCost> vehicleProfiles = transportationCostDao.getByCustomQuery(vehicleHql);

        List<String> commuteTypes = new ArrayList<>();
        commuteTypes.add("walk");
        commuteTypes.add("bike");
        commuteTypes.add("bus");
        for (TransportationCost vehicle : vehicleProfiles) {
            if (!commuteTypes.contains(vehicle.getVehicleType())) {
                commuteTypes.add(vehicle.getVehicleType());
            }
        }

        if (vehicleProfiles.isEmpty() && !commuteTypes.contains("car")) {
            commuteTypes.add("car");
            req.setAttribute("vehicleWarning", "You haven't added any vehicles yet. Default 'car' option added. Please add a vehicle profile for more accurate costs.");
        }

        req.setAttribute("commuteTypes", commuteTypes);

        // TODO update calculations so they actually calculate based on the users vehicle, cost of insurance, and total monthly commuting costs of gas for all vehicle types"
        GenericDao<CostAnalysis> costDao = new GenericDao<>(CostAnalysis.class);
        Map<String, CostAnalysis> costSummaryMap = new HashMap<>();

        for (String type : commuteTypes) {
            double totalMiles = 0;
            for (CommutingLog cl : userLogs) {
                if (cl.getCommuteType().equalsIgnoreCase(type)) {
                    totalMiles += cl.getDistanceInMiles();
                }
            }

            TransportationCost matchedVehicle = null;
            for (TransportationCost v : vehicleProfiles) {
                if (v.getVehicleType().equalsIgnoreCase(type)) {
                    matchedVehicle = v;
                    break;
                }
            }

            double insurance = matchedVehicle != null ? matchedVehicle.getInsuranceCost() : 0;
            double maintenance = matchedVehicle != null ? matchedVehicle.getMaintenanceCost() : 0;
            double mpg = matchedVehicle != null && matchedVehicle.getMilesPerGallon() > 0 ? matchedVehicle.getMilesPerGallon() : 25;
            double fuelCostPerGallon = matchedVehicle != null ? matchedVehicle.getFuelCost() : 3.5;

            double gasCost = (totalMiles / mpg) * fuelCostPerGallon;
            double oneYear = insurance + maintenance + gasCost;
            double twoYear = oneYear * 2;
            double fiveYear = oneYear * 5;

            // Check if existing analysis already exists for user+type
            String costHql = "from CostAnalysis where user.id = " + user.getId() +
                    " and commuteType = '" + type + "' order by analysisId desc";
            List<CostAnalysis> existing = costDao.getByCustomQuery(costHql);

            CostAnalysis analysis;
            if (!existing.isEmpty()) {
                analysis = existing.get(0);
            } else {
                analysis = new CostAnalysis();
                analysis.setUser(user);
                analysis.setCommuteType(type);
            }

            analysis.setOneYearCost(oneYear);
            analysis.setTwoYearCost(twoYear);
            analysis.setFiveYearCost(fiveYear);
            analysis.setTotalCost(oneYear + twoYear + fiveYear);

            // Save or update existing
            if (analysis.getAnalysisId() > 0) {
                costDao.update(analysis);
            } else {
                costDao.insert(analysis);
            }

            costSummaryMap.put(type, analysis);
        }

        req.setAttribute("costSummaryMap", costSummaryMap);
        RequestDispatcher dispatcher = req.getRequestDispatcher("CommutingCostLog.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = getLoggedInUserId(req);

        UserDao userDao = new UserDao();
        User user = userDao.getById(userId);

        GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);
        String hql = "from CommutingLog where user.id=" + user.getId();
        List<CommutingLog> userLogs = commutingLogDao.getByCustomQuery(hql);
        req.setAttribute("commutingLogs", userLogs);

        GenericDao<TransportationCost> transportationCostDao = new GenericDao<>(TransportationCost.class);
        String vehicleHql = "from TransportationCost where user.id = " + user.getId();
        List<TransportationCost> vehicleProfiles = transportationCostDao.getByCustomQuery(vehicleHql);

        List<String> commuteTypes = new ArrayList<>();
        commuteTypes.add("walk");
        commuteTypes.add("bike");
        commuteTypes.add("bus");
        for (TransportationCost vehicle : vehicleProfiles) {
            if (!commuteTypes.contains(vehicle.getVehicleType())) {
                commuteTypes.add(vehicle.getVehicleType());
            }
        }
        req.setAttribute("commuteTypes", commuteTypes);

        GenericDao<CostAnalysis> costDao = new GenericDao<>(CostAnalysis.class);
        Map<String, CostAnalysis> costSummaryMap = new HashMap<>();
        for (String type : commuteTypes) {
            String costHql = "from CostAnalysis where user.id = " + user.getId() +
                    " and commuteType = '" + type + "' order by analysisId desc";
            List<CostAnalysis> costSummaries = costDao.getByCustomQuery(costHql);
            if (!costSummaries.isEmpty()) {
                costSummaryMap.put(type, costSummaries.get(0));
            }
        }
        req.setAttribute("costSummaryMap", costSummaryMap);

        req.getRequestDispatcher("CommutingCostLog.jsp").forward(req, resp);
    }

    private int getLoggedInUserId(HttpServletRequest req) {
        String userEmail = (String) req.getSession().getAttribute("userName");

        if (userEmail == null) {
            throw new IllegalStateException("No user is logged in.");
        }

        UserDao userDao = new UserDao();
        User user = userDao.getByEmail(userEmail);

        if (user == null) {
            throw new IllegalStateException("User not found in the database.");
        }

        return user.getId();
    }
}
