package com.mytrip.attractionservice.internal.city.service;

import com.mytrip.attractionservice.internal.city.model.City;

import java.util.Set;

public interface CityService {

    Set<City> getCityByName(String cityName);

}
