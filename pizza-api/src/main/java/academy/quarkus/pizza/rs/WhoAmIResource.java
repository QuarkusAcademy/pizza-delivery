package academy.quarkus.pizza.rs;

import java.util.Map;
import io.quarkus.logging.Log;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("whoami")
public class WhoAmIResource {
    @Inject
    SecurityIdentity id;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> get(){
        var name = id.getPrincipal().getName();
        if (id.isAnonymous()){
            return Map.of("name", "anonymous");
        }
        var result = Map.of("name", name);
        Log.info(result);
        return result;
    }
}
