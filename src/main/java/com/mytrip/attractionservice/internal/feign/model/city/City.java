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
public class City {

    @JsonProperty("location_id")
    private String locationId;
    @JsonProperty("location_string")
    private String location;
    private String name;
    private String latitude;
    private String longitude;
    private String timezone;
}
