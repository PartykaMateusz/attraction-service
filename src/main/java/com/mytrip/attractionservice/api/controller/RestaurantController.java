package com.mytrip.attractionservice.api.controller;

import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("")
    public ResponseEntity<?> getRestaurantsInCity(@RequestParam(name="latitude") String latitude,
                                                  @RequestParam(name="longitude") String longitude) {
        List<AttractionResponse> restaurants = this.restaurantService.getRestaurantsByCoordinates(latitude, longitude);
        return ResponseEntity.ok(restaurants);
    }

}
