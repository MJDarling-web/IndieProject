package persistence;

import entity.FuelPrice;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FuelApiDao {
    private static final String API_URL = "https://www.fueleconomy.gov/ws/rest/fuelprices";

    public String getFuelPricesAsJson() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Accept", "application/json")
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public FuelPrice getFuelPrices() throws Exception {
        String json = getFuelPricesAsJson();
        return new ObjectMapper().readValue(json, FuelPrice.class);
    }
}