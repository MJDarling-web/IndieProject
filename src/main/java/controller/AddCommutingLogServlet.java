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
import java.util.List;

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
        req.setAttribute("commuteTypes", commuteTypes);

        // Calculate Cost Analysis
        double totalMiles = userLogs.stream()
                .filter(l -> l.getCommuteType().equalsIgnoreCase(commuteType))
                .mapToDouble(CommutingLog::getDistanceInMiles)
                .sum();

        TransportationCost matchedVehicle = vehicleProfiles.stream()
                .filter(v -> v.getVehicleType().equalsIgnoreCase(commuteType))
                .findFirst()
                .orElse(null);

        double insurance = matchedVehicle != null ? matchedVehicle.getInsuranceCost() : 0;
        double maintenance = matchedVehicle != null ? matchedVehicle.getMaintenanceCost() : 0;
        double mpg = matchedVehicle != null && matchedVehicle.getMilesPerGallon() > 0 ? matchedVehicle.getMilesPerGallon() : 25;
        double fuelCostPerGallon = matchedVehicle != null ? matchedVehicle.getFuelCost() : 3.5;

        double gasCost = (totalMiles / mpg) * fuelCostPerGallon;
        double oneYear = insurance + maintenance + gasCost;
        double twoYear = oneYear * 2;
        double fiveYear = oneYear * 5;

        CostAnalysis analysis = new CostAnalysis();
        analysis.setUser(user);
        analysis.setCommuteType(commuteType);
        analysis.setOneYearCost(oneYear);
        analysis.setTwoYearCost(twoYear);
        analysis.setFiveYearCost(fiveYear);
        analysis.setTotalCost(oneYear + twoYear + fiveYear);

        GenericDao<CostAnalysis> costDao = new GenericDao<>(CostAnalysis.class);
        costDao.insert(analysis);

        RequestDispatcher dispatcher = req.getRequestDispatcher("CommutingCostLog.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = getLoggedInUserId(req);

        // Assign user by ID
        UserDao userDao = new UserDao();
        User user = userDao.getById(userId);

        // Load commute logs
        GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);
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

        // Load most recent cost summary (if available)
        GenericDao<CostAnalysis> costDao = new GenericDao<>(CostAnalysis.class);
        String costHql = "from CostAnalysis where user.id = " + user.getId() + " order by analysisId desc";
        List<CostAnalysis> costSummaries = costDao.getByCustomQuery(costHql);

        if (!costSummaries.isEmpty()) {
            req.setAttribute("costSummary", costSummaries.get(0));
        }

        req.setAttribute("commuteTypes", commuteTypes);

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
