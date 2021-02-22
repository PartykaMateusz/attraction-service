package com.mytrip.attractionservice.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RestOkAttractionsResponse {

    @JsonProperty("data")
    private List<Attraction> data = new ArrayList<Attraction>();

    public RestOkAttractionsResponse restOkAttractionsResponse(final List<Attraction> attractionsDataResponseList) {
        this.data = attractionsDataResponseList;
        return this;
    }

    public RestOkAttractionsResponse addDataItem(final Attraction dataItem) {
        this.data.add(dataItem);
        return this;
    }

}
