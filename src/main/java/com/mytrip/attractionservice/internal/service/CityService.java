package com.mytrip.attractionservice.internal.service;

import com.mytrip.attractionservice.internal.model.Location;

import java.util.Set;

public interface CityService {

    Set<Location> getCityByName(String cityName);

}
