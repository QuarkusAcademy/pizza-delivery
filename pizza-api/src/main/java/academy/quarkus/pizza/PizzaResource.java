package academy.quarkus.pizza;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/pizza")
public class PizzaResource {

    @Transactional
    public void init(@Observes StartupEvent ev){
        var p1 = new Pizza("Marguerita");
        p1.persist();

        var p2 = new Pizza("Mushrooms");
        p2.persist();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pizza> getPizza() {
        List<Pizza> result =  Pizza.listAll();
        return result;
    }
}
