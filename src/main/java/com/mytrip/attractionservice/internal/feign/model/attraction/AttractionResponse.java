package com.mytrip.attractionservice.internal.feign.model.attraction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mytrip.attractionservice.internal.feign.model.shared.AncestorResponse;
import lombok.*;

import java.util.List;


@Data
@Setter
@ToString
@EqualsAndHashCode
public class AttractionResponse {

    @JsonProperty("location_id")
    private String locationId;
    @JsonProperty("location_string")
    private String location;
    private String name;
    private String latitude;
    private String longitude;
    private String website;
    private String timezone;
    private List<AncestorResponse> ancestors;
    private String phone;
    private String address;

}
