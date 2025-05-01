package controller;

import entity.CostAnalysis;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Load CostAnalysis records for this user
        GenericDao<CostAnalysis> costDao = new GenericDao<>(CostAnalysis.class);
        String hql = "from CostAnalysis where user.id = " + user.getId() + " order by analysisId desc";
        List<CostAnalysis> analyses = costDao.getByCustomQuery(hql);

        System.out.println("Loaded " + analyses.size() + " cost analyses for user " + user.getId());
        for (CostAnalysis analysis : analyses) {
            System.out.println("Type: " + analysis.getCommuteType() + ", Total: " + analysis.getTotalCost());
        }

        // Build a map: commuteType -> latest CostAnalysis
        Map<String, CostAnalysis> costSummaryMap = new HashMap<>();
        for (CostAnalysis analysis : analyses) {
            String type = analysis.getCommuteType();
            if (!costSummaryMap.containsKey(type)) {
                costSummaryMap.put(type, analysis);
            }
        }

        System.out.println("Final costSummaryMap size = " + costSummaryMap.size());

        req.setAttribute("costSummaryMap", costSummaryMap);

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
