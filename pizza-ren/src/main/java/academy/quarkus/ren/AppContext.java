package academy.quarkus.ren;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class AppContext {
    @Inject
    public AppConfig config;

    public String getTitle(){
        return "Pizza!";
    }
}
