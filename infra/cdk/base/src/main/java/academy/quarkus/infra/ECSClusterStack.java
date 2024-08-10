package academy.quarkus.infra;

import java.util.Arrays;
import java.util.List;

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
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListener;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationLoadBalancer;
import software.amazon.awscdk.services.elasticloadbalancingv2.FixedResponseOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.ListenerAction;
import software.amazon.awscdk.services.elasticloadbalancingv2.ListenerCertificate;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.RoleProps;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.route53.ARecord;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.HostedZoneProviderProps;
import software.amazon.awscdk.services.route53.RecordTarget;
import software.amazon.awscdk.services.route53.targets.LoadBalancerTarget;
import software.constructs.Construct;

public class ECSClusterStack extends Stack {
    ICluster cluster;
    ApplicationLoadBalancer alb;
    Role executionRole;
    ApplicationListener listener;
    SecurityGroup albSG;

    public ECSClusterStack(final Construct scope, final String id) {
        this(scope, id, null, null, null, null, null);
    }

    public ECSClusterStack(final Construct scope, 
        final String id, 
        final StackProps props,
        final NetworkStack networkStack,
        final String certificateARN,
        final String domainName,
        final String aliasName) {
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
                .enableFargateCapacityProviders(true)
                .build();
        
        albSG = SecurityGroup.Builder.create(this, "ALBSecurityGroup")
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

        var listenerCert = ListenerCertificate.fromCertificateManager(cert);

        var defaultAction = ListenerAction.fixedResponse(503, 
                FixedResponseOptions.builder()
                    .contentType("text/plain")
                    .messageBody("Service Unavailable, please retry later")
                    .build());

        listener = ApplicationListener.Builder.create(this, "HTTPSListener")
                .loadBalancer(alb)
                .open(true)
                .port(443)
                .certificates(List.of(listenerCert))
                .defaultAction(defaultAction)
                .build();

        var hostedZone = HostedZone.fromLookup(this, "HostedZone", HostedZoneProviderProps.builder()
                
                .domainName(domainName)
                .build());
        
        
        var recordName = aliasName + "." + domainName;

        var aliasRecord = ARecord.Builder.create(this, "AliasRecord")
                .zone(hostedZone)
                .recordName(recordName)
                .target(RecordTarget.fromAlias(new LoadBalancerTarget(alb)))
                .build();
        
        }
}
