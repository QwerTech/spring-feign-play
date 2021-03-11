package org.qwertech.springfeign;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Slf4j
@SpringBootApplication
@RestController
@EnableFeignClients
@RequiredArgsConstructor
@EnableCircuitBreaker
public class Application {

  public static final String TEST = "/test";
  private final OtherServiceApi otherServiceApi;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommonsRequestLoggingFilter requestLoggingFilter() {
    CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
//    loggingFilter.setIncludeClientInfo(true);
    loggingFilter.setIncludeQueryString(true);
//    loggingFilter.setIncludePayload(true);
//    loggingFilter.setMaxPayloadLength(64000);
//    loggingFilter.setIncludeHeaders(true);
    return loggingFilter;
  }

  @GetMapping(TEST)
  public ResponseEntity<String> testGet() {
    return otherServiceApi.testGet();
  }

  @PutMapping(TEST)
  public ResponseEntity<String> testPut() {
    return otherServiceApi.testPut();
  }

  @PostMapping(TEST)
  public ResponseEntity<String> testPost() {
    return otherServiceApi.testPost();
  }

  @DeleteMapping(TEST)
  public ResponseEntity<String> testDelete() {
    return otherServiceApi.testDelete();
  }


}
