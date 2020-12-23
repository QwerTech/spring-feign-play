package org.qwertech.springfeign;

import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryFactory;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.stereotype.Component;

@Component
public class ConfigurableRibbonLoadBalancedRetryFactory extends RibbonLoadBalancedRetryFactory {

  private final SpringClientFactory clientFactory;

  public ConfigurableRibbonLoadBalancedRetryFactory(SpringClientFactory clientFactory) {
    super(clientFactory);
    this.clientFactory = clientFactory;
  }

  @Override
  public LoadBalancedRetryPolicy createRetryPolicy(String service, ServiceInstanceChooser serviceInstanceChooser) {

    RibbonLoadBalancerContext lbContext = this.clientFactory.getLoadBalancerContext(service);
    return new ConfigurableRibbonLoadBalancedRetryPolicy(service, lbContext, serviceInstanceChooser, clientFactory.getClientConfig(service));
  }
}
