package controller;

import entity.CommutingLog;
import entity.TransportationProfile;
import entity.User;
import persistence.FuelApiDao;
import persistence.GenericDao;
import persistence.UserDao;
import util.CommutingCostCalculator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Servlet for updating an existing commuting log entry.
 * Fetches the log by ID, updates fields from form, recalculates cost, and saves changes.
 */
@WebServlet("/updateCommutingLog")
public class UpdateCommutingLogServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = getLoggedInUserId(req);
        User user = new UserDao().getById(userId);

        int logId = Integer.parseInt(req.getParameter("logId"));
        GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);
        CommutingLog log = commutingLogDao.getById(logId);

        if (log == null || log.getUser().getId() != userId) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Log not found or unauthorized");
            return;
        }

        String commuteType = req.getParameter("commuteType");
        double timeSpent = Double.parseDouble(req.getParameter("timeSpent"));
        double distanceInMiles = Double.parseDouble(req.getParameter("distanceInMiles"));

        // Lookup user profile to compute cost
        GenericDao<TransportationProfile> profileDao = new GenericDao<>(TransportationProfile.class);
        List<TransportationProfile> profiles = profileDao.getByCustomQuery(
                "from TransportationProfile where user.id = " + userId);

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
            try {
                CommutingCostCalculator calculator = new CommutingCostCalculator(new FuelApiDao());
                cost = calculator.computeGasCost(distanceInMiles, mpg);
            } catch (Exception e) {
                throw new ServletException("Failed to calculate fuel cost", e);
            }
        }

        // Update the log
        log.setCommuteType(commuteType);
        log.setTimeSpent(timeSpent);
        log.setDistanceInMiles(distanceInMiles);
        log.setCost(cost);
        log.setDate(new Date());

        commutingLogDao.update(log);

        resp.sendRedirect("addCommutingLog");
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
