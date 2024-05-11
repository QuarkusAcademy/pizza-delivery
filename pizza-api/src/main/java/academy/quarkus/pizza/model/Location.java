package academy.quarkus.pizza.model;

import io.quarkus.logging.Log;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

@Embeddable
public class Location {
    public Double lat;
    public Double lon;

    public Location(){}
    public Location(Double lat, Double lon){
        this.lat = lat;
        this.lon = lon;
    }

    public static Location current() {
        return new Location();
    }
}
