package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Ticket extends PanacheEntity {
    Person customer;
    String address1; 
    String address2; 
    String phone;

    public static Ticket persist(Person customer, 
        String address1, 
        String address2, 
        String phone) {
        var result = new Ticket();
        result.customer = customer;
        result.address1 = address1;
        result.address2 = address2;
        result.phone = phone;
        return result;
    }


}
