package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.transaction.Transactional;

@Entity
public class Courier extends PanacheEntity {
    @ManyToOne
    public Person person;

    @Column(nullable = false)
    public String registration;

    public Courier(){}

    @Transactional
    public static Courier persist(Long personId, String registration){
        Person person = Person.findById(personId);
        Courier result = new Courier();
        result.person = person;
        result.registration = registration;
        result.persist();
        return result;
    }

}
