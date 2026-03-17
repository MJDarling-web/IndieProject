package controller;

import entity.CommutingLog;
import entity.CostAnalysis;
import entity.Profile;
import entity.TransportationProfile;
import persistence.FuelApiDao;
import persistence.GenericDao;
import util.CommutingCostCalculator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * Servlet for handling the addition of a new commuting log entry for a user.
 *
 * This servlet has POST and GET requests from the commuting log form and records
 * details commuteType, time spent, vehicle, and distance traveled into the database.
 * Uses the user's session to identify current user and associate the log entry appropriately.
 *
 * Mapped to the CommutingLog and forwards to CommutingCostLog jsp.
 *
 * @author Micah Darling
 */
@WebServlet("/addCommutingLog")
public class AddCommutingLogServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Profile user = getLoggedInProfile(req);

        String commuteType = req.getParameter("commuteType");
        double timeSpent = Double.parseDouble(req.getParameter("timeSpent"));
        double distanceInMiles = Double.parseDouble(req.getParameter("distanceInMiles"));

        GenericDao<TransportationProfile> profDao = new GenericDao<>(TransportationProfile.class);
        List<TransportationProfile> profiles = profDao.getByPropertyEqual("user", user);

        double mpg = profiles.stream()
                .filter(p -> p.getVehicleType().equalsIgnoreCase(commuteType))
                .map(TransportationProfile::getMilesPerGallon)
                .findFirst()
                .orElse(25.0);

        double cost;
        String typeLower = commuteType.toLowerCase();
        if (typeLower.equals("walk") || typeLower.equals("bike")) {
            cost = 0.0;
        } else if (typeLower.equals("bus")) {
            cost = 2.00;
        } else {
            CommutingCostCalculator calculator = new CommutingCostCalculator(new FuelApiDao());
            try {
                cost = calculator.computeGasCost(distanceInMiles, mpg);
            } catch (Exception e) {
                throw new ServletException("Failed to fetch fuel price", e);
            }
        }

        CommutingLog log = new CommutingLog();
        log.setUser(user);
        log.setCommuteType(commuteType);
        log.setTimeSpent(timeSpent);
        log.setDistanceInMiles(distanceInMiles);
        log.setCost(cost);
        log.setDate(new Date());
        new GenericDao<>(CommutingLog.class).insert(log);

        List<CommutingLog> userLogs = new GenericDao<>(CommutingLog.class)
                .getByPropertyEqual("user", user);
        req.setAttribute("commutingLogs", userLogs);

        List<String> commuteTypes = new ArrayList<>(Arrays.asList("walk", "bike", "bus"));
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

        GenericDao<CostAnalysis> costDao = new GenericDao<>(CostAnalysis.class);
        Map<String, CostAnalysis> costSummary = new HashMap<>();

        List<CostAnalysis> allUserAnalyses = costDao.getByPropertyEqual("user", user);

        for (String type : commuteTypes) {
            double totalMiles = userLogs.stream()
                    .filter(c -> c.getCommuteType().equalsIgnoreCase(type))
                    .mapToDouble(CommutingLog::getDistanceInMiles)
                    .sum();

            TransportationProfile vehicle = profiles.stream()
                    .filter(p -> p.getVehicleType().equalsIgnoreCase(type))
                    .findFirst()
                    .orElse(null);

            double insurance = vehicle != null ? vehicle.getInsuranceCost() : 0;
            double maintenance = vehicle != null ? vehicle.getMaintenanceCost() : 0;
            double perGallon = vehicle != null ? vehicle.getFuelCostPerGallon() : 3.5;
            double mpgForType = vehicle != null && vehicle.getMilesPerGallon() > 0
                    ? vehicle.getMilesPerGallon() : 25;

            double typeGasCost = (totalMiles / mpgForType) * perGallon;
            double oneYear = insurance + maintenance + typeGasCost;
            double twoYear = oneYear * 2;
            double fiveYear = oneYear * 5;

            CostAnalysis ca = allUserAnalyses.stream()
                    .filter(a -> a.getCommuteType() != null && a.getCommuteType().equalsIgnoreCase(type))
                    .findFirst()
                    .orElse(new CostAnalysis());

            if (ca.getAnalysisId() == 0) {
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

        RequestDispatcher rd = req.getRequestDispatcher("CommutingCostLog.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Profile user = getLoggedInProfile(req);

        String editLogId = req.getParameter("editLogId");
        if (editLogId != null && !editLogId.isEmpty()) {
            try {
                int logId = Integer.parseInt(editLogId);
                CommutingLog editLog = new GenericDao<>(CommutingLog.class).getById(logId);
                if (editLog != null && editLog.getUser().getId().equals(user.getId())) {
                    req.setAttribute("editLog", editLog);
                }
            } catch (NumberFormatException e) {
                // ignore bad editLogId
            }
        }

        List<CommutingLog> userLogs = new GenericDao<>(CommutingLog.class)
                .getByPropertyEqual("user", user);
        req.setAttribute("commutingLogs", userLogs);

        List<TransportationProfile> profiles = new GenericDao<>(TransportationProfile.class)
                .getByPropertyEqual("user", user);

        List<String> commuteTypes = new ArrayList<>(Arrays.asList("walk", "bike", "bus"));
        for (TransportationProfile p : profiles) {
            if (!commuteTypes.contains(p.getVehicleType())) {
                commuteTypes.add(p.getVehicleType());
            }
        }
        req.setAttribute("commuteTypes", commuteTypes);

        Map<String, CostAnalysis> costSummary = new HashMap<>();
        GenericDao<CostAnalysis> costDao = new GenericDao<>(CostAnalysis.class);
        List<CostAnalysis> allUserAnalyses = costDao.getByPropertyEqual("user", user);

        for (String type : commuteTypes) {
            allUserAnalyses.stream()
                    .filter(a -> a.getCommuteType() != null && a.getCommuteType().equalsIgnoreCase(type))
                    .findFirst()
                    .ifPresent(a -> costSummary.put(type, a));
        }

        req.setAttribute("costSummaryMap", costSummary);

        req.getRequestDispatcher("CommutingCostLog.jsp").forward(req, resp);
    }

    private Profile getLoggedInProfile(HttpServletRequest req) {
        String email = (String) req.getSession().getAttribute("userEmail");

        if (email == null || email.isBlank()) {
            throw new IllegalStateException("No user logged in");
        }

        GenericDao<Profile> profileDao = new GenericDao<>(Profile.class);
        List<Profile> profiles = profileDao.getByPropertyEqual("email", email);

        if (profiles == null || profiles.isEmpty()) {
            throw new IllegalStateException("User profile not found");
        }

        return profiles.get(0);
    }
}