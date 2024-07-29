package academy.quarkus.vaadin;

import org.atmosphere.config.service.Get;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import academy.quarkus.pizza.auth.UserDetails;
import io.quarkus.oidc.token.propagation.AccessToken;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@RegisterRestClient(configKey = "whoami")
@AccessToken
public interface WhoAmIClient {
    @GET
    UserDetails get();
}
