package com.mytrip.attractionservice.internal.feign.model.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@ToString
@EqualsAndHashCode
public class AutoCompleteResponse {

    @JsonProperty("result_type")
    private String resultType;

    @JsonProperty("result_object")
    private AutoComplete resultObject;

    public AutoCompleteResponse() {
    }

    public AutoCompleteResponse(final String resultType, final AutoComplete resultObject) {
        this.resultType = resultType;
        this.resultObject = resultObject;
    }
}
