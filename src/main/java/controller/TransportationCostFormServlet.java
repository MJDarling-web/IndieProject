package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to display transportation cost input form.
 *
 * Handles GET request by fowarding user to transportationCostForm jsp.
 * where users can update their vehicle information.
 *
 * @author micahdarling
 */
@WebServlet("/TransportationCostForm")
public class TransportationCostFormServlet extends HttpServlet {
    /**
     * @param req  HttpServletRequest contains the request the client has made of the servlet
     * @param resp HttpServletResponse contains the response the servlet sends to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("TransportationCostForm.jsp").forward(req, resp);
    }
}
