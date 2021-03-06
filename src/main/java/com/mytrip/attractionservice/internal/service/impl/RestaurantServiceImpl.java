package com.mytrip.attractionservice.internal.service.impl;

import com.mytrip.attractionservice.api.exception.city.CityNotFound;
import com.mytrip.attractionservice.api.exception.restaurants.RestaurantClientPackageNotFoundException;
import com.mytrip.attractionservice.api.exception.restaurants.RestaurantException;
import com.mytrip.attractionservice.api.exception.restaurants.RestaurantsNotFound;
import com.mytrip.attractionservice.internal.feign.RestaurantFeignClient;
import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.service.CityService;
import com.mytrip.attractionservice.internal.service.RestaurantService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantServiceImpl.class);
    private static final String RESTAURANTS_SERVICE_5XX_ERROR = "RESTAURANTS_SERVICE_5XX_ERROR";
    private static final String GET_RESTAURANTS_INFO_NOT_FOUND = "GET_RESTAURANTS_INFO_NOT_FOUND";

    @Value("${rapidApi.tripAdvisor.key}")
    private String KEY;

    @Autowired
    private RestaurantFeignClient restaurantFeignClient;

    @Autowired
    private Function<AttractionResponse, Location> attractionMapper;

    @Autowired
    private CityService cityService;

    @Override
    public List<Location> getRestaurantsByCoordinates(final String latitude, final String longitude) {

        LOGGER.info("searching restaurants by latitude: {}, longitude: {} ", latitude, longitude);

        try {
            Optional<RestOkAttractionsResponse> attractionsResponse = restaurantFeignClient.getRestaurantsByCoordinates(KEY, latitude, longitude);
            List<Location> attractions = attractionsResponse.orElseThrow(() -> new RestaurantsNotFound(latitude, longitude))
                    .getData()
                    .stream()
                    .filter(attraction -> attraction.getLocationId() != null)
                    .filter(attraction -> attraction.getName() != null)
                    .map(attraction -> attractionMapper.apply(attraction))
                    .collect(Collectors.toList());
            LOGGER.info("returned {} attractions in latitude: {}, longitude: {}", attractions.size(), latitude, longitude);
            return attractions;
        }
        catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                this.handleNotFoundResponse(latitude, longitude);
            }
            LOGGER.error(RESTAURANTS_SERVICE_5XX_ERROR, "Attractions service 5xx error. status code : {0}", e.status());
        }
        this.handleUnexpectedResponse();
        return null;
    }

    @Override
    public List<Location> getRestaurantsByCity(final String cityName) {
        LOGGER.info("searching restaurants by cityName: {} ", cityName);

        final List<Location> cities = cityService.getCityByName(cityName);
        if (cities.isEmpty()) {
            LOGGER.debug("City: {} not found ", cityName);
            throw new CityNotFound(cityName);
        }
        final Location city = cities.get(0);
        return this.getRestaurantsByCoordinates(city.getLatitude(), city.getLongitude());
    }

    private void handleNotFoundResponse(final String latitude, final String longitude) {
        LOGGER.info(GET_RESTAURANTS_INFO_NOT_FOUND, "Restaurants not found for latitude: {}, longitude: {}", latitude, longitude);
        throw new RestaurantClientPackageNotFoundException(latitude, longitude);
    }

    private void handleUnexpectedResponse() {
        LOGGER.info(RESTAURANTS_SERVICE_5XX_ERROR, "Unexpected error");
        throw new RestaurantException();
    }
}
