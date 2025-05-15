package entity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import auth.*;
import persistence.UserDao;
import util.PropertiesLoader;
import org.apache.commons.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;
import java.util.stream.Collectors;
@WebServlet(urlPatterns = {"/auth"})
public class Auth extends HttpServlet implements PropertiesLoader {
    Properties properties;
    String CLIENT_ID, CLIENT_SECRET, OAUTH_URL, LOGIN_URL, REDIRECT_URL, REGION, POOL_ID;
    Keys jwks;

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public void init() throws ServletException {
        super.init();
        loadProperties();
        loadKey();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authCode = req.getParameter("code");

        if (authCode == null) {
            logger.error("No auth code received from Cognito.");
            resp.sendRedirect("index.jsp"); // or error page
            return;
        }

        try {
            HttpRequest authRequest = buildAuthRequest(authCode);
            TokenResponse tokenResponse = getToken(authRequest);

            DecodedJWT jwt = JWT.decode(tokenResponse.getIdToken());
            String email = jwt.getClaim("email").asString(); // ✅ actual email from Cognito
            String username = jwt.getClaim("cognito:username").asString(); // use as display name

            logger.debug("Authenticated email: " + email);

            // Save to session
            HttpSession session = req.getSession();
            session.setAttribute("userName", email); // 🔄 now always the email address

            // Look for user by email
            UserDao userDao = new UserDao();
            User user = userDao.getByEmail(email);

            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setFirstName(username);  // Use username as a fallback first name
                user.setLastName("n/a");      // Placeholder
                userDao.insertUser(user);
                logger.debug("Inserted new user into database.");
            } else {
                logger.debug("User already exists.");
            }

            RequestDispatcher dispatcher = req.getRequestDispatcher("Profile.jsp");
            dispatcher.forward(req, resp);

        } catch (Exception e) {
            logger.error("Authentication failed: " + e.getMessage(), e);
            resp.sendRedirect("index.jsp"); // forward to an error page if preferred
        }
    }

    private TokenResponse getToken(HttpRequest authRequest) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<?> response = client.send(authRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body().toString(), TokenResponse.class);
    }

    private HttpRequest buildAuthRequest(String authCode) {
        String keys = CLIENT_ID + ":" + CLIENT_SECRET;

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "authorization_code");
        parameters.put("client-secret", CLIENT_SECRET);
        parameters.put("client_id", CLIENT_ID);
        parameters.put("code", authCode);
        parameters.put("redirect_uri", REDIRECT_URL);

        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String encoding = Base64.getEncoder().encodeToString(keys.getBytes());

        return HttpRequest.newBuilder()
                .uri(URI.create(OAUTH_URL))
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + encoding)
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
    }

    private void loadKey() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            URL jwksURL = new URL(String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", REGION, POOL_ID));
            File jwksFile = new File("jwks.json");
            FileUtils.copyURLToFile(jwksURL, jwksFile);
            jwks = mapper.readValue(jwksFile, Keys.class);
        } catch (Exception e) {
            logger.error("Failed to load JWKS: " + e.getMessage(), e);
        }
    }

    private void loadProperties() {
        try {
            properties = loadProperties("/cognito.properties");
            CLIENT_ID = properties.getProperty("client.id");
            CLIENT_SECRET = properties.getProperty("client.secret");
            OAUTH_URL = properties.getProperty("oauthURL");
            LOGIN_URL = properties.getProperty("loginURL");
            REDIRECT_URL = properties.getProperty("redirectURL");
            REGION = properties.getProperty("region");
            POOL_ID = properties.getProperty("poolId");
        } catch (Exception e) {
            logger.error("Failed to load Cognito properties: " + e.getMessage(), e);
        }
    }
}


