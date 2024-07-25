package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;

@Entity
public class Person extends PanacheEntity {
    String name;
    String email; 
    String phone;

    public Person(){}

    @Transactional
    public static Person persist(String name, String email, String phone) {
        var result = new Person();
        result.name = name;
        result.email = email;
        result.phone = phone;
        result.persist();
        return result;
    }

}
