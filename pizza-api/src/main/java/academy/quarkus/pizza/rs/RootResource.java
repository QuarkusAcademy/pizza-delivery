package academy.quarkus.pizza.rs;

import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("")
public class RootResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> get(){
        return Map.of("message", "Welcome to our Pizza Delivery API!");
    }
}
