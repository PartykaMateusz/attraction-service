package com.mytrip.attractionservice.internal.service;

import com.mytrip.attractionservice.internal.model.Attraction;

import java.util.List;
import java.util.Set;

public interface AttractionService {

    List<Attraction> getAttractionsByCityId(Long cityId);

    Attraction getAttractionsByAttractionId(Long attractionId);

    Set<Attraction> getAttractionsByAttractionName(String attractionName);
}
