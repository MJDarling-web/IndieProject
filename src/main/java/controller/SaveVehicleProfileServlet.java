
package controller;

import entity.TransportationCost;
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

@WebServlet("/saveVehicleProfile")
public class SaveVehicleProfileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Received POST at /saveVehicleProfile");

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

        TransportationCost profile = new TransportationCost();
        profile.setUser(user);
        profile.setVehicleType(req.getParameter("vehicleType"));
        profile.setInsuranceCost(parseDoubleOrDefault(req.getParameter("insuranceCost"), 0));
        profile.setFuelCost(parseDoubleOrDefault(req.getParameter("fuelCost"), 0));
        profile.setMaintenanceCost(parseDoubleOrDefault(req.getParameter("maintenanceCost"), 0));
        profile.setMilesPerGallon(parseDoubleOrDefault(req.getParameter("milesPerGallon"), 0));
        profile.setPublicTransportCost(parseDoubleOrDefault(req.getParameter("publicTransportCost"), 0));
        profile.setCreatedDate(new Date());

        GenericDao<TransportationCost> dao = new GenericDao<>(TransportationCost.class);
        dao.insert(profile);

        resp.sendRedirect("addCommutingLog");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Received GET at /saveVehicleProfile");
        req.getRequestDispatcher("TransportationCostForm.jsp").forward(req, resp);
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}