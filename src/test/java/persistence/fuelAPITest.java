package persistence;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class fuelAPITest {
    private static final String API_URL = "https://www.fueleconomy.gov/ws/rest/fuelprices";

    @Test
    public void testFuelPricesAPI() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).header("Accept", "application/json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
        System.out.println("Response: " + response.body());
    }
}