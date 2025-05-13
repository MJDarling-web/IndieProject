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
 * Servlet that handles deletion of a commuting log entry
 *
 * Has POST request to remove specific commutingLog entry
 * from the database using its unique ID. After successful deletion
 * it redirects the user back to the commuting log overview page.
 *
 * @author micahdarling
 */
@WebServlet("/deleteCommutingLog")
public class DeleteCommutingLogServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int logId = Integer.parseInt(req.getParameter("logId"));
        GenericDao<CommutingLog> commutingLogDao = new GenericDao<>(CommutingLog.class);

        CommutingLog log = commutingLogDao.getById(logId);
        if (log != null) {
            commutingLogDao.deleteEntity(log);
        }

        resp.sendRedirect("addCommutingLog");
    }


}
