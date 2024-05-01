package academy.quarkus.pizza;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Pizza extends PanacheEntity {
    public String description;

    public Pizza() {
    }

    public Pizza(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
