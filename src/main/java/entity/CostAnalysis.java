package entity;

import javax.persistence.*;

@Entity
@Table(name = "cost_analyses")
public class CostAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private int analysisId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Relationship to the User entity

    @Column(name = "commute_type" )
    private String commuteType; // Type of transportation (Car, Bike, Walk, Public Transit, etc.)

    @Column(name = "one_year_cost" )
    private double oneYearCost; // 1-year cost projection

    @Column(name = "two_year_cost" )
    private double twoYearCost; // 2-year cost projection

    @Column(name = "five_year_cost" )
    private double fiveYearCost; // 5-year cost projection

    @Column(name = "total_cost" )
    private double totalCost; // Total projected cost across all time periods

    // Getters and Setters
    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommuteType() {
        return commuteType;
    }

    public void setCommuteType(String commuteType) {
        this.commuteType = commuteType;
    }

    public double getOneYearCost() {
        return oneYearCost;
    }

    public void setOneYearCost(double oneYearCost) {
        this.oneYearCost = oneYearCost;
    }

    public double getTwoYearCost() {
        return twoYearCost;
    }

    public void setTwoYearCost(double twoYearCost) {
        this.twoYearCost = twoYearCost;
    }

    public double getFiveYearCost() {
        return fiveYearCost;
    }

    public void setFiveYearCost(double fiveYearCost) {
        this.fiveYearCost = fiveYearCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "CostAnalysis{" +
                "analysisId=" + analysisId +
                ", user=" + user.getId() + // Assuming User entity has getId() method
                ", commuteType='" + commuteType + '\'' +
                ", oneYearCost=" + oneYearCost +
                ", twoYearCost=" + twoYearCost +
                ", fiveYearCost=" + fiveYearCost +
                ", totalCost=" + totalCost +
                '}';
    }
}
