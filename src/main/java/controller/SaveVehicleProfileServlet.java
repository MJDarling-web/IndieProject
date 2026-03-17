package controller;

import entity.Profile;
import entity.TransportationProfile;
import persistence.GenericDao;

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
 * Servlet that handles displaying and saving user's vehicle profile
 *
 * GET request updates user's existing transportationProfile or initializes default one.
 * POST request updates profile based on form input and persists the changes using generic DAO
 */
@WebServlet("/saveVehicleProfile")
public class SaveVehicleProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Profile user = getLoggedInProfile(req);
        UUID userId = user.getId();

        GenericDao<TransportationProfile> profileDao = new GenericDao<>(TransportationProfile.class);
        TransportationProfile profile;

        List<TransportationProfile> existingProfiles = profileDao.getByCustomQuery(
                "from TransportationProfile where user.id = '" + userId + "'"
        );

        if (!existingProfiles.isEmpty()) {
            profile = existingProfiles.get(0);
        } else {
            profile = new TransportationProfile();
            profile.setUser(user);
            profile.setVehicleType("Car");
            profile.setMilesPerGallon(25.0);
            profile.setMonthlyPayment(0.0);
            profile.setInsuranceCost(0.0);
            profile.setMaintenanceCost(0.0);
            profile.setFuelCostPerGallon(3.5);
            profile.setLastUpdated(new Date());
        }

        req.setAttribute("vehicleProfile", profile);
        req.getRequestDispatcher("MyVehicles.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Profile user = getLoggedInProfile(req);
        UUID userId = user.getId();

        GenericDao<TransportationProfile> profileDao = new GenericDao<>(TransportationProfile.class);
        TransportationProfile profile;

        List<TransportationProfile> existingProfiles = profileDao.getByCustomQuery(
                "from TransportationProfile where user.id = '" + userId + "'"
        );

        if (!existingProfiles.isEmpty()) {
            profile = existingProfiles.get(0);
        } else {
            profile = new TransportationProfile();
            profile.setUser(user);
        }

        profile.setVehicleType(req.getParameter("vehicleType"));
        profile.setMilesPerGallon(parseDoubleOrDefault(req.getParameter("milesPerGallon"), 25));
        profile.setMonthlyPayment(parseDoubleOrDefault(req.getParameter("monthlyPayment"), 0));
        profile.setInsuranceCost(parseDoubleOrDefault(req.getParameter("insuranceCost"), 0));
        profile.setMaintenanceCost(parseDoubleOrDefault(req.getParameter("maintenanceCost"), 0));
        profile.setFuelCostPerGallon(parseDoubleOrDefault(req.getParameter("fuelCostPerGallon"), 3.5));
        profile.setLastUpdated(new Date());

        if (profile.getProfileId() > 0) {
            profileDao.update(profile);
        } else {
            profileDao.insert(profile);
        }

        req.setAttribute("vehicleProfile", profile);
        req.setAttribute("successMessage", "Your vehicle profile was saved successfully!");
        req.getRequestDispatcher("MyVehicles.jsp").forward(req, resp);
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

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}