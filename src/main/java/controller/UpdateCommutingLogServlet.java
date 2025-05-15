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
import javax.servlet.http.HttpSession;
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
        // Retrieve the logged-in User from session
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
            return;
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not logged in");
            return;
        }

        // Fetch the log by ID and ensure it belongs to this user
        int logId = Integer.parseInt(req.getParameter("logId"));
        GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);
        CommutingLog log = commutingLogDao.getById(logId);

        if (log == null || log.getUser().getId() != user.getId()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Log not found or unauthorized");
            return;
        }

        // Read updated values from form
        String commuteType = req.getParameter("commuteType");
        double timeSpent = Double.parseDouble(req.getParameter("timeSpent"));
        double distanceInMiles = Double.parseDouble(req.getParameter("distanceInMiles"));

        // Use the user's transportationProfiles instead of custom query
        List<TransportationProfile> profiles = user.getTransportationProfiles();
        double mpg = profiles.stream()
                .filter(p -> p.getVehicleType().equalsIgnoreCase(commuteType))
                .map(TransportationProfile::getMilesPerGallon)
                .findFirst()
                .orElse(25.0);

        // Calculate cost based on commuteType
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

        // Update and persist the log
        log.setCommuteType(commuteType);
        log.setTimeSpent(timeSpent);
        log.setDistanceInMiles(distanceInMiles);
        log.setCost(cost);
        log.setDate(new Date());
        commutingLogDao.update(log);

        // Redirect back to add/view page
        resp.sendRedirect("addCommutingLog");
    }
}
