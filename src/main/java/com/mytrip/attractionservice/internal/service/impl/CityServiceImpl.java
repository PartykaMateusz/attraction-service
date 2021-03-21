package com.mytrip.attractionservice.internal.service.impl;

import com.mytrip.attractionservice.api.exception.city.CityException;
import com.mytrip.attractionservice.api.exception.city.CityNotFound;
import com.mytrip.attractionservice.api.exception.restaurants.RestaurantsNotFound;
import com.mytrip.attractionservice.internal.feign.AttractionFeignClient;
import com.mytrip.attractionservice.internal.feign.CityFeignClient;
import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.city.AutoCompleteResponseList;
import com.mytrip.attractionservice.internal.feign.model.city.AutoComplete;
import com.mytrip.attractionservice.internal.feign.model.city.AutoCompleteResponse;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.model.LocationType;
import com.mytrip.attractionservice.internal.model.mapper.AttractionMapper;
import com.mytrip.attractionservice.internal.service.CityService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    private static final String GEOS = "geos";

    @Value("${rapidApi.tripAdvisor.key}")
    private String KEY;

    private static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);
    private static final String CITIES_SERVICE_5XX_ERROR = "CITIES_SERVICE_5XX_ERROR";
    private static final String GET_CITIES_INFO_NOT_FOUND = "GET_CITIES_INFO_NOT_FOUND";

    @Autowired
    private CityFeignClient cityClient;

    @Autowired
    private Function<AutoComplete, Location> cityMapper;

    @Autowired
    private Function<AttractionResponse, Location> attractionMapper;

    @Autowired
    private AttractionFeignClient attractionClient;

    @Override
    public List<Location> getCityByName(String cityName) {

        LOGGER.info("searching cities by name "+cityName);

        try {
            Optional<AutoCompleteResponseList> optionalCityResponseList = cityClient.getLocationByName(KEY, cityName);
            AutoCompleteResponseList citiesResponse =
                    optionalCityResponseList.orElseThrow(() -> new CityNotFound(cityName));
            List<Location> cities = citiesResponse.getData()
                    .stream()
                    .filter(cityResponse -> cityResponse.getResultType().equals(GEOS))
                    .map(AutoCompleteResponse::getResultObject)
                    .filter(city -> city.getLocationId() != null)
                    .map(this.cityMapper)
                    .collect(Collectors.toList());

            LOGGER.info("returned {} cities", cities.size());
            return cities;
        }
        catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                this.handleNotFoundResponse(cityName);
            }
            LOGGER.error(CITIES_SERVICE_5XX_ERROR, "Attractions service 5xx error. status code : {0}", e.status());
        }
        this.handleUnexpectedResponse();
        return null;
    }

    @Override
    public Location getCityById(final String cityId) {
        LOGGER.info("searching city by id " + cityId);

        try {
            final Optional<AttractionResponse> attractionsResponse
                    = attractionClient.getAttractionsByAttractionId(KEY, cityId);
            final AttractionResponse attraction = attractionsResponse
                    .orElseThrow(() -> new CityNotFound(cityId));
            if (attraction.getLocationId() == null) {
                LOGGER.warn("City not found. cityId: {}", cityId);
                throw new CityNotFound(cityId);
            }
            Location city = this.attractionMapper.apply(attraction);
            if (!isCity(city)) {
                LOGGER.warn("Location {}, with id: {} found, but type: {} is wrong",
                        city.getName(), cityId, city.getLocationType());
                throw new CityNotFound(cityId);
            }
            LOGGER.info("successfully returned city. name: {}, id: {}", attraction.getName(), cityId);
            return city;
        }
        catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                handleNotFoundResponse(cityId);
            }

            LOGGER.error(CITIES_SERVICE_5XX_ERROR, "City service 5xx error. status code : {0}", e.status());
            this.handleUnexpectedResponse();
        }
        return null;
    }

    private boolean isCity(final Location city) {
        return city.getLocationType() != null
                && city.getLocationType().equals(LocationType.CITY);
    }

    private void handleNotFoundResponse(final String cityName) {
        LOGGER.info(GET_CITIES_INFO_NOT_FOUND, "AutoComplete {0} notFound", cityName);
        throw new CityNotFound(cityName);
    }

    private void handleUnexpectedResponse() {
        LOGGER.info(GET_CITIES_INFO_NOT_FOUND, "Unexpected error");
        throw new CityException();
    }
}
