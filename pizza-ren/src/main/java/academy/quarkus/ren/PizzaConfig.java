package academy.quarkus.ren;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.Optional;

@ConfigMapping(prefix = "pizza")
@StaticInitSafe
public interface PizzaConfig {
    @WithDefault("!Pizza!")
    String titleHead();
    @WithDefault("!Delicious!")
    String titleTail();

    Optional<String> googleMapsKey();
}
