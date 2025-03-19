package controller;

import entity.User;
import persistence.UserDao;

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
        // Create UserDao
        UserDao userDao = new UserDao();

        // Fetch all users
        List<User> users = userDao.getAll();

        // Log the number of users retrieved
        logger.info("Fetched " + (users != null ? users.size() : 0) + " users from the database.");

        // Log the users to check the data
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                logger.info("User: " + user.getFirstName() + " " + user.getLastName() + " - " + user.getEmail());
            }
        } else {
            logger.warning("No users found in the database.");
        }

        // Set users as a request attribute
        req.setAttribute("users", users);

        // Forward the request to LoginRegister.jsp
        RequestDispatcher dispatcher = req.getRequestDispatcher("/LoginRegister.jsp");
        dispatcher.forward(req, resp);
    }
}