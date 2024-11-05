package academy.quarkus.pizza.rs;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("user")
public class UserResource {
    @Path("info")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Map<String, String> getUserInfo(){
        var result = Map.of("requires", "user");
        return result;
    }

    @Path("admin")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Map<String, String> getAdminInfo(){
        var result = Map.of("requires", "admin");
        return result;
    }

}
