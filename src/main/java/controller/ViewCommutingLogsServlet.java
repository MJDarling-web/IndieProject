package controller;

import entity.CommutingLog;
import entity.User;
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
import java.util.*;
import jakarta.persistence.*;
import persistence.UserDao;


/**
 * Servlet that retrieves and displays all commuting logs for a user.
 *
 * This servlet handles GET requests by querying the database for all
 * entries for logged-in user. Fetched logs are then passed to a JSP page for display.
 *
 * @author micahdarling
 */
@WebServlet("/CommutingCostLog")
public class ViewCommutingLogsServlet extends HttpServlet {
    // Creates a logger instance for this class
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Handles GET request to display user's commuting logs based on user's email address.
     * Logs messages about operation and forwards data to JSP.
     * If no logs found a message is displayed.
     *
     * @param request HttpServletRequest contains request the client has made of the servlet
     * @param response HttpServletResponse contains the response the servlet sends to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        List<String> logMessages = new ArrayList<>();
        Logger logger = LogManager.getLogger(this.getClass());

        String userEmail = (String) request.getSession().getAttribute("userName");

        if (userEmail == null) {
            response.sendRedirect("logIn.jsp");
            return;
        }

        try {
            UserDao userDao = new UserDao();
            User user = userDao.getByEmail(userEmail);

            if (user == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User not found.");
                return;
            }

            logger.info("Fetching commuting logs for user ID: " + user.getId());

            GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);
            List<CommutingLog> logs = commutingLogDao.getByPropertyEqual("user", user);

            if (logs.isEmpty()) {
                logger.info("No commuting logs found for user.");
            } else {
                logger.info("Fetched " + logs.size() + " commuting logs.");
            }

            request.setAttribute("commutingLogs", logs);
            request.setAttribute("logMessages", logMessages);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/CommutingCostLog.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            logger.error("Error occurred while fetching commuting logs.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to fetch commuting logs.");
        }
    }

}
