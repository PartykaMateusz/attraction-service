package com.mytrip.attractionservice.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mytrip.attractionservice.internal.feign.model.shared.AncestorResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@Setter
@ToString
@EqualsAndHashCode
public class Location {

    @JsonProperty("location_id")
    private String locationId;
    @JsonProperty("location_string")
    private String location;
    private String name;
    private LocationType locationType;
    private String country;
    private String city;
    private String province;
    private String region;
    private String latitude;
    private String longitude;
    private String website;
    private String timezone;
    private String phone;
    private String address;

}
