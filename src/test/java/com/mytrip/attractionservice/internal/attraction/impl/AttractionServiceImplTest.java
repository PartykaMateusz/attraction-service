package com.mytrip.attractionservice.internal.attraction.impl;

import com.mytrip.attractionservice.api.exception.AttractionClientPackageNotFoundException;
import com.mytrip.attractionservice.api.exception.AttractionException;
import com.mytrip.attractionservice.api.exception.AttractionNotFoundException;
import com.mytrip.attractionservice.internal.feign.AttractionFeignClient;
import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.AutoCompleteAttraction;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAutoCompleteResponse;
import com.mytrip.attractionservice.internal.service.impl.AttractionServiceImpl;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        ReflectionTestUtils.setField(this.attractionService, "KEY", "testKey");
        Optional<RestOkAttractionsResponse> attractionsResponse = Optional.of(this.generateAttractions());
        Optional<AttractionResponse> attractionResponse = Optional.of(this.generateAttraction());

        when(attractionClient.getAttractionsByCityId(anyString(), anyString())).thenReturn(attractionsResponse);
        when(attractionClient.getAttractionsByAttractionId(anyString(), anyString())).thenReturn(attractionResponse);

    }

    @Test
    public void getAttractionsByCityId(){
        AttractionResponse expectedAttraction = this.generateAttraction();
        AttractionResponse actualAttraction = attractionService.getAttractionsByCityId(CITY_ID).get(0);

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
        AttractionResponse expectedAttraction = this.generateAttraction();

        AttractionResponse actualAttraction = attractionService.getAttractionsByAttractionId(ATTRACTION_ID);

        assertEquals(expectedAttraction.getLocation_id(), actualAttraction.getLocation_id());
        assertEquals(expectedAttraction.getName(), actualAttraction.getName());
        assertEquals(expectedAttraction.getLatitude(), actualAttraction.getLatitude());
        assertEquals(expectedAttraction.getLongitude(), actualAttraction.getLongitude());
        assertEquals(expectedAttraction.getWebsite(), actualAttraction.getWebsite());
    }

    @Test
    public void getAttractionsByIdWhenAttractionNotFound(){
        AttractionResponse expectedAttraction = this.generateAttraction();
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

    @Test
    public void getAttractionByAttractionName() {
        Optional<RestOkAutoCompleteResponse> expected = Optional.of(this.generateRestOkAutoCompleteResponse());
        when(attractionClient.getLocationByName(anyString(), anyString())).thenReturn(expected);

        Set<AttractionResponse> actualResponse = attractionService.getAttractionsByAttractionName("Warsaw");

        assertEquals(2, actualResponse.size());
        Iterator<AttractionResponse> iterator = actualResponse.iterator();

        AttractionResponse firstActualAttraction = iterator.next();
        assertEquals(expected.get().getData().get(0).getResultObject().getLocation_id(), firstActualAttraction.getLocation_id());
        assertEquals(expected.get().getData().get(0).getResultObject().getName(), firstActualAttraction.getName());
        assertEquals(expected.get().getData().get(0).getResultObject().getLatitude(), firstActualAttraction.getLatitude());
        assertEquals(expected.get().getData().get(0).getResultObject().getLongitude(), firstActualAttraction.getLongitude());
        assertEquals(expected.get().getData().get(0).getResultObject().getWebsite(), firstActualAttraction.getWebsite());

        AttractionResponse secondActualAttraction = iterator.next();
        assertEquals(expected.get().getData().get(1).getResultObject().getLocation_id(), secondActualAttraction.getLocation_id());
        assertEquals(expected.get().getData().get(1).getResultObject().getName(), secondActualAttraction.getName());
        assertEquals(expected.get().getData().get(1).getResultObject().getLatitude(), secondActualAttraction.getLatitude());
        assertEquals(expected.get().getData().get(1).getResultObject().getLongitude(), secondActualAttraction.getLongitude());
        assertEquals(expected.get().getData().get(1).getResultObject().getWebsite(), secondActualAttraction.getWebsite());
    }

    @Test
    public void getAttractionByAttractionNameWhenFeignException(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.NotFound("test", request, null);
        when(attractionClient.getLocationByName(anyString(), anyString())).thenThrow(feignException);
        assertThrows(AttractionClientPackageNotFoundException.class, () -> attractionService.getAttractionsByAttractionName("Warsaw"));
    }

    @Test
    public void getAttractionByAttractionNameWhen500ErrorResponse(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.ServiceUnavailable("test", request, null);
        when(attractionClient.getLocationByName(anyString(), anyString())).thenThrow(feignException);
        assertThrows(AttractionException.class, () -> attractionService.getAttractionsByAttractionName("Warsaw"));
    }

    private RestOkAttractionsResponse generateAttractions() {
        RestOkAttractionsResponse response = new RestOkAttractionsResponse();
        AttractionResponse attraction = this.generateAttraction();
        response.restOkAttractionsResponse(Collections.singletonList(attraction));
        return response;
    }

    private RestOkAutoCompleteResponse generateRestOkAutoCompleteResponse() {

        RestOkAutoCompleteResponse response = new RestOkAutoCompleteResponse();

        AutoCompleteAttraction marketSquare = new AutoCompleteAttraction(this.generateAttraction());
        AutoCompleteAttraction marketSquare2 = new AutoCompleteAttraction(this.generateAttraction());
        marketSquare2.getResultObject().setLocation_id("1");

        response.setData(Arrays.asList(marketSquare, marketSquare2));

        return response;
    }

    private Request generateRequest() {
        Request request = Request.create(Request.HttpMethod.GET, "test", new HashMap<>(), (byte[]) null, null);

        return request;
    }

    private AttractionResponse generateAttraction() {
        AttractionResponse attraction = new AttractionResponse();
        attraction.setLocation_id("4872855");
        attraction.setName("Market Square");
        attraction.setLatitude("50.037384");
        attraction.setLongitude("22.004278");
        attraction.setWebsite("http://www.rzeszow.pl/miasto-rzeszow/historia/zabytki-rzeszowa/rynek");
        return attraction;
    }
}