package persistence;

import entity.FuelPrice;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FuelAPITest {

    @Test
    public void testRegularPriceExists() {
        try {
            // 1. Create DAO instance
            FuelApiDao dao = new FuelApiDao();

            // 2. Get fuel prices from API
            FuelPrice prices = dao.getFuelPrices();

            // 3. Verify regular price exists and is not empty
            assertNotNull(prices.getRegular(), "Regular price should not be null");
            assertFalse(prices.getRegular().isEmpty(), "Regular price should not be empty");

            System.out.println("Regular price exists: $" + prices.getRegular());

        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
}