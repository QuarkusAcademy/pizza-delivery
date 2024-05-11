package academy.quarkus.pizza.rs;

import academy.quarkus.pizza.model.*;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@QuarkusTest
public class PizzaResourceTest {
    @Inject
    PizzaResource pizzas;

    /**
     * Initial pizza order happy flow:
     * 1. Show menu of the nearest store
     * 2. Add pizza to cart
     * 3. Review cart before checkout
     * 4. Checkout
     * 5. Delivery
     * 6. Feedback
     */
    @Test
    public void testFindNearestStore(){
        // GIVEN
        var location = Location.current();
        // WHEN
        var store = Store.findNearest(location);
        // THEN
        assertNotNull(store);
        Log.infof(store.id + " " + store.name);
    }

    @Test
    public void testAddToTicket(){
        // GIVEN
        var store = Store.persist("Test Shack", "__test__");

        var trad = Category.persist(store, "Traditional", "10.99");
        var marg = Pizza.persist("Marguerita");
        var mush = Pizza.persist("Mushrooms");
        trad.addPizzas(marg, mush);
        var julio = Person.persist("Julio", "julio@caravana.cloud",  "+5 (55) 5555-5555");

        // WHEN
        var ticket = Ticket.persist(julio, "Av Mofarrej 1500", "ap 94E", "+5 (55) 5555-5555");
        ticket.addItem(marg, trad.price, 2);
        ticket.addItem(mush, trad.price, 1);
        var ticketValue = ticket.getValue();

        // THEN
        var expectedValue = new BigDecimal("32.97");
        assertEquals(expectedValue, ticketValue);
    }

}
