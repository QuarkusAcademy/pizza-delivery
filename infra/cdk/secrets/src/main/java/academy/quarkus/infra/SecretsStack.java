package academy.quarkus.infra;

import org.eclipse.microprofile.config.ConfigProvider;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ssm.StringParameter;
import software.constructs.Construct;

public class SecretsStack extends Stack {
    public SecretsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public SecretsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        buildParameter("quarkus.oidc.provider");
        buildParameter("quarkus.oidc.client-id");
        buildParameter("quarkus.oidc.credentials.secret");
    }

    private StringParameter buildParameter(String id) {
        var resourceId = id
            .replaceAll("\\.","_")
            .replaceAll("\\-","_")
            .toUpperCase();

        var value = ConfigProvider.getConfig().getValue(id, String.class);

        var param = StringParameter.Builder.create(this, resourceId)
            .parameterName(resourceId)
            .stringValue(value)
            .build();

        return param;
    }
}
