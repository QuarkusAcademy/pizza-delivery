package academy.quarkus.pizza.rs.user;

import io.quarkus.logging.Log;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("/user/whoami")
public class WhoAmIResource {
    @Inject
    SecurityIdentity identity;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    // export access_token=$(curl -s -X POST http://localhost:8888/realms/quarkus/protocol/openid-connect/token --user quarkus-app:secret -H 'content-type: application/x-www-form-urlencoded' -d 'username=alice&password=alice&grant_type=password' | jq -r '.access_token')
    // echo $access_token
    // curl -s  http://localhost:8080/api/user/whoami  -H "Authorization: Bearer "$access_token | jq
    public Map<String, String> getWhoAmI(){
        var name = "anonymous";
        if (! identity.isAnonymous()){
            name = identity.getPrincipal().getName();
        }
        Map<String,String> result = Map.of(
                "name", name
        );
        Log.infof("WhoAmI() = %s", result);
        return result;
    }
}
