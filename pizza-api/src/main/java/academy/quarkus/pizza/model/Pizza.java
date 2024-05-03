package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Pizza extends PanacheEntity {
    public String description;

    public Pizza() {
    }

    public static Pizza persist(String description) {
        var result = new Pizza();
        result.description = description;
        return result;
    }

    @Override
    public String toString() {
        return description;
    }
}
