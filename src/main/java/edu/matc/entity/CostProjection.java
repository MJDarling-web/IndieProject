package edu.matc.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cost_projections")
public class CostProjection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private int logId;

    @ManyToOne
    @JoinColumn(name = "user_id"  )
    private User user; // Relationship to the User entity

    @Column(name = "transportation_mode"  )
    private String transportationMode; // Type of transportation (Car, Bike, Walk, etc.)

    @Column(name = "date_added"  )
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdded; // The date this projection was created

    @Column(name = "duration_in_minutes"  )
    private int durationInMinutes; // Duration of commute in minutes

    @Column(name = "distance_in_miles"  )
    private double distanceInMiles; // Distance of commute in miles

    // Getters and Setters
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTransportationMode() {
        return transportationMode;
    }

    public void setTransportationMode(String transportationMode) {
        this.transportationMode = transportationMode;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public double getDistanceInMiles() {
        return distanceInMiles;
    }

    public void setDistanceInMiles(double distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
    }
}
