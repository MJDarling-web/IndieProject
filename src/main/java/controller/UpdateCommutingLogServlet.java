
package controller;

import entity.CommutingLog;
import persistence.GenericDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/updateCommutingLog")
public class UpdateCommutingLogServlet extends HttpServlet {
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