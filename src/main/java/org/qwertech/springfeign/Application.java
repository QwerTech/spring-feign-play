package org.qwertech.springfeign;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@SpringBootApplication
@RestController
@EnableFeignClients
@RequiredArgsConstructor
public class Application {

  public static final String TEST = "/test";
  private final OtherServiceApi otherServiceApi;

  @GetMapping(TEST)
  @SneakyThrows
  public ResponseEntity<String> test() {
    return otherServiceApi.testGet();
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }


}
