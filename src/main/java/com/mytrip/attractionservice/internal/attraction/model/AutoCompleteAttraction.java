package com.mytrip.attractionservice.internal.attraction.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@ToString
@EqualsAndHashCode
public class AutoCompleteAttraction {

    @JsonProperty("result_object")
    private Attraction resultObject;

    public AutoCompleteAttraction() {
    }

    public AutoCompleteAttraction(Attraction resultObject) {
        this.resultObject = resultObject;
    }
}
