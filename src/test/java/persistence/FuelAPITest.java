package persistence;

import entity.FuelPrice;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FuelAPITest {

    @Test
    public void testRegularPriceExists() {
        try {
            FuelApiDao dao = new FuelApiDao();

            FuelPrice prices = dao.getFuelPrices();

            assertNotNull(prices.getRegular(), "Regular price should not be null");
            assertFalse(prices.getRegular().isEmpty(), "Regular price should not be empty");

        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
}