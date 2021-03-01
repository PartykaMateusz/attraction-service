package com.mytrip.attractionservice.internal.city.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@ToString
@EqualsAndHashCode
public class CityResponse {

    @JsonProperty("result_type")
    private String resultType;

    @JsonProperty("result_object")
    private City resultObject;

    public CityResponse() {
    }

    public CityResponse(final String resultType, final City resultObject) {
        this.resultType = resultType;
        this.resultObject = resultObject;
    }
}
