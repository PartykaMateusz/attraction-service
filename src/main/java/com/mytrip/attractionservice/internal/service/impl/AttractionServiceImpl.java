package com.mytrip.attractionservice.internal.service.impl;

import com.mytrip.attractionservice.api.exception.*;
import com.mytrip.attractionservice.internal.feign.AttractionFeignClient;

import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.AutoCompleteAttraction;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAutoCompleteResponse;
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

    @Override
    public List<AttractionResponse> getAttractionsByCityId(Long cityId) {

        LOGGER.info("searching attractions in city "+cityId);

        try {
            Optional<RestOkAttractionsResponse> attractionsResponse = attractionClient.getAttractionsByCityId(KEY, cityId.toString());
            List<AttractionResponse> attractions = attractionsResponse.orElseThrow(() -> new AttractionsInCityNotFound(cityId)).getData();
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
    public AttractionResponse getAttractionsByAttractionId(Long attractionId) {
        LOGGER.info("searching attractions by ID:  "+attractionId);

        try {
            Optional<AttractionResponse> attractionsResponse = attractionClient.getAttractionsByAttractionId(KEY, attractionId.toString());
            AttractionResponse attraction = attractionsResponse.orElseThrow(() -> new AttractionNotFoundException(attractionId));
            if (attraction.getLocation_id() == null) {
                //TODO make cool exception resolver
                LOGGER.warn("Attraction not found. attractionId: {}", attractionId);
                throw new AttractionNotFoundException(attractionId);
            }
            LOGGER.info("successfully returned attraction. name: {}, id: {}", attraction.getName(), attractionId);
            return attraction;
        }
        catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                handleNotFoundResponse(attractionId);
            }

            LOGGER.error(ATTRACTIONS_SERVICE_5XX_ERROR, "Attractions service 5xx error. status code : {0}", e.status());

            throw e;
        }
    }

    @Override
    public Set<AttractionResponse> getAttractionsByAttractionName(String attractionName) {

        LOGGER.info("searching attractions by name "+attractionName);

        try {
            Optional<RestOkAutoCompleteResponse> optionalRestOkAutoCompleteResponse = attractionClient.getLocationByName(KEY, attractionName);
            RestOkAutoCompleteResponse attractionsResponse =
                    optionalRestOkAutoCompleteResponse.orElseThrow(() -> new AttractionNotFound(attractionName));
            Set<AttractionResponse> attractions = attractionsResponse.getData()
                    .stream()
                    .map(AutoCompleteAttraction::getResultObject)
                    .filter(attraction -> attraction.getLocation_id() != null)
                    .collect(Collectors.toSet());

            LOGGER.info("returned {} attractions", attractions.size());
            return attractions;
        }
        catch (FeignException e) {
            if (e.status() == HttpStatus.NOT_FOUND.value()) {
                this.handleNotFoundResponse(attractionName);
            }
            LOGGER.error(ATTRACTIONS_SERVICE_5XX_ERROR, "Attractions service 5xx error. status code : {0}", e.status());
        }
        this.handleUnexpectedResponse();
        return null;
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
