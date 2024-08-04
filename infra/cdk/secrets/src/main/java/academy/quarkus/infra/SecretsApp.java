package academy.quarkus.infra;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@QuarkusMain
public class SecretsApp 
    implements QuarkusApplication {

    @Inject
    App app;

    public void init(@Observes StartupEvent event){
        new SecretsStack(app, "SecretsStack", StackProps.builder().build());
    }

    @Produces
    @ApplicationScoped
    public App produceApp(){
        App app = new App();
        return app;
    }

    @Override
    public int run(String... args) throws Exception {
        app.synth();
        return doExit(0);
    }

    private int doExit(int i) {
        System.exit(i);
        return i;
    }
    public static void main(final String[] args) {
        Quarkus.run(SecretsApp.class, args);
    }
}

