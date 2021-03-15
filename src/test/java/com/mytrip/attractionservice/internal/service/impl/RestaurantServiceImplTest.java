package com.mytrip.attractionservice.internal.service.impl;

import com.mytrip.attractionservice.api.exception.city.CityNotFound;
import com.mytrip.attractionservice.api.exception.restaurants.RestaurantClientPackageNotFoundException;
import com.mytrip.attractionservice.api.exception.restaurants.RestaurantException;
import com.mytrip.attractionservice.api.exception.restaurants.RestaurantsNotFound;
import com.mytrip.attractionservice.internal.feign.AttractionFeignClient;
import com.mytrip.attractionservice.internal.feign.RestaurantFeignClient;
import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.feign.model.city.City;
import com.mytrip.attractionservice.internal.feign.model.city.CityResponse;
import com.mytrip.attractionservice.internal.feign.model.city.CityResponseList;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.model.LocationType;
import com.mytrip.attractionservice.internal.service.CityService;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RestaurantServiceImplTest {

    private static final String KEY = "testKey" ;
    private static final String LATITUDE = "50";
    private static final String LONGITUDE = "20";
    private static final String CITY_NAME = "cityName";
    private static final String RESTAURANT_ID = "12";

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Mock
    private RestaurantFeignClient restaurantFeignClient;

    @Mock
    private Function<AttractionResponse, Location> attractionMapper;

    @Mock
    private CityService cityService;

    @Mock
    private AttractionFeignClient attractionClient;

    private List<Location> attractions = this.generateCities();

    private AttractionResponse attractionResponse = this.generateAttraction();

    private Location restaurantLocation = this.generateRestaurantLocation();



    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(this.restaurantService, "KEY", KEY);
        Optional<RestOkAttractionsResponse> attractionsResponse = Optional.of(this.generateAttractions());
        when(restaurantFeignClient.getRestaurantsByCoordinates(KEY, LATITUDE, LONGITUDE)).thenReturn(attractionsResponse);
        when(this.cityService.getCityByName(CITY_NAME)).thenReturn(attractions);
        when(this.attractionClient.getAttractionsByAttractionId(KEY, RESTAURANT_ID)).thenReturn(Optional.of(attractionResponse));
        when(this.attractionMapper.apply(attractionResponse)).thenReturn(this.restaurantLocation);
    }

    @Test
    public void getRestaurantsById(){
        Location location = restaurantService.getRestaurantById(RESTAURANT_ID);
        assertNotNull(location);
        verify(attractionMapper).apply(any());
    }


    @Test
    public void getRestaurantsByCoordinates(){
        List<Location> locations = restaurantService.getRestaurantsByCoordinates(LATITUDE, LONGITUDE);

        assertEquals(1, locations.size());

        verify(attractionMapper).apply(any());
        verify(restaurantFeignClient).getRestaurantsByCoordinates(KEY, LATITUDE, LONGITUDE);
    }

    @Test
    public void getRestaurantsByCoordinatesWhenFeignException(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.NotFound("test", request, null);
        when(restaurantFeignClient.getRestaurantsByCoordinates(KEY, LATITUDE, LONGITUDE)).thenThrow(feignException);
        verify(attractionMapper, never()).apply(any());
        assertThrows(RestaurantsNotFound.class, () -> restaurantService.getRestaurantsByCoordinates(LATITUDE, LONGITUDE));
    }

    @Test
    public void getRestaurantsByCoordinatesWhenFeign500Exception(){
        Request request = generateRequest();
        FeignException feignException = new FeignException.ServiceUnavailable("test", request, null);
        when(restaurantFeignClient.getRestaurantsByCoordinates(KEY, LATITUDE, LONGITUDE)).thenThrow(feignException);
        verify(attractionMapper, never()).apply(any());
        assertThrows(RestaurantException.class, () -> restaurantService.getRestaurantsByCoordinates(LATITUDE, LONGITUDE));
    }

    @Test
    public void getRestaurantsByCity(){
        List<Location> locations = restaurantService.getRestaurantsByCity(CITY_NAME);

        assertEquals(1, locations.size());

        verify(attractionMapper).apply(any());
        verify(restaurantFeignClient).getRestaurantsByCoordinates(KEY, LATITUDE, LONGITUDE);
    }

    @Test
    public void getRestaurantsByCityWhenCityNotFound(){
        when(this.cityService.getCityByName(CITY_NAME)).thenReturn(Collections.emptyList());

        verify(attractionMapper, never()).apply(any());
        verify(restaurantFeignClient, never()).getRestaurantsByCoordinates(KEY, LATITUDE, LONGITUDE);

        assertThrows(CityNotFound.class, () -> restaurantService.getRestaurantsByCity(CITY_NAME));
    }

    private RestOkAttractionsResponse generateAttractions() {
        RestOkAttractionsResponse response = new RestOkAttractionsResponse();
        AttractionResponse attraction = this.generateAttraction();
        response.restOkAttractionsResponse(Collections.singletonList(attraction));
        return response;
    }

    private AttractionResponse generateAttraction() {
        AttractionResponse attraction = new AttractionResponse();
        attraction.setLocationId(RESTAURANT_ID);
        attraction.setName("Compact Okocim Klub");
        attraction.setLatitude("52.229263");
        attraction.setLongitude("21.01181");
        return attraction;
    }

    private Location generateRestaurantLocation() {
        Location restaurant = new Location();
        restaurant.setName("Compact Okocim Klub");
        restaurant.setLocationId(RESTAURANT_ID);
        restaurant.setLatitude("52.229263");
        restaurant.setLongitude("21.01181");
        restaurant.setLocationType(LocationType.RESTAURANT);
        return restaurant;
    }

    private Request generateRequest() {
        Request request = Request.create(Request.HttpMethod.GET, "test", new HashMap<>(), (byte[]) null, null);
        return request;
    }

    private List<Location> generateCities() {
        Location warsaw = generateWarsaw();
        return List.of(warsaw);
    }

    private Location generateWarsaw() {
        Location city = new Location();
        city.setName("Warsaw");
        city.setTimezone("Europe/Warsaw");
        city.setLocation("Warsaw, Poland");
        city.setLocationId("274856");
        city.setLatitude(LATITUDE);
        city.setLongitude(LONGITUDE);
        return city;
    }
}
