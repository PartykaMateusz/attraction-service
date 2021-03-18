package com.mytrip.attractionservice.internal.service.impl;

import com.mytrip.attractionservice.api.exception.city.CityException;
import com.mytrip.attractionservice.api.exception.city.CityNotFound;
import com.mytrip.attractionservice.internal.feign.CityFeignClient;
import com.mytrip.attractionservice.internal.feign.model.city.AutoCompleteResponseList;
import com.mytrip.attractionservice.internal.feign.model.city.AutoComplete;
import com.mytrip.attractionservice.internal.feign.model.city.AutoCompleteResponse;
import com.mytrip.attractionservice.internal.model.Location;
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

    private void handleNotFoundResponse(final String cityName) {
        LOGGER.info(GET_CITIES_INFO_NOT_FOUND, "AutoComplete {0} notFound", cityName);
        throw new CityNotFound(cityName);
    }

    private void handleUnexpectedResponse() {
        LOGGER.info(GET_CITIES_INFO_NOT_FOUND, "Unexpected error");
        throw new CityException();
    }
}
