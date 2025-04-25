package controller;

import entity.CommutingLog;
import entity.User;
import persistence.GenericDao;

import jakarta.persistence.*;
import persistence.UserDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        // Save
        GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);
        commutingLogDao.insert(log);

        // update to show updated logs
        String hql = "from CommutingLog where user.id=" + user.getId();
        List<CommutingLog> userLogs = commutingLogDao.getByCustomQuery(hql);
        req.setAttribute("commutingLogs", userLogs);

        RequestDispatcher dispatcher = req.getRequestDispatcher("CommutingCostLog.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
