package academy.quarkus.pizza.rs;

import org.eclipse.microprofile.jwt.JsonWebToken;

import academy.quarkus.pizza.auth.UserDetails;
import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.vertx.mutiny.ext.auth.User;
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
    public UserDetails get(){
        var name = id.getPrincipal().getName();
        var result = new UserDetails(false, name);
        Log.info(result);
        return result;
    }
}
