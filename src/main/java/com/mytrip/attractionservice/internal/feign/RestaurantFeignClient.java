package com.mytrip.attractionservice.internal.feign;

import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.feign.model.city.AutoCompleteResponseList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "restaurants",
        url = "https://tripadvisor1.p.rapidapi.com")
public interface RestaurantFeignClient {

    String APPLICATION_JSON = "application/json";

    @RequestMapping(value = "/restaurants/list-by-latlng",
            produces = APPLICATION_JSON,
            method = RequestMethod.GET)
    Optional<RestOkAttractionsResponse> getRestaurantsByCoordinates(@RequestHeader(name = "x-rapidapi-key") String rapidApiKey,
                                                             @RequestParam(name = "latitude") String latitude,
                                                             @RequestParam(name = "longitude") String longitude);

    @RequestMapping(value = "/locations/auto-complete",
            produces = APPLICATION_JSON,
            method = RequestMethod.GET)
    Optional<AutoCompleteResponseList> getRestaurantByName(@RequestHeader(name = "x-rapidapi-key") String rapidApiKey,
                                                           @RequestParam(name = "query") String query);
}
