package controller;

import entity.TransportationProfile;
import entity.User;
import persistence.GenericDao;
import persistence.UserDao;

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
 * Servlet that handles displaying and saving user's vehicle profile
 *
 * GET request updates user's existing transportationProfile or initializes default one.
 * POST request updates profile based on form input and persists the changes using generic DAO
 *
 * @author micahdarling
 */
@WebServlet("/saveVehicleProfile")
public class SaveVehicleProfileServlet extends HttpServlet {

    /**
     * Handles GET requests to load user's current vehicle profile
     *
     * @param req HttpServletRequest request the client has made of the servlet
     * @param resp HttpServletResponse contains the response the servlet sends to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String userEmail = (String) session.getAttribute("userName");

        if (userEmail == null) {
            resp.sendRedirect("logIn.jsp");
            return;
        }

        UserDao userDao = new UserDao();
        User user = userDao.getByEmail(userEmail);

        GenericDao<TransportationProfile> profileDao = new GenericDao<>(TransportationProfile.class);
        TransportationProfile profile;

        // Check if profile exists
        List<TransportationProfile> existingProfiles = profileDao.getByCustomQuery(
                "from TransportationProfile where user.id = " + user.getId()
        );

        if (!existingProfiles.isEmpty()) {
            profile = existingProfiles.get(0);
        } else {
            profile = new TransportationProfile();
            profile.setUser(user);
            profile.setVehicleType("Car"); // Default values
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

    /**
     * @param req  an {@link HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String userEmail = (String) session.getAttribute("userName");

        if (userEmail == null) {
            resp.sendRedirect("logIn.jsp");
            return;
        }

        UserDao userDao = new UserDao();
        User user = userDao.getByEmail(userEmail);

        GenericDao<TransportationProfile> profileDao = new GenericDao<>(TransportationProfile.class);
        TransportationProfile profile;

        List<TransportationProfile> existingProfiles = profileDao.getByCustomQuery(
                "from TransportationProfile where user.id = " + user.getId()
        );

        if (!existingProfiles.isEmpty()) {
            profile = existingProfiles.get(0);
        } else {
            profile = new TransportationProfile();
            profile.setUser(user);
        }

        // Update profile fields from form
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

        // After saving, reload the form with updated data
        req.setAttribute("vehicleProfile", profile);
        req.setAttribute("successMessage", "Your vehicle profile was saved successfully!");
        req.getRequestDispatcher("MyVehicles.jsp").forward(req, resp);
    }

    /**
     * @param value
     * @param defaultValue
     * @return defaultValue
     */
    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
