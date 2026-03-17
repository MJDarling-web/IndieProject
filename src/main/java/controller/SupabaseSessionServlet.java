package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth/session")
public class SupabaseSessionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(req.getReader());

        String accessToken = json.has("accessToken") ? json.get("accessToken").asText() : null;
        String email = json.has("email") ? json.get("email").asText() : null;

        if (accessToken == null || accessToken.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing access token");
            return;
        }

        if (email == null || email.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing email");
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("userEmail", email);
        session.setAttribute("userName", email);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}