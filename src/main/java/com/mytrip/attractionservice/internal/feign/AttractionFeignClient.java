package com.mytrip.attractionservice.internal.feign;

import com.mytrip.attractionservice.internal.configuration.AttractionClientConfiguration;
import com.mytrip.attractionservice.internal.model.Attraction;
import com.mytrip.attractionservice.internal.model.RestOkAttractionsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@FeignClient(name = "attractions",
        url = "https://tripadvisor1.p.rapidapi.com",
        configuration = AttractionClientConfiguration.class)
public interface AttractionFeignClient {

    String APPLICATION_JSON = "application/json";

    String rzeszow = "274785";

    @RequestMapping(value = "/attractions/list",
            produces = APPLICATION_JSON,
            method = RequestMethod.GET)
    ResponseEntity<RestOkAttractionsResponse> getAttractionsByCityId(@RequestHeader(name = "x-rapidapi-key") String rapidApiKey,
                                                                     @RequestParam(name = "location_id") String locationId);

    @RequestMapping(value = "/attractions/get-details",
            produces = APPLICATION_JSON,
            method = RequestMethod.GET)
    ResponseEntity<Optional<Attraction>> getAttractionsByAttractionId(@RequestHeader(name = "x-rapidapi-key") String rapidApiKey,
                                                                      @RequestParam(name = "location_id") String attractionId);
}
