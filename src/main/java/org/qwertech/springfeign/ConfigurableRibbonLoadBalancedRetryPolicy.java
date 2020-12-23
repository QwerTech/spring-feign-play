package org.qwertech.springfeign;

import com.netflix.client.config.IClientConfig;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryContext;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryPolicy;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;



public class ConfigurableRibbonLoadBalancedRetryPolicy extends RibbonLoadBalancedRetryPolicy {

  public ConfigurableRibbonLoadBalancedRetryPolicy(String serviceId, RibbonLoadBalancerContext context,
      ServiceInstanceChooser loadBalanceChooser, IClientConfig clientConfig) {
    super(serviceId, context, loadBalanceChooser, clientConfig);
  }

  @Override
  public boolean canRetry(LoadBalancedRetryContext context) {
    HttpMethod method = context.getRequest().getMethod();
    return super.canRetry(context) ||HttpMethod.PUT == method;
  }
}
