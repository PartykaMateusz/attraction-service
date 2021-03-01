package com.mytrip.attractionservice.internal.service;

import com.mytrip.attractionservice.internal.feign.model.city.City;

import java.util.Set;

public interface CityService {

    Set<City> getCityByName(String cityName);

}
