package org.qwertech.springfeign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * RequestInterceptor for {@see FeignClient} which sends requester ip address further
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClientIpPropagateFeignRequestInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate requestTemplate) {
    String clientIp = RequestUtils.getClientIp();
    if (!Objects.isNull(clientIp)) {
      requestTemplate.header(RequestUtils.X_FORWARDED_FOR_HTTP_HEADER, clientIp);
    }
  }

}