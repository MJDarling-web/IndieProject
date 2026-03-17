package controller;

import entity.CommutingLog;
import entity.Profile;
import entity.TransportationProfile;
import persistence.FuelApiDao;
import persistence.GenericDao;
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
import java.util.UUID;

/**
 * Servlet for updating an existing commuting log entry.
 * Fetches the log by ID, updates fields from form, recalculates cost, and saves changes.
 */
@WebServlet("/updateCommutingLog")
public class UpdateCommutingLogServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Profile user = getLoggedInProfile(req);
        UUID userId = user.getId();

        int logId = Integer.parseInt(req.getParameter("logId"));
        GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);
        CommutingLog log = commutingLogDao.getById(logId);

        if (log == null || !log.getUser().getId().equals(userId)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Log not found or unauthorized");
            return;
        }

        String commuteType = req.getParameter("commuteType");
        double timeSpent = Double.parseDouble(req.getParameter("timeSpent"));
        double distanceInMiles = Double.parseDouble(req.getParameter("distanceInMiles"));

        GenericDao<TransportationProfile> profileDao = new GenericDao<>(TransportationProfile.class);
        List<TransportationProfile> profiles = profileDao.getByCustomQuery(
                "from TransportationProfile where user.id = '" + userId + "'"
        );

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

        log.setCommuteType(commuteType);
        log.setTimeSpent(timeSpent);
        log.setDistanceInMiles(distanceInMiles);
        log.setCost(cost);
        log.setDate(new Date());

        commutingLogDao.update(log);

        resp.sendRedirect("addCommutingLog");
    }

    private Profile getLoggedInProfile(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        if (session == null) {
            throw new IllegalStateException("No active session");
        }

        String email = (String) session.getAttribute("userEmail");

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