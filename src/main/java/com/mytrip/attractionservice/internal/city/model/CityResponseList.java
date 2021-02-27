package com.mytrip.attractionservice.internal.city.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mytrip.attractionservice.internal.attraction.model.AutoCompleteAttraction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CityResponseList {

    @JsonProperty("data")
    private List<CityResponse> data = new ArrayList<>();
}
