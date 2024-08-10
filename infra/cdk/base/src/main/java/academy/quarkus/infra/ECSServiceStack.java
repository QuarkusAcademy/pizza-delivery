package academy.quarkus.infra;

import java.util.List;
import java.util.Map;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ecs.AwsLogDriverProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ClusterAttributes;
import software.amazon.awscdk.services.ecs.ContainerDefinitionOptions;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.FargateService;
import software.amazon.awscdk.services.ecs.FargateTaskDefinition;
import software.amazon.awscdk.services.ecs.LoadBalancerTargetOptions;
import software.amazon.awscdk.services.ecs.LogDriver;
import software.amazon.awscdk.services.ecs.PortMapping;
import software.amazon.awscdk.services.ecs.RepositoryImageProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.AddApplicationTargetGroupsProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListener;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListenerAttributes;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationProtocol;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationTargetGroup;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.elasticloadbalancingv2.ListenerCondition;
import software.amazon.awscdk.services.elasticloadbalancingv2.TargetType;
import software.amazon.awscdk.services.iam.FromRoleArnOptions;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.ssm.StringParameter;
import software.constructs.Construct;

public class ECSServiceStack extends Stack {
    public ECSServiceStack(final Construct scope, final String id) {
        this(scope, id, null, 
        null, null,
        null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public ECSServiceStack(final Construct scope, 
        final String id, 
        final StackProps props,
        final NetworkStack networkStack,
        final DatabaseStack dbStack,
        final String clusterName,
        final Number cpu,
        final Number mem,
        final String roleArn,
        final String serviceId,
        final Number containerPort,
        final String listenerARN,
        final String listenerSGId,
        final String pathPattern,
        final Number priority,
        final String healthPath,
        final String imageName) {
        super(scope, id, props);

        var cluster = Cluster.fromClusterAttributes(this, "ECSCluster",
            ClusterAttributes.builder()
                .clusterName(clusterName)
                .vpc(networkStack.vpc)
                .build());
        
        var immutable = FromRoleArnOptions.builder().mutable(false).build();
        var executionRole = Role.fromRoleArn(this, "TaskExecutionRole", roleArn, immutable);        

        var taskDef = FargateTaskDefinition.Builder.create(this, "QuarkusTask")
            .cpu(cpu)
            .memoryLimitMiB(mem)
            .executionRole(executionRole)
            .build();
        
        var image = ContainerImage.fromRegistry(imageName,
            RepositoryImageProps.builder()
                .build());
        
        var logGroup = LogGroup.Builder.create(this, "LogGroup")
            .logGroupName(cluster.getClusterName() + "-logs")
            .removalPolicy(RemovalPolicy.DESTROY)
            .build();
            
        var logging = LogDriver.awsLogs(AwsLogDriverProps.builder()
            .logGroup(logGroup)
            .streamPrefix(serviceId)
            .build());

        var oidcProvider = getParameter("QUARKUS_OIDC_PROVIDER");
        var oidcClientId = getParameter("QUARKUS_OIDC_CLIENT_ID");
        var oidcSecret = getParameter("QUARKUS_OIDC_CREDENTIALS_SECRET");
        var dbSecret = dbStack.dbcluster.getSecret();
        var host = dbSecret.secretValueFromJson("host").unsafeUnwrap();
        var port = dbSecret.secretValueFromJson("port").unsafeUnwrap();
        var jdbcUsername = dbSecret.secretValueFromJson("username").unsafeUnwrap();
        var jdbcPassword = dbSecret.secretValueFromJson("password").unsafeUnwrap();
        var dbname = dbSecret.secretValueFromJson("dbname").unsafeUnwrap();
        var jdbcUrl = "jdbc:postgresql://" + host + ":"+port+"/"+dbname;

        var env = Map.of(
            "QUARKUS_OIDC_PROVIDER", oidcProvider,
            "QUARKUS_OIDC_CLIENT_ID", oidcClientId,
            "QUARKUS_OIDC_CREDENTIALS_SECRET", oidcSecret,
            "QUARKUS_DATASOURCE_JDBC_URL", jdbcUrl,
            "QUARKUS_DATASOURCE_USERNAME", jdbcUsername,
            "QUARKUS_DATASOURCE_PASSWORD", jdbcPassword
        );


        var containerDef = taskDef.addContainer(serviceId,
            ContainerDefinitionOptions
                .builder()
                .image(image)
                .logging(logging)
                .memoryLimitMiB(mem)
                .cpu(cpu)
                .environment(env)
                .build());

        containerDef.addPortMappings(
            PortMapping.builder()
                .containerPort(containerPort)
                .hostPort(containerPort)
                .build()
        );
    

        var vpcSubnets = SubnetSelection.builder()
            .subnetType(SubnetType.PUBLIC)
            .build();

        var serviceSG = SecurityGroup.Builder.create(this, "ServiceSG_" + serviceId)
                .vpc(networkStack.vpc)
                .allowAllOutbound(true)
                .description("Security Group for service " + serviceId)
                .build();

        serviceSG.addIngressRule(Peer.anyIpv4(), Port.tcp(containerPort), "Allow ingress on " + containerPort, false);            

        var service = FargateService.Builder.create(this, "QuarkusService")
            .cluster(cluster)
            .taskDefinition(taskDef)
            .desiredCount(1)
            .vpcSubnets(vpcSubnets)
            .securityGroups(List.of(serviceSG))
            .assignPublicIp(true)
            .build();

        var target = service.loadBalancerTarget(
            LoadBalancerTargetOptions.builder()
                .containerName(serviceId)
                .containerPort(containerPort)
                .build()
        );

        var targetGroup = ApplicationTargetGroup.Builder.create(this, "ServiceTargetGroup")
            .vpc(networkStack.vpc)
            .protocol(ApplicationProtocol.HTTP)
            .port(containerPort)
            .targetType(TargetType.IP)
            .targets(List.of(target))
            .build();
        
        
        var hc = HealthCheck.builder()
            .path(healthPath)
            .interval(Duration.seconds(30))
            .timeout(Duration.seconds(5))
            .healthyThresholdCount(3)
            .unhealthyThresholdCount(3)
            .build();

        targetGroup.configureHealthCheck(hc);

        var listenerSG = SecurityGroup.fromSecurityGroupId(this, "ImportedListenerSG", listenerSGId);

        var attrs = ApplicationListenerAttributes.builder()
            .listenerArn(listenerARN)
            .securityGroup(listenerSG)
            .build();

        var listener = ApplicationListener.fromApplicationListenerAttributes(
            this, "Listener_"+serviceId, attrs
        );


        var conditions = List.of(
            ListenerCondition.pathPatterns(List.of(pathPattern))
        );

        listener.addTargetGroups("TargetGroup_"+serviceId,
            AddApplicationTargetGroupsProps.builder()
                .targetGroups(List.of(targetGroup))
                .conditions(conditions)
                .priority(priority)
                .build());

    }

    private String getParameter(String string) {
        var val = StringParameter.valueFromLookup(this, string);
        return val;            
    }
}
