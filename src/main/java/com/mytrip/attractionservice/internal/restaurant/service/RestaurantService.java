package com.mytrip.attractionservice.internal.restaurant.service;

import com.mytrip.attractionservice.internal.attraction.model.Attraction;

import java.util.List;

public interface RestaurantService {

    List<Attraction> getRestaurantsByCoordinates(String latitude, String longitude);
}
