package com.mytrip.attractionservice.api.controller;

import com.mytrip.attractionservice.internal.city.model.City;
import com.mytrip.attractionservice.internal.city.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Set;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("")
    public ResponseEntity<?> searchCityByName(@RequestParam(name = "name")
                                                  @Min(value = 3, message = "require 3 or more characters") final String cityName){
        Set<City> cities = cityService.getCityByName(cityName);
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}
