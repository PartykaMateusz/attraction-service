package com.mytrip.attractionservice.internal.feign.model.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mytrip.attractionservice.internal.feign.model.shared.AncestorResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@Setter
@ToString
@EqualsAndHashCode
public class AutoComplete {

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
