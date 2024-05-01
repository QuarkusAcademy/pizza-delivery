package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Store extends PanacheEntity {

    public String name;
    public String code;

    public Store() {}

    public static Store findNearest(Location loc){
        Store store = Store.find("code", "__default__").firstResult();
        return store;
    }
}
