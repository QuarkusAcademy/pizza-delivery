package academy.quarkus.pizza.sec;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;

import java.util.function.Supplier;

import org.eclipse.microprofile.jwt.JsonWebToken;

@Dependent
class SecurityIdentitySupplier implements Supplier<SecurityIdentity> {
    private SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Override
    @ActivateRequestContext
    public SecurityIdentity get() {
        if (identity.isAnonymous()){
            return identity;
        }
        var builder = QuarkusSecurityIdentity.builder(identity);
        builder.addRole("user");

        var email = (String) token.getClaim("email");
        if (email != null && email.endsWith("@faermanj.com")) {
            builder.addRole("admin");
        }
        var result = builder.build();
        return result;
    }

    public void setIdentity(SecurityIdentity identity) {
        this.identity = identity;
    }
}