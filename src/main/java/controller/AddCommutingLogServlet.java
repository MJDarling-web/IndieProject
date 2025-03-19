package controller;

import entity.CommutingLog;
import entity.User;
import persistence.GenericDao;

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
        // Get the logged-in user's ID (you might use session or another method)
        int userId = getLoggedInUserId(req);

        // Retrieve the form parameters
        String commuteType = req.getParameter("commuteType");
        double timeSpent = Double.parseDouble(req.getParameter("timeSpent"));
        double distanceInMiles = Double.parseDouble(req.getParameter("distanceInMiles"));
        double cost = Double.parseDouble(req.getParameter("cost"));

        // Create a new CommutingLog instance
        CommutingLog log = new CommutingLog();
        log.setCommuteType(commuteType);
        log.setTimeSpent(timeSpent);
        log.setDistanceInMiles(distanceInMiles);
        log.setCost(cost);

        // Set the user for the log
        User user = new User();
        user.setId(userId);  // Set the ID of the logged-in user
        log.setUser(user);

        // Save the log to the database
        GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);
        commutingLogDao.insert(log);

        // Redirect back to the view page to see the updated list of logs
        resp.sendRedirect("viewCommutingLogs");
    }

    // Example method to get the logged-in user's ID
    private int getLoggedInUserId(HttpServletRequest req) {
        return (int) req.getSession().getAttribute("userId");
    }
}
