package entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//TODO javadocs
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuelPrice {
    @JsonProperty("regular")
    private String regular;

    @JsonProperty("premium")
    private String premium;

    @JsonProperty("diesel")
    private String diesel;

    // Getters and Setters
    public String getRegular() { return regular; }
    public String getPremium() { return premium; }
    public String getDiesel() { return diesel; }

    public void setRegular(String regular) { this.regular = regular; }
    public void setPremium(String premium) { this.premium = premium; }
    public void setDiesel(String diesel) { this.diesel = diesel; }

    @Override
    public String toString() {
        return String.format("Regular: $%s, Premium: $%s, Diesel: $%s",
                regular, premium, diesel);
    }
    /**
    // TODO update FuelPrice entity to be able to host all of these fuel types from the api was Response entity but not being used
    private String lpg;
    private String e85;
    private String electric;
    private String midgrade;
    private String cng;
 **/

}