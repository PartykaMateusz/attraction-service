package com.mytrip.attractionservice.internal.service;

import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;

import java.util.List;

public interface RestaurantService {

    List<AttractionResponse> getRestaurantsByCoordinates(String latitude, String longitude);
}
