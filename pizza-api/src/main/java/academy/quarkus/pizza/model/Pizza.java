package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.*;

import java.util.List;

@Entity

public class Pizza extends PanacheEntity {
    public String description;

    public Pizza() {
    }

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
