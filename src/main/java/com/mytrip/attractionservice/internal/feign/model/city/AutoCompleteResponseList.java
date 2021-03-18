package com.mytrip.attractionservice.internal.feign.model.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AutoCompleteResponseList {

    @JsonProperty("data")
    private List<AutoCompleteResponse> data = new ArrayList<>();
}
