package academy.quarkus.pizza.sec;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.control.ActivateRequestContext;

import java.util.function.Supplier;

@Dependent
class SecurityIdentitySupplier implements Supplier<SecurityIdentity> {
    private SecurityIdentity identity;

    @Override
    @ActivateRequestContext
    public SecurityIdentity get() {
        if (identity.isAnonymous()){
            return identity;
        }
        var principal = identity.getPrincipal();
        var email = (String) null;
        if (principal instanceof DefaultJWTCallerPrincipal djcp) {
            email = djcp.getClaim("email");
        }
        if (email == null) {
            //TODO: Handle identities without email
            return identity;
        }
        var builder = QuarkusSecurityIdentity.builder(identity);
        // var roles = authService.getRolesByEmail(email);
        // roles.stream().forEach(role -> builder.addRole(role));
        var result = builder.build();
        return result;
    }

    public void setIdentity(SecurityIdentity identity) {
        this.identity = identity;
    }
}