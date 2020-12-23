package org.qwertech.springfeign;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.lessThan;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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
  void ipAddressForwardedToExternalService() {
    String ip = "ip";
    stubFor(get(OtherServiceApi.TEST_OTHER_SERVICE).willReturn(ok("result")));

    RestAssured.given().header(RequestUtils.X_FORWARDED_FOR_HTTP_HEADER, ip).get(Application.TEST).then().statusCode(200);

    verify(1, getRequestedFor(urlEqualTo(OtherServiceApi.TEST_OTHER_SERVICE))
        .withHeader(RequestUtils.X_FORWARDED_FOR_HTTP_HEADER, containing(ip)));
  }

  @Test
  void getRequestRetries() {
    stubFor(get(OtherServiceApi.TEST_OTHER_SERVICE).willReturn(WireMock.serviceUnavailable()));

    RestAssured.given().get(Application.TEST);

    verify(3, getRequestedFor(urlEqualTo(OtherServiceApi.TEST_OTHER_SERVICE)));
  }

  @Test
  void hystrixMetrics() {
    stubFor(get(OtherServiceApi.TEST_OTHER_SERVICE).willReturn(WireMock.ok()));

    RestAssured.given().get(Application.TEST);
    final String metrics = RestAssured.given().get("/actuator/prometheus").getBody().asString();

    Assertions.assertThat(metrics)
        .contains("hystrix_execution_total",
            "hystrix_latency_total_seconds_count", "hystrix_latency_total_seconds_sum", "hystrix_latency_total_seconds_max",
            "hystrix_latency_execution_seconds_count", "hystrix_latency_execution_seconds_sum", "hystrix_latency_execution_seconds_max",
            "hystrix_circuit_breaker_open{group=\"other-service\",key=\"OtherServiceFeign#testGet()\",}");
  }

  @Test
  void hystrixBreaksCircuit() {
    stubFor(post(OtherServiceApi.TEST_OTHER_SERVICE).willReturn(WireMock.serviceUnavailable()));
    final int requests = 1000;

    IntStream.range(0, requests)
        .parallel()
        .forEach(i -> RestAssured.given().post(Application.TEST));

    verify(lessThan(requests), postRequestedFor(urlEqualTo(OtherServiceApi.TEST_OTHER_SERVICE)));
  }

  @Test
  void postRequestRetries() {
    stubFor(post(OtherServiceApi.TEST_OTHER_SERVICE).willReturn(WireMock.serviceUnavailable()));

    RestAssured.given().post(Application.TEST);

    verify(1, postRequestedFor(urlEqualTo(OtherServiceApi.TEST_OTHER_SERVICE)));
  }

  @Test
  void putRequestRetries() {
    stubFor(put(OtherServiceApi.TEST_OTHER_SERVICE).willReturn(WireMock.serviceUnavailable()));

    RestAssured.given().put(Application.TEST);

    verify(3, putRequestedFor(urlEqualTo(OtherServiceApi.TEST_OTHER_SERVICE)));
  }

  @Test
  void deleteRequestRetries() {
    stubFor(delete(OtherServiceApi.TEST_OTHER_SERVICE).willReturn(WireMock.serviceUnavailable()));

    RestAssured.given().delete(Application.TEST);

    verify(1, deleteRequestedFor(urlEqualTo(OtherServiceApi.TEST_OTHER_SERVICE)));
  }

}
