package com.mytrip.attractionservice.internal.service;

import com.mytrip.attractionservice.internal.model.Location;

import java.util.List;
import java.util.Set;

public interface CityService {

    List<Location> getCityByName(String cityName);

    Location getCityById(String cityId);
}
