package controller;

import entity.CommutingLog;
import persistence.GenericDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/viewCommutingLogs")
public class ViewCommutingLogsServlet extends HttpServlet {

    // Create a logger instance for this class
    private static final Logger logger = LogManager.getLogger(ViewCommutingLogsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Create a list to hold log messages
        List<String> logMessages = new ArrayList<>();

        // Assuming we don't have a login system yet, so set userId to 1 for testing
        int userId = 1;  // Change this to a valid user ID when you implement login

        logMessages.add("Starting to fetch commuting logs for user ID: " + userId);
        logger.info("Starting to fetch commuting logs for user ID: " + userId);

        // Fetch commuting logs for this user
        GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);

        // HQL query to fetch logs for the user
        String hql = "FROM CommutingLog";  // Query to get all logs for now

        try {
            // Get logs from the database
            List<CommutingLog> logs = commutingLogDao.getByCustomQuery(hql);

            // Log the number of records fetched
            if (logs.isEmpty()) {
                logMessages.add("No commuting logs found for user ID: " + userId);
                logger.warn("No commuting logs found for user ID: " + userId);
            } else {
                logMessages.add("Fetched " + logs.size() + " commuting logs for user ID: " + userId);
                logger.info("Fetched " + logs.size() + " commuting logs for user ID: " + userId);
            }

            // Set logs as a request attribute to pass them to the JSP
            req.setAttribute("commutingLogs", logs);
            req.setAttribute("logMessages", logMessages); // Pass log messages to JSP

            // Forward the request to the JSP page
            RequestDispatcher dispatcher = req.getRequestDispatcher("/CommutingCostLog.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            // Log an error if the database query fails
            logMessages.add("Error occurred while fetching commuting logs for user ID: " + userId);
            logger.error("Error occurred while fetching commuting logs for user ID: " + userId, e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to fetch commuting logs");
        }
    }
}
