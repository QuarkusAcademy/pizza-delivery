package academy.quarkus.pizza;

import academy.quarkus.pizza.model.Location;
import academy.quarkus.pizza.model.Pizza;
import academy.quarkus.pizza.model.Store;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@QuarkusTest
public class PizzaTest {
    @Test
    public void testFindNearestStore(){
        // GIVEN
        var location = Location.current();
        // WHEN
        var store = Store.findNearest(location);
        // THEN
        assertNotNull(store);
    }
}
