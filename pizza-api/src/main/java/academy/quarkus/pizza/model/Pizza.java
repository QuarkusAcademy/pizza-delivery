package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;

@Entity
public class Pizza extends PanacheEntity {
    public String description;

    public Pizza() {
    }

    @Transactional()
    public static Pizza persist(String description) {
        var result = new Pizza();
        result.description = description;
        result.persist();
        return result;
    }

    @Override
    public String toString() {
        return description;
    }
}
