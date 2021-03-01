package com.mytrip.attractionservice.internal.feign.model.attraction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RestOkAutoCompleteResponse {

    @JsonProperty("data")
    private List<AutoCompleteAttraction> data = new ArrayList<>();
}
