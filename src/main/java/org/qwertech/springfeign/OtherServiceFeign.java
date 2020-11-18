package org.qwertech.springfeign;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("other-service")
public interface OtherServiceFeign extends OtherServiceApi {

}
