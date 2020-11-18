package org.qwertech.springfeign;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

@Slf4j
@ContextConfiguration(classes = {Application.class}, initializers = WiremockInitializer.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTests {

  @LocalServerPort
  protected int port;

  @BeforeEach
  public final void initIT() {
    RestAssured.port = port;
  }

  public static WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());

  static {
    wireMockServer.start();
    int wmPort = wireMockServer.port();
    log.info("WireMock started on port {}", wmPort);
    WireMock.configureFor(wmPort);
  }

  @AfterEach
  public final void resetMocks() {
    Mockito.reset();
    WireMock.reset();
  }

  @Test
  void contextLoads() {
    String ip = "ip";
    stubFor(get(OtherServiceApi.TEST_OTHER_SERVICE).willReturn(ok("result")));

    RestAssured.given().header(RequestUtils.X_FORWARDED_FOR_HTTP_HEADER, ip).get(Application.TEST).then().statusCode(200);

    verify(1, getRequestedFor(urlEqualTo(OtherServiceApi.TEST_OTHER_SERVICE))
        .withHeader(RequestUtils.X_FORWARDED_FOR_HTTP_HEADER, containing(ip)));
  }

}
