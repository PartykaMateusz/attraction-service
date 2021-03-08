package com.mytrip.attractionservice.internal.service.impl;

import com.mytrip.attractionservice.api.exception.city.CityException;
import com.mytrip.attractionservice.api.exception.city.CityNotFound;
import com.mytrip.attractionservice.internal.feign.CityFeignClient;
import com.mytrip.attractionservice.internal.feign.model.city.City;
import com.mytrip.attractionservice.internal.feign.model.city.CityResponse;
import com.mytrip.attractionservice.internal.feign.model.city.CityResponseList;
import com.mytrip.attractionservice.internal.model.Location;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CityServiceImplTest {

    private static final String CITY_NAME = "Warsaw";
    private static final String GEOS = "geos";
    private static final String KEY = "testKey" ;

    @InjectMocks
    private CityServiceImpl cityService;

    @Mock
    CityFeignClient cityFeignClient;

    @Mock
    private Function<City, Location> cityMapper;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(this.cityService, "KEY", KEY);
    }

    @Test
    public void getCityByName(){
        CityResponseList expectedResponseList = generateCityResponseList();
        when(this.cityFeignClient.getLocationByName(anyString(), anyString())).thenReturn(Optional.of(expectedResponseList));

        Set<Location> cities = cityService.getCityByName(CITY_NAME);

        assertEquals(1, cities.size());

        verify(cityMapper).apply(any());
        verify(cityFeignClient).getLocationByName(KEY, CITY_NAME);
    }

    @Test
    public void getCityByNameWhenApiReturnedNotCity(){
        CityResponseList expectedResponseList = generateCityResponseListWithCityAndRestaurant();
        when(this.cityFeignClient.getLocationByName(anyString(), anyString())).thenReturn(Optional.of(expectedResponseList));

        Set<Location> cities = cityService.getCityByName(CITY_NAME);

        assertEquals(1, cities.size());

        verify(cityMapper).apply(any());
        verify(cityFeignClient).getLocationByName(KEY, CITY_NAME);
    }

    @Test
    public void getAttractionsByCityIdWhenFeignException(){
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

    private CityResponseList generateCityResponseList() {
        CityResponseList cityResponseList = new CityResponseList();
        cityResponseList.setData(generateData());
        return cityResponseList;
    }

    private CityResponseList generateCityResponseListWithCityAndRestaurant() {
        CityResponseList cityResponseList = new CityResponseList();
        cityResponseList.setData(generateDataWithCityAndRestaurant());
        return cityResponseList;
    }

    private List<CityResponse> generateDataWithCityAndRestaurant() {
        City warsaw = generateWarsaw();
        CityResponse cityResponse = new CityResponse(GEOS, warsaw);
        City restaurant = generateRestaurant();
        CityResponse restaurantResponse = new CityResponse("restaurant", restaurant);
        return List.of(cityResponse, restaurantResponse);
    }

    private List<CityResponse> generateData() {
        City warsaw = generateWarsaw();
        CityResponse cityResponse = new CityResponse(GEOS, warsaw);
        return List.of(cityResponse);
    }

    private City generateWarsaw() {
        City city = new City();
        city.setName("Warsaw");
        city.setTimezone("Europe/Warsaw");
        city.setLocation("Warsaw, Poland");
        city.setLocationId("274856");
        city.setLatitude("52.229263");
        city.setLongitude("21.01181");
        return city;
    }

    private City generateRestaurant() {
        City restaurant = new City();
        restaurant.setName("restaurant");
        restaurant.setTimezone("Europe/Warsaw");
        restaurant.setLocation("Warsaw, Poland");
        restaurant.setLocationId("274856");
        restaurant.setLatitude("52.229263");
        restaurant.setLongitude("21.01181");
        return restaurant;
    }

    private Request generateRequest() {
        Request request = Request.create(Request.HttpMethod.GET, "test", new HashMap<>(), (byte[]) null, null);
        return request;
    }
}