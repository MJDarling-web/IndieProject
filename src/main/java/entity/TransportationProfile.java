package entity;
import jakarta.persistence.*;
import java.util.Date;

/**
 * Entity representing a user's transportation cost profile.
 * This profile includes details such as the user's vehicle type,
 * fuel efficiency, recurring vehicle expenses (e.g., payment, insurance),
 * and the current fuel cost (for later modification if user wants to change cost per gallon).
 * This data is used to calculate the user's commuting costs over time.
 *
 * Mapped to transportation_costs table in the database.
 *
 * @author micahdarling
 */
@Entity
@Table(name = "transportation_costs")
public class TransportationProfile {

    /** Unique identifier for the transportation profile. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private int profileId;

    /** User's transportation profile. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false)
    private User user;

    /** Type of vehicle used by the user (e.g., Car, Bike, Bus). */
    @Column(name = "vehicle_type")
    private String vehicleType;

    /** Fuel efficiency of the vehicle in miles per gallon (MPG). */
    @Column(name = "miles_per_gallon")
    private Double milesPerGallon = 25.0; // Default 25 MPG

    /** Monthly loan or lease payment for the vehicle. */
    @Column(name = "monthly_payment")
    private Double monthlyPayment = 0.0;

    /** Monthly cost of insurance. */
    @Column(name = "insurance_cost")
    private Double insuranceCost = 0.0;

    /** Monthly maintenance cost for the vehicle. */
    @Column(name = "maintenance_cost")
    private Double maintenanceCost = 0.0;

    /** Cost per gallon of fuel for intial set-up and saved for later if user wants to change cost of fuel*/
    @Column(name = "fuel_cost")
    private Double fuelCostPerGallon = 3.5;

    /** Timestamp of the last update to this profile. */
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated = new Date();
    // Constructor with all fields

    //No-argument constructor
    public TransportationProfile() {}

    //Full constructor
    public TransportationProfile(User user, String vehicleType, Double milesPerGallon,
                                 Double monthlyPayment, Double insuranceCost, Double maintenanceCost,
                                 Double fuelCostPerGallon, Date lastUpdated) {
        this.user = user;
        this.vehicleType = vehicleType;
        this.milesPerGallon = milesPerGallon;
        this.monthlyPayment = monthlyPayment;
        this.insuranceCost = insuranceCost;
        this.maintenanceCost = maintenanceCost;
        this.fuelCostPerGallon = fuelCostPerGallon;
        this.lastUpdated = lastUpdated;
    }

    //Getters and setters
    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getMilesPerGallon() {
        return milesPerGallon;
    }

    public void setMilesPerGallon(Double milesPerGallon) {
        this.milesPerGallon = milesPerGallon;
    }

    public Double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(Double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public Double getInsuranceCost() {
        return insuranceCost;
    }

    public void setInsuranceCost(Double insuranceCost) {
        this.insuranceCost = insuranceCost;
    }

    public Double getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(Double maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public Double getFuelCostPerGallon() {
        return fuelCostPerGallon;
    }

    public void setFuelCostPerGallon(Double fuelCostPerGallon) {
        this.fuelCostPerGallon = fuelCostPerGallon;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
