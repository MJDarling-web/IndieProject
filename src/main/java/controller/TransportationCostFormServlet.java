package controller;

import entity.User;
import entity.TransportationProfile;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.List;

/**
 * Servlet to display transportation cost input form.
 *
 * Handles GET request by forwarding user to transportationCostForm jsp
 * where users can update their vehicle information, pre-populated
 * with their existing profiles loaded from session.
 *
 * @author micahdarling
 */
@WebServlet("/TransportationCostForm")
public class TransportationCostFormServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(TransportationCostFormServlet.class);

    /**
     * Handles GET request to show the transportation cost form.
     * Retrieves the logged-in User from session and their TransportationProfiles.
     * Forwards to JSP with profiles set as request attribute.
     *
     * @param req  HttpServletRequest contains the request the client has made of the servlet
     * @param resp HttpServletResponse contains the response the servlet sends to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Retrieve existing session; redirect to login if missing
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect("logIn.jsp");
            return;
        }

        // Retrieve the User object from session
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("logIn.jsp");
            return;
        }

        logger.info("Loading transportation cost form for user ID: {}", user.getId());

        // Get the user's transportation profiles for pre-population
        List<TransportationProfile> profiles = user.getTransportationProfiles();
        req.setAttribute("transportationProfiles", profiles);

        // Forward to JSP
        req.getRequestDispatcher("TransportationCostForm.jsp").forward(req, resp);
    }
}
