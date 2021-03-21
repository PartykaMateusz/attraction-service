package com.mytrip.attractionservice.api.controller;

import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/name/{cityName}")
    public ResponseEntity<?> searchCityByName(@PathVariable(name = "cityName")
                                                  @Min(value = 3, message = "require 3 or more characters") final String cityName){
        List<Location> cities = cityService.getCityByName(cityName);
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<?> searchCityById(@PathVariable(name = "cityId") final String cityId){
        Location city = cityService.getCityById(cityId);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }
}
