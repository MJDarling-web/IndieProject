package controller;

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
import java.util.logging.Logger;

@WebServlet("/viewUsers")
public class ViewUsersServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ViewUsersServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Create GenericDao for User entity
        GenericDao<User> userDao = new GenericDao<>(User.class);

        // HQL query to fetch all users
        String hql = "FROM User";  // HQL to get all users

        List<User> users = null;
        try {
            // Fetch users using the GenericDao
            users = userDao.getByCustomQuery(hql);

            // Log the number of users retrieved
            logger.info("Fetched " + (users != null ? users.size() : 0) + " users from the database.");
        } catch (Exception e) {
            logger.severe("Error fetching users: " + e.getMessage());
            e.printStackTrace();
        }

        // Set users as a request attribute
        req.setAttribute("users", users);

        // Forward the request to viewUsers.jsp
        RequestDispatcher dispatcher = req.getRequestDispatcher("/LoginRegister.jsp");
        dispatcher.forward(req, resp);
    }
}
