package academy.quarkus.infra;

import java.util.Arrays;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.ICertificate;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ICluster;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationLoadBalancer;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.RoleProps;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.constructs.Construct;

public class ECSClusterStack extends Stack {
    ICluster cluster;
    ApplicationLoadBalancer alb;
    Role executionRole;

    public ECSClusterStack(final Construct scope, final String id) {
        this(scope, id, null, null, null);
    }

    public ECSClusterStack(final Construct scope, 
        final String id, 
        final StackProps props,
        final NetworkStack networkStack,
        final String certificateARN) {
        super(scope, id, props);

        executionRole = new Role(this, "EcsTaskExecutionRole",
            RoleProps.builder()
                .assumedBy(new ServicePrincipal("ecs-tasks.amazonaws.com"))
                .build());
        
        executionRole.addToPolicy(PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .actions(Arrays.asList(
                        "logs:*", 
                        "secretsmanager:*",
                        "iam:*",
                        "ssm:*",
                        "ecr:*",
                        "ecr-public:*",
                        "rds:*",
                        "dynamodb:*"))
                .resources(Arrays.asList("*"))
                .build());
        
        cluster = Cluster.Builder.create(this, "ECSCluster")
                .vpc(networkStack.vpc)
                .build();
        
        var albSG = SecurityGroup.Builder.create(this, "ALBSecurityGroup")
                .vpc(networkStack.vpc)
                .allowAllOutbound(true)
                .build();
        albSG.addIngressRule(Peer.anyIpv4(), Port.tcp(443), "Allow HTTPS traffic");
        //TODO: Debug only, remove this later
        albSG.addIngressRule(Peer.anyIpv4(), Port.tcp(8080), "Allow HTTP traffic");

        alb = ApplicationLoadBalancer.Builder.create(this, "ALB")
                .vpc(networkStack.vpc)
                .internetFacing(true)
                .securityGroup(albSG)
                .build();
        
        ICertificate cert = Certificate.fromCertificateArn(this, "HTTPSCertificate", certificateARN);
        
        var arnOut = cert.getCertificateArn();
        CfnOutput.Builder.create(this, "HTTPSCertificateARN").value(arnOut).build();
        //TODO: Record Name
    }
}
