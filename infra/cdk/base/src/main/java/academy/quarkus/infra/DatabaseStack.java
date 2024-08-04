package academy.quarkus.infra;

import java.util.List;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.rds.AuroraPostgresClusterEngineProps;
import software.amazon.awscdk.services.rds.AuroraPostgresEngineVersion;
import software.amazon.awscdk.services.rds.ClusterInstance;
import software.amazon.awscdk.services.rds.Credentials;
import software.amazon.awscdk.services.rds.DatabaseCluster;
import software.amazon.awscdk.services.rds.DatabaseClusterEngine;
import software.amazon.awscdk.services.rds.DatabaseSecret;
import software.amazon.awscdk.services.rds.SubnetGroup;
import software.constructs.Construct;

public class DatabaseStack extends Stack {
    public DatabaseStack(final Construct scope, final String id) {
        this(scope, id, null, null);
    }

    public DatabaseStack(final Construct scope, 
        final String id, 
        final StackProps props,
        final NetworkStack networkStack) {
        super(scope, id, props);
        var auroraProps = AuroraPostgresClusterEngineProps
            .builder()
            .version(AuroraPostgresEngineVersion.VER_14_11)
            .build();   
        var engine = DatabaseClusterEngine.auroraPostgres(auroraProps);

        var isolatedSubnets = SubnetSelection.builder()
            .subnetType(SubnetType.PRIVATE_ISOLATED)
            .build();

        var subnetGroup = SubnetGroup.Builder.create(this, "IsolatedSubnetGroup")
            .vpc(networkStack.vpc)
            .vpcSubnets(isolatedSubnets)
            .description("Isolated DB Subnet Group")
            .build();

        var dbSG = SecurityGroup.Builder.create(this, "DatabaseSecurityGroup")
            .vpc(networkStack.vpc)
            .allowAllOutbound(true)
            .build();
        dbSG.addIngressRule(Peer.anyIpv4(), Port.tcp(5432), "Allow PG");

        var writer = ClusterInstance.serverlessV2("writer-sls");

        var secret = DatabaseSecret.Builder.create(this, "DBSecret")
            .username("clusteradmin")
            .build();

        var credentials = Credentials.fromSecret(secret);

        var dbcluster = DatabaseCluster.Builder.create(this, "DatabaseCluster")
            .engine(engine)
            .defaultDatabaseName("pizzadb")
            .vpc(networkStack.vpc)
            .subnetGroup(subnetGroup)
            .securityGroups(List.of(dbSG))
            .serverlessV2MinCapacity(0.5)
            .serverlessV2MaxCapacity(8.0)
            .writer(writer)
            .readers(List.of())
            .credentials(credentials)
            .build();
        
            var dbSecret = dbcluster.getSecret();
            var secretARN = dbSecret.getSecretArn();
            var dbSecretARN = CfnOutput.Builder.create(this, "DBSecretARN")
                .value(secretARN)
                .build();
    }
}
