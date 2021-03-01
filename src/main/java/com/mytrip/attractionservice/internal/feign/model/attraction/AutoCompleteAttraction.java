package com.mytrip.attractionservice.internal.feign.model.attraction;

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
    private AttractionResponse resultObject;

    public AutoCompleteAttraction() {
    }

    public AutoCompleteAttraction(AttractionResponse resultObject) {
        this.resultObject = resultObject;
    }
}
