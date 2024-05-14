package academy.quarkus.ren;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import java.util.Optional;

@Dependent
public class CtrlContext {
    @Inject
    PizzaConfig config;

    public String getTitleHead(){
        return config.titleHead();
    }

    public String getTitleTail(){
        return config.titleTail();
    }

    public String getGoogleMapsKey(){
        return config.googleMapsKey().orElse("");
    }
}
