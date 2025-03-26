package persistence;

import org.junit.jupiter.api.Test;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import static javax.ws.rs.client.ClientBuilder.newClient;
import static org.junit.jupiter.api.Assertions.*;

public class fuelAPITest {

    private static final String API_URL = "https://www.fueleconomy.gov/ws/rest/fuelprices";

    @Test
    public void testFuelPricesAPI() throws Exception {
        // Create JAX-RS client
        Client client = newClient();

        // Define the target API URL
        WebTarget target = client.target(API_URL);

        // Send the GET request to the API with the expected response format as JSON
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        // Assert that the response status is 200 OK
        assertEquals(200, response.getStatus());

        // Assert that the response content is not null
        String jsonResponse = response.readEntity(String.class);
        assertNotNull(jsonResponse);

        // Print the response for verification
        System.out.println("Response: " + jsonResponse);

        // Example of asserting specific price values in the response
        // Check if the 'regular' fuel price is correct (we expect 3.07)
        // assert jsonResponse.contains("\"regular\":\"3.07\"");

        // Close the response
        response.close();
    }
}
