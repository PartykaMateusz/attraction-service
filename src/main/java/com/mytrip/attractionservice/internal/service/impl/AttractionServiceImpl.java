package com.mytrip.attractionservice.internal.service.impl;

import com.mytrip.attractionservice.api.exception.*;
import com.mytrip.attractionservice.internal.feign.AttractionFeignClient;

import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.AutoCompleteAttraction;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAutoCompleteResponse;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.service.AttractionService;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class AttractionServiceImpl implements AttractionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttractionServiceImpl.class);
    private static final String ATTRACTIONS_SERVICE_5XX_ERROR = "ATTRACTIONS_SERVICE_5XX_ERROR";
    private static final String GET_ATTRACTIONS_INFO_NOT_FOUND = "GET_ATTRACTIONS_INFO_NOT_FOUND";

    @Value("${rapidApi.tripAdvisor.key}")
    private String KEY;

    @Autowired
    private AttractionFeignClient attractionClient;

    @Autowired
    private Function<AttractionResponse, Location> attractionMapper;

    @Override
    public List<Location> getAttractionsByCityId(Long cityId) {

        LOGGER.info("searching attractions in city "+cityId);

        try {
            Optional<RestOkAttractionsResponse> attractionsResponse = attractionClient.getAttractionsByCityId(KEY, cityId.toString());
            List<Location> attractions = attractionsResponse.orElseThrow(() -> new AttractionsInCityNotFound(cityId))
                    .getData()
                    .stream()
                    .filter(attraction -> attraction.getLocationId() != null && attraction.getName() != null)
                    .map(this.attractionMapper)
                    .collect(Collectors.toList());
            LOGGER.info("returned {} attractions in {} city", attractions.size(), cityId);
            return attractions;
        }
        catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                this.handleNotFoundResponse(cityId);
            }
            LOGGER.error(ATTRACTIONS_SERVICE_5XX_ERROR, "Attractions service 5xx error. status code : {0}", e.status());
        }
        this.handleUnexpectedResponse();
        return null;
    }

    @Override
    public Location getAttractionsByAttractionId(Long attractionId) {
        LOGGER.info("searching attractions by ID:  "+attractionId);

        try {
            Optional<AttractionResponse> attractionsResponse = attractionClient.getAttractionsByAttractionId(KEY, attractionId.toString());
            AttractionResponse attraction = attractionsResponse.orElseThrow(() -> new AttractionNotFoundException(attractionId));
            if (attraction.getLocationId() == null) {
                //TODO make cool exception resolver
                LOGGER.warn("Attraction not found. attractionId: {}", attractionId);
                throw new AttractionNotFoundException(attractionId);
            }
            LOGGER.info("successfully returned attraction. name: {}, id: {}", attraction.getName(), attractionId);
            return this.attractionMapper.apply(attraction);
        }
        catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                handleNotFoundResponse(attractionId);
            }

            LOGGER.error(ATTRACTIONS_SERVICE_5XX_ERROR, "Attractions service 5xx error. status code : {0}", e.status());

            throw e;
        }
    }

    private void handleNotFoundResponse(Long cityId) {
        LOGGER.info(GET_ATTRACTIONS_INFO_NOT_FOUND, "Attractions number not found for city {0}", cityId);
        throw new AttractionClientPackageNotFoundException(cityId);
    }

    private void handleUnexpectedResponse() {
        LOGGER.info(GET_ATTRACTIONS_INFO_NOT_FOUND, "Unexpected error");
        throw new AttractionException();
    }

    private void handleNotFoundResponse(String attractionName) {
        LOGGER.info(GET_ATTRACTIONS_INFO_NOT_FOUND, "Attractions {0} not found }", attractionName);
        throw new AttractionClientPackageNotFoundException(attractionName);
    }
}
