package controller;

import entity.CommutingLog;
import entity.User;
import persistence.GenericDao;
import persistence.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        // Retrieve existing session; redirect to login if missing
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("logIn.jsp");
            return;
        }

        // Retrieve the full User object from session instead of just the email
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("logIn.jsp");
            return;
        }

        try {
            // Log that we are fetching commuting logs for this user
            logger.info("Fetching commuting logs for user ID: {}", user.getId());

            // Use Hibernate-managed collection rather than new DAO lookup
            List<CommutingLog> logs = user.getCommutingLogs();

            if (logs.isEmpty()) {
                logger.info("No commuting logs found for user ID: {}", user.getId());
            } else {
                logger.info("Fetched {} commuting logs for user ID: {}", logs.size(), user.getId());
            }

            // Pass logs and messages to JSP
            request.setAttribute("commutingLogs", logs);
            request.setAttribute("logMessages", logMessages);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/CommutingCostLog.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            logger.error("Error occurred while fetching commuting logs for user ID: {}", user.getId(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to fetch commuting logs.");
        }
    }
}
