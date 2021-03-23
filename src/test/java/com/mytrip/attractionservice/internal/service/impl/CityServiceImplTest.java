package com.mytrip.attractionservice.internal.service.impl;

import com.mytrip.attractionservice.api.exception.city.CityException;
import com.mytrip.attractionservice.api.exception.city.CityNotFound;
import com.mytrip.attractionservice.api.exception.restaurants.RestaurantsNotFound;
import com.mytrip.attractionservice.internal.feign.AttractionFeignClient;
import com.mytrip.attractionservice.internal.feign.CityFeignClient;
import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.city.AutoComplete;
import com.mytrip.attractionservice.internal.feign.model.city.AutoCompleteResponse;
import com.mytrip.attractionservice.internal.feign.model.city.AutoCompleteResponseList;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.model.LocationType;
import feign.FeignException;
import feign.Request;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CityServiceImplTest {

    private static final String CITY_NAME = "Warsaw";
    private static final String GEOS = "geos";
    private static final String KEY = "testKey" ;
    private static final String CITY_ID = "274856";
    private static final String LATITUDE = "50";
    private static final String LONGITUDE = "20";

    @InjectMocks
    private CityServiceImpl cityService;

    @Mock
    private CityFeignClient cityFeignClient;

    @Mock
    private AttractionFeignClient attractionClient;

    @Mock
    private Function<AutoComplete, Location> cityMapper;

    @Mock
    private Function<AttractionResponse, Location> attractionMapper;

    private AttractionResponse attractionResponse = this.generateAttractionResponse();

    private Location cityLocation = this.generateLocation();



    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(this.cityService, "KEY", KEY);
        when(this.attractionClient.getAttractionsByAttractionId(KEY, CITY_ID)).thenReturn(Optional.of(attractionResponse));
        when(this.attractionMapper.apply(attractionResponse)).thenReturn(this.cityLocation);
    }

    @Test
    public void getCityByName(){
        AutoCompleteResponseList expectedResponseList = generateCityResponseList();
        when(this.cityFeignClient.getLocationByName(anyString(), anyString())).thenReturn(Optional.of(expectedResponseList));

        List<Location> cities = cityService.getCityByName(CITY_NAME);

        assertEquals(1, cities.size());

        verify(cityMapper).apply(any());
        verify(cityFeignClient).getLocationByName(KEY, CITY_NAME);
    }

    @Test
    public void getCityByNameWhenApiReturnedNotCity(){
        AutoCompleteResponseList expectedResponseList = generateCityResponseListWithCityAndRestaurant();
        when(this.cityFeignClient.getLocationByName(anyString(), anyString())).thenReturn(Optional.of(expectedResponseList));

        List<Location> cities = cityService.getCityByName(CITY_NAME);

        assertEquals(1, cities.size());

        verify(cityMapper).apply(any());
        verify(cityFeignClient).getLocationByName(KEY, CITY_NAME);
    }

    @Test
    public void getCityByNameWhenFeignException(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.NotFound("test", request, null);
        when(cityFeignClient.getLocationByName(anyString(), anyString())).thenThrow(feignException);
        verify(cityMapper, never()).apply(any());
        assertThrows(CityNotFound.class, () -> cityService.getCityByName(CITY_NAME));
    }

    @Test
    public void getAttractionByAttractionNameWhen500ErrorResponse(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.ServiceUnavailable("test", request, null);
        when(cityFeignClient.getLocationByName(anyString(), anyString())).thenThrow(feignException);
        verify(cityMapper, never()).apply(any());
        assertThrows(CityException.class, () -> cityService.getCityByName(CITY_NAME));
    }

    @Test
    public void getCityById(){
        Location location = cityService.getCityById(CITY_ID);
        assertNotNull(location);
        verify(attractionMapper).apply(any());
    }

    @Test
    public void getCityByIdWhenLocationIdIsNull(){
        AttractionResponse attractionResponse = this.generateAttractionResponse();
        attractionResponse.setLocationId(null);
        when(this.attractionClient.getAttractionsByAttractionId(KEY, CITY_ID)).thenReturn(Optional.of(attractionResponse));
        when(this.attractionMapper.apply(attractionResponse)).thenReturn(this.cityLocation);
        assertThrows(CityNotFound.class, () -> cityService.getCityById(CITY_ID));
    }

    @Test
    public void getCityByIdWhenLocationIsFoundButTypeIsWrong(){
        cityLocation.setLocationType(LocationType.RESTAURANT);
        when(this.attractionClient.getAttractionsByAttractionId(KEY, CITY_ID)).thenReturn(Optional.of(attractionResponse));
        when(this.attractionMapper.apply(attractionResponse)).thenReturn(this.cityLocation);
        assertThrows(CityNotFound.class, () -> cityService.getCityById(CITY_ID));
    }

    @Test
    public void getCityByIdWhenFeignException(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.NotFound("test", request, null);
        when(attractionClient.getAttractionsByAttractionId(KEY, CITY_ID)).thenThrow(feignException);
        verify(attractionMapper, never()).apply(any());
        assertThrows(CityNotFound.class, () -> cityService.getCityById(CITY_ID));
    }

    @Test
    public void getCityByIdWhen500ErrorResponse(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.BadGateway("test", request, null);
        when(attractionClient.getAttractionsByAttractionId(KEY, CITY_ID)).thenThrow(feignException);
        verify(attractionMapper, never()).apply(any());
        assertThrows(CityException.class, () -> cityService.getCityById(CITY_ID));
    }

    private AutoCompleteResponseList generateCityResponseList() {
        AutoCompleteResponseList cityResponseList = new AutoCompleteResponseList();
        cityResponseList.setData(generateData());
        return cityResponseList;
    }

    private AutoCompleteResponseList generateCityResponseListWithCityAndRestaurant() {
        AutoCompleteResponseList cityResponseList = new AutoCompleteResponseList();
        cityResponseList.setData(generateDataWithCityAndRestaurant());
        return cityResponseList;
    }

    private List<AutoCompleteResponse> generateDataWithCityAndRestaurant() {
        AutoComplete warsaw = generateWarsaw();
        AutoCompleteResponse cityResponse = new AutoCompleteResponse(GEOS, warsaw);
        AutoComplete restaurant = generateRestaurant();
        AutoCompleteResponse restaurantResponse = new AutoCompleteResponse("restaurant", restaurant);
        return List.of(cityResponse, restaurantResponse);
    }

    private List<AutoCompleteResponse> generateData() {
        AutoComplete warsaw = generateWarsaw();
        AutoCompleteResponse cityResponse = new AutoCompleteResponse(GEOS, warsaw);
        return List.of(cityResponse);
    }

    private AutoComplete generateWarsaw() {
        AutoComplete autoComplete = new AutoComplete();
        autoComplete.setName("Warsaw");
        autoComplete.setTimezone("Europe/Warsaw");
        autoComplete.setLocation("Warsaw, Poland");
        autoComplete.setLocationId(CITY_ID);
        autoComplete.setLatitude("52.229263");
        autoComplete.setLongitude("21.01181");
        return autoComplete;
    }

    private AutoComplete generateRestaurant() {
        AutoComplete restaurant = new AutoComplete();
        restaurant.setName("restaurant");
        restaurant.setTimezone("Europe/Warsaw");
        restaurant.setLocation("Warsaw, Poland");
        restaurant.setLocationId(CITY_ID);
        restaurant.setLatitude("52.229263");
        restaurant.setLongitude("21.01181");
        return restaurant;
    }

    private Request generateRequest() {
        Request request = Request.create(Request.HttpMethod.GET, "test", new HashMap<>(), (byte[]) null, null);
        return request;
    }

    private AttractionResponse generateAttractionResponse() {
        AttractionResponse attraction = new AttractionResponse();
        attraction.setLocationId(CITY_ID);
        attraction.setName(CITY_NAME);
        attraction.setLatitude("52.229263");
        attraction.setLongitude("21.01181");
        return attraction;
    }

    private Location generateLocation() {
        Location city = new Location();
        city.setName("Warsaw");
        city.setTimezone("Europe/Warsaw");
        city.setLocation("Warsaw, Poland");
        city.setLocationId("274856");
        city.setLatitude(LATITUDE);
        city.setLongitude(LONGITUDE);
        city.setLocationType(LocationType.CITY);
        return city;
    }
}