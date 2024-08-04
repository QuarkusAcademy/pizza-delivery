package academy.quarkus.infra;

import java.util.List;

import org.eclipse.microprofile.config.ConfigProvider;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.DefaultInstanceTenancy;
import software.amazon.awscdk.services.ec2.IpAddresses;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ssm.StringParameter;
import software.constructs.Construct;

public class NetworkStack extends Stack {
    public NetworkStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public NetworkStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        var isolatedSubnets = SubnetConfiguration.builder()
            .subnetType(SubnetType.PRIVATE_ISOLATED)
            .name("IsolatedSubnet")
            .cidrMask(24)
            .build();
        
        var privateSubnets = SubnetConfiguration.builder()
            .subnetType(SubnetType.PRIVATE_WITH_EGRESS)
            .name("PrivateSubnet")
            .cidrMask(24)
            .build();

        var publicSubnets = SubnetConfiguration.builder()
            .subnetType(SubnetType.PUBLIC)
            .name("PublicSubnet")
            .cidrMask(24)
            .mapPublicIpOnLaunch(true)
            .build();

        var addr = IpAddresses.cidr("10.0.0.0/16");

        var azs = Stack.of(this).getAvailabilityZones();
        if (azs.size() < 2) {
            throw new RuntimeException("Need at least 2 availability zones");
        }
        azs = azs.subList(0, 2);

        var vpc = Vpc.Builder.create(this, "VPC")
            .subnetConfiguration(List.of(
                isolatedSubnets,
                privateSubnets,
                publicSubnets
            ))
            .ipAddresses(addr)
            .defaultInstanceTenancy(DefaultInstanceTenancy.DEFAULT)
            .enableDnsHostnames(true)
            .enableDnsSupport(true)
            .natGateways(1)
            .availabilityZones(azs)
            .build();
    }

    

}
