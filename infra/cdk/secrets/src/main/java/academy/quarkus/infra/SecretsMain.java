package academy.quarkus.infra;

import io.quarkus.runtime.Quarkus;

public class SecretsMain {
    public static void main(final String[] args) {
        Quarkus.run(SecretsApp.class, args);
    }
}
