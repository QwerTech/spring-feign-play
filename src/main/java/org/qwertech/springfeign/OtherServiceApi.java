package org.qwertech.springfeign;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface OtherServiceApi {

  String TEST_OTHER_SERVICE = "/test-other-service";

  @GetMapping(TEST_OTHER_SERVICE)
  ResponseEntity<String> testGet();

}
