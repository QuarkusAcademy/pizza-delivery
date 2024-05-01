package academy.quarkus.pizza;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@QuarkusTest
public class PizzaTest {
    @Inject
    PizzaResource pizzaResource;

    @Test
    public void testGetPizzas(){
        List<Pizza> ps = pizzaResource.getPizzas();
        assertFalse(ps.isEmpty());
    }
}
