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

@WebServlet(urlPatterns = "/viewUsers")
public class ViewUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        // Create an instance of GenericDao for User entity
        GenericDao<User> userDao = new GenericDao<>(User.class);

        // Get all users from the database
        List<User> users = userDao.getAll();

        // Set the list of users as a request attribute
        request.setAttribute("users", users);

        // Forward the request to the JSP for displaying the data
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewUsers.jsp");
        dispatcher.forward(request, response);
    }
}
