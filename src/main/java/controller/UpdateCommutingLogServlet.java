
package controller;

import entity.CommutingLog;
import persistence.GenericDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that handles update to existing commuting log entry.
 *
 * POST request containing updated commuting log data,
 * retrieves user by user's ID,
 * updates fields in database,
 * and redirects user back to CommutingCostLog jsp.
 *
 * @author micahdarling
 */
@WebServlet("/updateCommutingLog")
public class UpdateCommutingLogServlet extends HttpServlet {
    /**
     * Handles POST requests to update an existing commuting log entry.
     *
     * @param req  HttpServletRequest containing form data and the log ID
     * @param resp used to redirect to the commuting log view
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int logId = Integer.parseInt(req.getParameter("logId"));
        GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);

        CommutingLog log = commutingLogDao.getById(logId);
        if (log != null) {
            log.setCommuteType(req.getParameter("commuteType"));
            log.setTimeSpent(Double.parseDouble(req.getParameter("timeSpent")));
            log.setDistanceInMiles(Double.parseDouble(req.getParameter("distanceInMiles")));
            log.setCost(Double.parseDouble(req.getParameter("cost")));

            commutingLogDao.update(log);
        }

        resp.sendRedirect("CommutingCostLog");
    }
}