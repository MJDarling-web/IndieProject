package controller;

import entity.User;
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

/**
 * Servlet that handles user sign out.
 * Uses GET request to invalidate session and forwards to index jsp.
 * @author micahdarling
 */
@WebServlet("/signout")
public class SignOutServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(SignOutServlet.class);

    /**
     * Invalidates the current user session and redirects to home page.
     *
     * @param req HttpServletRequest contains users session
     * @param resp HttpServletResponse forwards to index home page
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Invalidate session to clear stored User
        HttpSession session = req.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                logger.info("Signing out user ID: {}", user.getId());
            }
            session.invalidate();
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }
}
