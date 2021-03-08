package com.mytrip.attractionservice.internal.service.impl;

import com.mytrip.attractionservice.internal.feign.RestaurantFeignClient;
import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.feign.model.city.CityResponseList;
import com.mytrip.attractionservice.internal.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RestaurantServiceImplTest {

    private static final String KEY = "testKey" ;
    private static final String LATITUDE = "50";
    private static final String LONGITUDE = "20";

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Mock
    private RestaurantFeignClient restaurantFeignClient;

    @Mock
    private Function<AttractionResponse, Location> attractionMapper;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(this.restaurantService, "KEY", KEY);
        Optional<RestOkAttractionsResponse> attractionsResponse = Optional.of(this.generateAttractions());
        when(restaurantFeignClient.getRestaurantsByCoordinates(KEY, LATITUDE, LONGITUDE)).thenReturn(attractionsResponse);
    }

    @Test
    public void getRestaurantsByCoordinates(){
        List<Location> locations = restaurantService.getRestaurantsByCoordinates(LATITUDE, LONGITUDE);

        assertEquals(1, locations.size());

        verify(attractionMapper).apply(any());
        verify(restaurantFeignClient).getRestaurantsByCoordinates(KEY, LATITUDE, LONGITUDE);
    }

    private RestOkAttractionsResponse generateAttractions() {
        RestOkAttractionsResponse response = new RestOkAttractionsResponse();
        AttractionResponse attraction = this.generateAttraction();
        response.restOkAttractionsResponse(Collections.singletonList(attraction));
        return response;
    }

    private AttractionResponse generateAttraction() {
        AttractionResponse attraction = new AttractionResponse();
        attraction.setLocationId("750466");
        attraction.setName("Compact Okocim Klub");
        attraction.setLatitude("49.99999");
        attraction.setLongitude("22.01705");
        return attraction;
    }
}
