package com.mytrip.attractionservice.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AutoCompleteAttraction {

    @JsonProperty("result_object")
    private List<Attraction> data = new ArrayList<Attraction>();
}
