package academy.quarkus.pizza.init;

import academy.quarkus.pizza.model.*;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

public class SampleDataInit {
    @Inject
    LaunchMode mode;

    @Transactional
    public void init(@Observes StartupEvent ev){
        if (LaunchMode.NORMAL.equals(mode))
            return;
        var store = Store.persist("Pizza Shack", "__default__");

        /*
        var trad = Category.persist(store, "Traditional", "10.99");
        var marg = Pizza.persist("Marguerita");
        var mush = Pizza.persist("Mushrooms");
        trad.addPizzas(marg, mush);

        var premium = Category.persist(store, "Premium", "14.99");
        var cheeses = Pizza.persist("4 Cheeses");
        var veggies = Pizza.persist("Vegetables");
        var napoles = Pizza.persist("Napoletana");
        premium.addPizzas(cheeses, veggies, napoles);

        var winona = Person.persist("Winona Courier", "cool@girl.movie", "+2222222222222");
        var courier = Courier.persist(winona.id, "123467890");

        var ticket = Ticket.persist(winona, "Rabbit Hole 1", "Tea Room", "+33333333");
        var delivery = Delivery.persist(store.id, ticket.id, winona.id);
        delivery.updateLocation(delivery.id, 0.0, 0.0);
        */
    }
}
