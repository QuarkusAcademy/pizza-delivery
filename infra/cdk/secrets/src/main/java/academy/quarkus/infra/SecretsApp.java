package academy.quarkus.infra;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class SecretsApp {
    public static void main(final String[] args) {
        App app = new App();
        new SecretsStack(app, "SecretsStack", StackProps.builder().build());
        app.synth();
    }
}

