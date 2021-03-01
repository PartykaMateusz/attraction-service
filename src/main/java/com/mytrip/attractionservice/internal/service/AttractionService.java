package com.mytrip.attractionservice.internal.service;

import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;

import java.util.List;
import java.util.Set;

public interface AttractionService {

    List<AttractionResponse> getAttractionsByCityId(Long cityId);

    AttractionResponse getAttractionsByAttractionId(Long attractionId);

    Set<AttractionResponse> getAttractionsByAttractionName(String attractionName);
}
