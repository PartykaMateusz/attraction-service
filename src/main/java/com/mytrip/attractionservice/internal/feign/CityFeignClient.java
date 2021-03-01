package com.mytrip.attractionservice.internal.feign;

import com.mytrip.attractionservice.internal.city.model.CityResponseList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "cities",
        url = "https://tripadvisor1.p.rapidapi.com")
public interface CityFeignClient {

    String APPLICATION_JSON = "application/json";

    @RequestMapping(value = "/locations/auto-complete",
            produces = APPLICATION_JSON,
            method = RequestMethod.GET)
    Optional<CityResponseList> getLocationByName(@RequestHeader(name = "x-rapidapi-key") String rapidApiKey,
                                                 @RequestParam(name = "query") String query);
}
