package controller;

//TODO double check for user login and redirect if user is not logged in
//TODO create the logic of total cost of transportation with users car

import entity.ComparisonCost;
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

@WebServlet("/comparisonCost")
public class ComparisonCostServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("ComparisonCost.jsp").forward(req, resp);
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}