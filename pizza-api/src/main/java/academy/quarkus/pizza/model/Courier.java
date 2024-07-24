package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.OneToOne;
import jakarta.transaction.Transactional;
import jakarta.persistence.Entity;

@Entity
public class Courier extends PanacheEntity {
    @OneToOne
    Person person;

    String license;

    @Transactional
    public static Courier persist(Long personId, String license) {
        var result = new Courier();
        result.person = Person.findById(personId);
        result.license = license;
        result.persist();
        return result;
    }

}
