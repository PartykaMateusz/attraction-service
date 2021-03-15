package com.mytrip.attractionservice.api.controller;

import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;


    @GetMapping("/{restaurantId}")
    public ResponseEntity<?> getRestaurantById(@PathVariable String restaurantId) {
        Location restaurants = this.restaurantService.getRestaurantById(restaurantId);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/city")
    public ResponseEntity<?> getRestaurantsInCity(@RequestParam(name="latitude") String latitude,
                                                  @RequestParam(name="longitude") String longitude) {
        List<Location> restaurants = this.restaurantService.getRestaurantsByCoordinates(latitude, longitude);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/city/{cityName}")
    public ResponseEntity<?> getRestaurantsByCityName(@PathVariable String cityName) {
        List<Location> restaurants = this.restaurantService.getRestaurantsByCity(cityName);
        return ResponseEntity.ok(restaurants);
    }

}
