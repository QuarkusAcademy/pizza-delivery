package academy.quarkus.ren;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "app")
@StaticInitSafe
public interface AppConfig {
    @WithDefault("Delicious!")
    String slogan();

    String googleMapsApiKey();
}
