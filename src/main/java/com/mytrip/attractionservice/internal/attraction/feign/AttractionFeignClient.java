package com.mytrip.attractionservice.internal.attraction.feign;

import com.mytrip.attractionservice.internal.attraction.configuration.AttractionClientConfiguration;
import com.mytrip.attractionservice.internal.attraction.model.Attraction;
import com.mytrip.attractionservice.internal.attraction.model.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.attraction.model.RestOkAutoCompleteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
    Optional<RestOkAttractionsResponse> getAttractionsByCityId(@RequestHeader(name = "x-rapidapi-key") String rapidApiKey,
                                                               @RequestParam(name = "location_id") String locationId);

    @RequestMapping(value = "/attractions/get-details",
            produces = APPLICATION_JSON,
            method = RequestMethod.GET)
    Optional<Attraction> getAttractionsByAttractionId(@RequestHeader(name = "x-rapidapi-key") String rapidApiKey,
                                                      @RequestParam(name = "location_id") String attractionId);

    @RequestMapping(value = "/locations/auto-complete",
            produces = APPLICATION_JSON,
            method = RequestMethod.GET)
    Optional<RestOkAutoCompleteResponse> getLocationByName(@RequestHeader(name = "x-rapidapi-key") String rapidApiKey,
                                                           @RequestParam(name = "query") String query);


}
