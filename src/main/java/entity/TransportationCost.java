package entity;
import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name = "transportation_costs")
public class TransportationCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "cost_id")
    private int costId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user; // user associated with this transportation cost rocord

    @Column(name = "insurance_cost")
    private Double insuranceCost;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "fuel_cost")
    private double fuelCost;

    @Column(name = "maintenance_cost")
    private double maintenanceCost;

    @Column(name= "public_transport_cost")
    private double publicTransportCost;

    @Column(name="created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date CreatedDate;

    @Column(name="miles_per_gallon")
    private double milesPerGallon;

    // No-argument constructor
    public TransportationCost() {
    }

    // Constructor with all fields
    public TransportationCost(Double insuranceCost, String vehicleType, Double fuelCost, Double maintenanceCost, Double publicTransportCost, Date CreatedDate, double milesPerGallon) {
        this.insuranceCost = insuranceCost;
        this.vehicleType = vehicleType;
        this.fuelCost = fuelCost;
        this.maintenanceCost = maintenanceCost;
        this.publicTransportCost = publicTransportCost;
        this.CreatedDate = CreatedDate;
        this.milesPerGallon = milesPerGallon;
    }

    //getters and setters
    public int getCostId() {
        return costId;
    }

    public void setCostId(int costId) {
        this.costId = costId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getInsuranceCost() {
        return insuranceCost;
    }

    public void setInsuranceCost(Double insuranceCost) {
        this.insuranceCost = insuranceCost;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public double getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(double maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public double getPublicTransportCost() {
        return publicTransportCost;
    }

    public void setPublicTransportCost(double publicTransportCost) {
        this.publicTransportCost = publicTransportCost;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }

    public void setMilesPerGallon(double milesPerGallon) { this.milesPerGallon = milesPerGallon;}

    public double getMilesPerGallon() { return milesPerGallon;}
}

