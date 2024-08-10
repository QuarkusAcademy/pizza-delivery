package academy.quarkus.infra;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

@QuarkusMain
public class BaseApp 
    implements QuarkusApplication {

    @Inject
    App app;

    @ConfigProperty(name="certificate.arn")
    String certificateARN;

    @ConfigProperty(name="domain.name")
    String domainName;

    @ConfigProperty(name="alias.name")
    String aliasName;

    @SuppressWarnings("unused")
    public void init(@Observes StartupEvent event){
        var stackProps = StackProps.builder()
            .env(Environment.builder()
                .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                .region(System.getenv("CDK_DEFAULT_REGION"))
                .build())
            .build();

        var networkStack = new NetworkStack(app, "NetworkStack", stackProps);
        var dbStack = new DatabaseStack(app, "DatabaseStack", stackProps, networkStack);
        var clusterStack = new ECSClusterStack(app, "ECSClusterStack", stackProps, networkStack, certificateARN, domainName, aliasName);
        var serviceStack = new ECSServiceStack(
            app, "ECSServiceStack", stackProps, 
            networkStack, dbStack, 
            clusterStack.cluster.getClusterName(),
            512,
            1024,
            clusterStack.executionRole.getRoleArn(),
            "pizza-api",
            8080,
            clusterStack.listener.getListenerArn(), 
            clusterStack.albSG.getSecurityGroupId(),
            "/api/*",
            999,
            "/api/",
            "caravanacloud/pizza-api:latest");
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
        Quarkus.run(BaseApp.class, args);
    }
}

