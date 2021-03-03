package com.mytrip.attractionservice.internal.service;

import com.mytrip.attractionservice.internal.model.Location;

import java.util.List;


public interface AttractionService {

    List<Location> getAttractionsByCityId(Long cityId);

    Location getAttractionsByAttractionId(Long attractionId);
}
