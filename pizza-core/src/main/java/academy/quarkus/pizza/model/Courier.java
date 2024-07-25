package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.OneToOne;
import jakarta.transaction.Transactional;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;

@Entity
public class Courier extends PanacheEntity {
    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    Person person;

    String license;

    public Courier(){}

    @Transactional
    public static Courier persist(Long personId, String license) {
        var result = new Courier();
        result.person = Person.findById(personId);
        result.license = license;
        result.persist();
        return result;
    }

}
