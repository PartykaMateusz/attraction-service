package com.mytrip.attractionservice.internal.service.impl;

import com.mytrip.attractionservice.api.exception.AttractionClientPackageNotFoundException;
import com.mytrip.attractionservice.api.exception.AttractionException;
import com.mytrip.attractionservice.api.exception.AttractionNotFoundException;
import com.mytrip.attractionservice.internal.feign.AttractionFeignClient;
import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.AutoCompleteAttraction;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAutoCompleteResponse;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.model.mapper.AttractionMapper;
import com.mytrip.attractionservice.internal.service.impl.AttractionServiceImpl;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AttractionServiceImplTest {

    private static final Long CITY_ID = 274785L;
    private static final Long ATTRACTION_ID = 4872855L;
    private static final String TEST_KEY = "testKey";

    @InjectMocks
    private AttractionServiceImpl attractionService;

    @Mock
    private AttractionFeignClient attractionClient;

    @Mock
    private Function<AttractionResponse, Location> attractionMapper;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(this.attractionService, "KEY", TEST_KEY);
        Optional<RestOkAttractionsResponse> attractionsResponse = Optional.of(this.generateAttractions());
        Optional<AttractionResponse> attractionResponse = Optional.of(this.generateAttraction());

        when(attractionClient.getAttractionsByCityId(TEST_KEY, CITY_ID.toString())).thenReturn(attractionsResponse);
        when(attractionClient.getAttractionsByAttractionId(anyString(), anyString())).thenReturn(attractionResponse);
    }

    @Test
    public void getAttractionsByCityId(){
        attractionService.getAttractionsByCityId(CITY_ID).get(0);
        verify(attractionClient).getAttractionsByCityId(TEST_KEY, CITY_ID.toString());
        verify(attractionMapper).apply(any());
    }

    @Test
    public void getAttractionsByCityIdWhenFeignException(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.NotFound("test", request, null);
        when(attractionClient.getAttractionsByCityId(anyString(), anyString())).thenThrow(feignException);
        verify(attractionMapper, never()).apply(any());
        assertThrows(AttractionClientPackageNotFoundException.class, () -> attractionService.getAttractionsByCityId(CITY_ID).get(0));
    }

    @Test
    public void getAttractionsByCityIdWhenUnexpectedException(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.ServiceUnavailable("test", request, null);
        when(attractionClient.getAttractionsByCityId(anyString(), anyString())).thenThrow(feignException);
        verify(attractionMapper, never()).apply(any());
        assertThrows(AttractionException.class, () -> attractionService.getAttractionsByCityId(CITY_ID).get(0));
    }

    @Test
    public void getAttractionsById(){
        this.generateAttraction();
        attractionService.getAttractionsByAttractionId(ATTRACTION_ID);
        verify(attractionClient).getAttractionsByAttractionId(TEST_KEY, ATTRACTION_ID.toString());
        verify(attractionMapper).apply(any());
    }

    @Test
    public void getAttractionsByIdWhenAttractionNotFound(){
        AttractionResponse expectedAttraction = this.generateAttraction();
        expectedAttraction.setLocationId(null);
        when(attractionClient
                .getAttractionsByAttractionId(anyString(), anyString()))
                .thenReturn(Optional.of(expectedAttraction));
        verify(attractionMapper, never()).apply(any());
        assertThrows(AttractionNotFoundException.class, () -> attractionService.getAttractionsByAttractionId(ATTRACTION_ID));
    }

    @Test
    public void getAttractionsByIdWhenFeignException(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.NotFound("test", request, null);
        when(attractionClient.getAttractionsByAttractionId(anyString(), anyString())).thenThrow(feignException);
        assertThrows(AttractionClientPackageNotFoundException.class, () -> attractionService.getAttractionsByAttractionId(ATTRACTION_ID));
    }

    private RestOkAttractionsResponse generateAttractions() {
        RestOkAttractionsResponse response = new RestOkAttractionsResponse();
        AttractionResponse attraction = this.generateAttraction();
        response.restOkAttractionsResponse(Collections.singletonList(attraction));
        return response;
    }

    private Request generateRequest() {
        return Request.create(Request.HttpMethod.GET, "test", new HashMap<>(), (byte[]) null, null);
    }

    private AttractionResponse generateAttraction() {
        AttractionResponse attraction = new AttractionResponse();
        attraction.setLocationId("4872855");
        attraction.setName("Market Square");
        attraction.setLatitude("50.037384");
        attraction.setLongitude("22.004278");
        attraction.setWebsite("http://www.rzeszow.pl/miasto-rzeszow/historia/zabytki-rzeszowa/rynek");
        return attraction;
    }
}