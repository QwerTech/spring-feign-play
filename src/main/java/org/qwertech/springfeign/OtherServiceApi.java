package org.qwertech.springfeign;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

public interface OtherServiceApi {

  String TEST_OTHER_SERVICE = "/test-other-service";

  @GetMapping(TEST_OTHER_SERVICE)
  ResponseEntity<String> testGet();

  @PutMapping(TEST_OTHER_SERVICE)
  ResponseEntity<String> testPut();

  @PostMapping(TEST_OTHER_SERVICE)
  ResponseEntity<String> testPost();

  @DeleteMapping(TEST_OTHER_SERVICE)
  ResponseEntity<String> testDelete();
}
