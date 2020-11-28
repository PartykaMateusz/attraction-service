package com.mytrip.attractionservice.internal.impl;

import com.mytrip.attractionservice.api.exception.AttractionClientPackageNotFoundException;
import com.mytrip.attractionservice.api.exception.AttractionException;
import com.mytrip.attractionservice.internal.feign.AttractionFeignClient;
import com.mytrip.attractionservice.internal.model.Attraction;
import com.mytrip.attractionservice.internal.model.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.service.AttractionService;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AttractionServiceImpl implements AttractionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttractionServiceImpl.class);
    private static final String ATTRACTIONS_SERVICE_5XX_ERROR = "ATTRACTIONS_SERVICE_5XX_ERROR";
    private static final String GET_ATTRACTIONS_INFO_NOT_FOUND = "GET_ATTRACTIONS_INFO_NOT_FOUND";

    private final String KEY = "6791fde3e0msha209aa7ca51fc34p12803ejsn6025ca677714";

    @Autowired
    private AttractionFeignClient attractionClient;

    @Override
    public List<Attraction> getAttractionsByCityId(Long cityId) {

        LOGGER.info("searching attractions in city "+cityId);

        try {
            ResponseEntity<RestOkAttractionsResponse> attractionsResponse = attractionClient.getAttractionsByCityId(KEY, cityId.toString());
            List<Attraction> attractions = attractionsResponse.getBody().getData();
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



//    @Override
//    public Attraction getAttractionsByAttractionId(Long attractionId) {
//        LOGGER.info("searching attractions by ID:  "+attractionId);
//
//        try {
//            ResponseEntity<RestOkAttractionsResponse> attractionsResponse = attractionClient.getAttractionsByAttractionId(KEY, attractionId.toString());
//            Attraction attraction = attractionsResponse.getBody().getData().get(0);
//            LOGGER.info("returned {} attractions in {} city", attraction.size(), attractionId);
//            return attraction;
//        }
//        catch (FeignException e) {
//            if (e.status() == HttpStatus.NOT_FOUND.value()) {
//                handleNotFoundResponse(cityId);
//            }
//
//            if (e.status() >= 500 && e.status() <= 599) {
//                LOGGER.error(ATTRACTIONS_SERVICE_5XX_ERROR, "Attractions service 5xx error. status code : {0}", e.status());
//            } else {
//                LOGGER.error(ATTRACTIONS_SERVICE_5XX_ERROR, "Attractions service error for city: {0} " +
//                        "status code: {1} ", cityId, e.status());
//            }
//
//            throw e;
//        }
//    }

    private void handleNotFoundResponse(Long cityId) {
        LOGGER.info(GET_ATTRACTIONS_INFO_NOT_FOUND, "Attractions number not found for city {0}", cityId);
        throw new AttractionClientPackageNotFoundException(cityId);
    }

    private void handleUnexpectedResponse() {
        LOGGER.info(GET_ATTRACTIONS_INFO_NOT_FOUND, "Unexpected error");
        throw new AttractionException();
    }
}
