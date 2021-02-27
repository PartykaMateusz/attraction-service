package com.mytrip.attractionservice.internal.city.impl;

import com.mytrip.attractionservice.api.exception.city.CityException;
import com.mytrip.attractionservice.api.exception.city.CityNotFound;
import com.mytrip.attractionservice.internal.city.feign.CityFeignClient;
import com.mytrip.attractionservice.internal.city.model.City;
import com.mytrip.attractionservice.internal.city.model.CityResponse;
import com.mytrip.attractionservice.internal.city.model.CityResponseList;
import com.mytrip.attractionservice.internal.city.service.CityService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    private static final String GEOS = "geos";
    private final String KEY = "6791fde3e0msha209aa7ca51fc34p12803ejsn6025ca677714";
    private static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);
    private static final String CITIES_SERVICE_5XX_ERROR = "CITIES_SERVICE_5XX_ERROR";
    private static final String GET_CITIES_INFO_NOT_FOUND = "GET_CITIES_INFO_NOT_FOUND";

    @Autowired
    private CityFeignClient cityClient;

    @Override
    public Set<City> getCityByName(String cityName) {

        LOGGER.info("searching cities by name "+cityName);

        try {
            Optional<CityResponseList> optionalCityResponseList = cityClient.getLocationByName(KEY, cityName);
            CityResponseList citiesResponse =
                    optionalCityResponseList.orElseThrow(() -> new CityNotFound(cityName));
            Set<City> cities = citiesResponse.getData()
                    .stream()
                    .filter(cityResponse -> cityResponse.getResultType().equals(GEOS))
                    .map(CityResponse::getResultObject)
                    .filter(city -> city.getLocationId() != null)
                    .collect(Collectors.toSet());

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

    private void handleNotFoundResponse(final String cityName) {
        LOGGER.info(GET_CITIES_INFO_NOT_FOUND, "City {0} notFound", cityName);
        throw new CityNotFound(cityName);
    }

    private void handleUnexpectedResponse() {
        LOGGER.info(GET_CITIES_INFO_NOT_FOUND, "Unexpected error");
        throw new CityException();
    }
}
