package org.qwertech.springfeign;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.support.TestPropertySourceUtils;

@Component
public class WiremockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
        "wiremock.server.port=" + ApplicationTests.wireMockServer.port());
  }
}
