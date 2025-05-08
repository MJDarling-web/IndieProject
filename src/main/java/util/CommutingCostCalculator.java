// src/main/java/service/CommutingCostCalculator.java
package util;

import entity.FuelPrice;
import persistence.FuelApiDao;

public class CommutingCostCalculator {
    private final FuelApiDao fuelDao;

    /**
     * Inject the FuelApiDao so we can swap in a mock during tests.
     */
    public CommutingCostCalculator(FuelApiDao fuelDao) {
        this.fuelDao = fuelDao;
    }

    /**
     * Fetches the current regular fuel price and computes:
     *    cost = (distanceInMiles / mpg) * price
     *
     * @param distanceInMiles total miles driven
     * @param mpg miles per gallon for the vehicle
     * @return the dollar cost of the gas for that distance
     */
    public double computeGasCost(double distanceInMiles, double mpg) throws Exception {
        FuelPrice fp = fuelDao.getFuelPrices();
        double pricePerGallon = Double.parseDouble(fp.getRegular());
        return (distanceInMiles / mpg) * pricePerGallon;
    }
}
