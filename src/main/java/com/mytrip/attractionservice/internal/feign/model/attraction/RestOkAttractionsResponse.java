package com.mytrip.attractionservice.internal.feign.model.attraction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RestOkAttractionsResponse {

    @JsonProperty("data")
    private List<AttractionResponse> data = new ArrayList<AttractionResponse>();

    public RestOkAttractionsResponse restOkAttractionsResponse(final List<AttractionResponse> attractionsDataResponseList) {
        this.data = attractionsDataResponseList;
        return this;
    }

    public RestOkAttractionsResponse addDataItem(final AttractionResponse dataItem) {
        this.data.add(dataItem);
        return this;
    }

}
