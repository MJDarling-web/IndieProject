package entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import java.util.Date;

/**
 * Represents an application user, with associated commuting logs, cost analyses,
 * and transportation profiles. Password is managed by Cognito, not stored here.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // One-to-many relationships:
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommutingLog> commutingLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CostAnalysis> costAnalyses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TransportationProfile> transportationProfiles = new ArrayList<>();

    /**
     * No-argument constructor for JPA
     */
    public User() {
    }

    /**
     * Constructor without password, as authentication is handled externally via Cognito.
     */
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CommutingLog> getCommutingLogs() {
        return commutingLogs;
    }

    public void setCommutingLogs(List<CommutingLog> commutingLogs) {
        this.commutingLogs = commutingLogs;
    }

    public List<CostAnalysis> getCostAnalyses() {
        return costAnalyses;
    }

    public void setCostAnalyses(List<CostAnalysis> costAnalyses) {
        this.costAnalyses = costAnalyses;
    }

    public List<TransportationProfile> getTransportationProfiles() {
        return transportationProfiles;
    }

    public void setTransportationProfiles(List<TransportationProfile> transportationProfiles) {
        this.transportationProfiles = transportationProfiles;
    }

    @Override
    public String toString() {
        return "User{id=" + id
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", email='" + email + '\''
                + '}';
    }
}


