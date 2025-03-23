package controller;

import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.GenericDao;
import jakarta.persistence.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/viewUsers")
public class ViewUsersServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        // Create an instance of GenericDao for User entity
        GenericDao<User> userDao = new GenericDao<>(User.class);
        System.out.println("Logger working 77: " + logger.isDebugEnabled());
        logger.info("connected");
        // Get all users from the database
        List<User> users = userDao.getAll();
        logger.info(users);
        // Set the list of users as a request attribute
        request.setAttribute("users", users);
        System.out.println(users);
        // Forward the request to the JSP for displaying the data
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewUsers.jsp");
        dispatcher.forward(request, response);

    }
}
