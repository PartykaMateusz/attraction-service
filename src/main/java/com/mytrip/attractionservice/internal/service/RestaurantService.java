package com.mytrip.attractionservice.internal.service;

import com.mytrip.attractionservice.internal.model.Location;

import java.util.List;

public interface RestaurantService {

    List<Location> getRestaurantsByCoordinates(String latitude, String longitude);

    List<Location> getRestaurantsByCity(String cityName);
}
