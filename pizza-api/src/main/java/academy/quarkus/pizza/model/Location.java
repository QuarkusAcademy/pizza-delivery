package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Location extends PanacheEntity {
    public String addressMain;
    public String addressComplement;
    public String city;

    public static Location current() {
        return new Location();
    }

    public Location(){}

    public static Location persist(String addressMain, String addressComplement, String city){
        var result = new Location();
        result.addressMain = addressMain;
        result.addressComplement = addressComplement;
        result.city = city;
        result.persist();
        return result;
    }
}
