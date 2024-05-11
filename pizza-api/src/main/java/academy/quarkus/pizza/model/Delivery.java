package academy.quarkus.pizza.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

@Entity
@NamedQueries({
        @NamedQuery(name = "Delivery.updateLocation",
                query = """
                    update Delivery d
                        set d.currentLocation = :location
                        where d.id = :id
    """),
        @NamedQuery(name = "Delivery.updateRating",
                query = """
                    update Delivery d
                        set d.rating = :rating
                        where d.id = :id
    """)
})
public class Delivery extends PanacheEntity {
    @ManyToOne
    public Ticket ticket;

    @ManyToOne
    public Store store;

    @ManyToOne
    public Courier courier;

    @Embedded
    public Location currentLocation;

    public Integer rating;

    public Delivery(){}

    @Transactional
    public static Delivery persist(Long ticketId, Long storeId, Long courierId){
        Ticket ticket = Ticket.findById(ticketId);
        Store store = Store.findById(storeId);
        Courier courier = Courier.findById(courierId);
        Delivery result = new Delivery();
        result.ticket = ticket;
        result.store = store;
        result.courier = courier;
        result.persist();
        return result;
    }

    @Transactional
    public static void updateLocation(Long deliveryId, Double lat, Double lon){
        Location loc = new Location(lat, lon);
        Delivery.update(
                "#Delivery.updateLocation",
                Parameters.with("location", loc)
                        .and("id", deliveryId));
    }

    @Transactional
    public static void updateRating(Long deliveryId, Integer rating){
        Delivery.update(
                "#Delivery.updateRating",
                Parameters.with("rating", rating)
                        .and("id", deliveryId));
    }
}
