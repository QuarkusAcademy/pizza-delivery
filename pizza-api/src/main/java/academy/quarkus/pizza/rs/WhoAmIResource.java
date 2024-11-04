package academy.quarkus.pizza.rs;

import java.util.Map;
import io.quarkus.logging.Log;
import io.quarkus.oidc.UserInfo;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("whoami")
public class WhoAmIResource {
    @Inject
    SecurityIdentity id;

    @Inject
    UserInfo info;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Map<String, String> get(){
        if (id.isAnonymous()){
            return Map.of("name", "anonymous");
        }
        var name = info.getName();
        var email = info.getEmail();
        var result = Map.of(
            "name", name, 
            "email", email);
        Log.info(result);
        return result;
    }
}
