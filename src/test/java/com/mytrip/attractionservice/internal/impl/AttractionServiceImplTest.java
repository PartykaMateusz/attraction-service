package com.mytrip.attractionservice.internal.impl;

import com.mytrip.attractionservice.api.exception.AttractionClientPackageNotFoundException;
import com.mytrip.attractionservice.api.exception.AttractionException;
import com.mytrip.attractionservice.api.exception.AttractionNotFoundException;
import com.mytrip.attractionservice.internal.feign.AttractionFeignClient;
import com.mytrip.attractionservice.internal.model.Attraction;
import com.mytrip.attractionservice.internal.model.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.service.AttractionService;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.swing.text.html.Option;
import javax.validation.constraints.Null;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AttractionServiceImplTest {

    private static final Long CITY_ID = 274785L;
    private static final Long ATTRACTION_ID = 4872855L;

    @InjectMocks
    private AttractionServiceImpl attractionService;

    @Mock
    AttractionFeignClient attractionClient;

    @BeforeEach
    public void setUp() {
        Optional<RestOkAttractionsResponse> attractionsResponse = Optional.of(this.generateAttractions());
        Optional<Attraction> attractionResponse = Optional.of(this.generateAttraction());

        when(attractionClient.getAttractionsByCityId(anyString(), anyString())).thenReturn(attractionsResponse);
        when(attractionClient.getAttractionsByAttractionId(anyString(), anyString())).thenReturn(attractionResponse);
    }

    private RestOkAttractionsResponse generateAttractions() {
        RestOkAttractionsResponse response = new RestOkAttractionsResponse();
        Attraction attraction = this.generateAttraction();
        response.restOkAttractionsResponse(Collections.singletonList(attraction));
        return response;
    }


    @Test
    public void getAttractionsByCityId(){
        Attraction expectedAttraction = this.generateAttraction();

        Attraction actualAttraction = attractionService.getAttractionsByCityId(CITY_ID).get(0);

        assertEquals(expectedAttraction.getLocation_id(), actualAttraction.getLocation_id());
        assertEquals(expectedAttraction.getName(), actualAttraction.getName());
        assertEquals(expectedAttraction.getLatitude(), actualAttraction.getLatitude());
        assertEquals(expectedAttraction.getLongitude(), actualAttraction.getLongitude());
        assertEquals(expectedAttraction.getWebsite(), actualAttraction.getWebsite());
    }

    @Test
    public void getAttractionsByCityIdWhenFeignException(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.NotFound("test", request, null);
        when(attractionClient.getAttractionsByCityId(anyString(), anyString())).thenThrow(feignException);
        assertThrows(AttractionClientPackageNotFoundException.class, () -> attractionService.getAttractionsByCityId(CITY_ID).get(0));
    }

    @Test
    public void getAttractionsByCityIdWhenUnexpectedException(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.ServiceUnavailable("test", request, null);
        when(attractionClient.getAttractionsByCityId(anyString(), anyString())).thenThrow(feignException);
        assertThrows(AttractionException.class, () -> attractionService.getAttractionsByCityId(CITY_ID).get(0));
    }

    @Test
    public void getAttractionsById(){
        Attraction expectedAttraction = this.generateAttraction();

        Attraction actualAttraction = attractionService.getAttractionsByAttractionId(ATTRACTION_ID);

        assertEquals(expectedAttraction.getLocation_id(), actualAttraction.getLocation_id());
        assertEquals(expectedAttraction.getName(), actualAttraction.getName());
        assertEquals(expectedAttraction.getLatitude(), actualAttraction.getLatitude());
        assertEquals(expectedAttraction.getLongitude(), actualAttraction.getLongitude());
        assertEquals(expectedAttraction.getWebsite(), actualAttraction.getWebsite());
    }

    @Test
    public void getAttractionsByIdWhenAttractionNotFound(){
        Attraction expectedAttraction = this.generateAttraction();
        expectedAttraction.setLocation_id(null);
        when(attractionClient
                .getAttractionsByAttractionId(anyString(), anyString()))
                .thenReturn(Optional.of(expectedAttraction));

        assertThrows(AttractionNotFoundException.class, () -> attractionService.getAttractionsByAttractionId(ATTRACTION_ID));
    }

    @Test
    public void getAttractionsByIdWhenFeignException(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.NotFound("test", request, null);
        when(attractionClient.getAttractionsByAttractionId(anyString(), anyString())).thenThrow(feignException);
        assertThrows(AttractionClientPackageNotFoundException.class, () -> attractionService.getAttractionsByAttractionId(ATTRACTION_ID));
    }

    private Request generateRequest() {
        Request request = Request.create(Request.HttpMethod.GET, "test", new HashMap<>(), (byte[]) null, null);

        return request;
    }

    private Attraction generateAttraction() {
        Attraction attraction = new Attraction();
        attraction.setLocation_id("4872855");
        attraction.setName("Market Square");
        attraction.setLatitude("50.037384");
        attraction.setLongitude("22.004278");
        attraction.setWebsite("http://www.rzeszow.pl/miasto-rzeszow/historia/zabytki-rzeszowa/rynek");
        return attraction;
    }
}