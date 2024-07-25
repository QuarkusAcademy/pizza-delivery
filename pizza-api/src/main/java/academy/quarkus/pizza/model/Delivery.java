package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.transaction.Transactional;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Embedded;


@Entity
public class Delivery extends PanacheEntity {
    @ManyToOne
    Store store;

    @OneToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    Person courier;

    @Embedded
    Location location;

    public Delivery(){}

    @Transactional
    public static Delivery persist(Long storeId, Long ticketId, Long courierId) {
        var result = new Delivery();
        result.courier = Person.findById(courierId);
        result.ticket = Ticket.findById(ticketId);
        result.store = Store.findById(storeId);
        result.persist();
        return result;     
    }

    public void updateLocation(Long id, Double lat, Double lon) {
        Delivery delivery = Delivery.findById(id);
        delivery.location = new Location(lat, lon);
        delivery.persist();    
    }

}
