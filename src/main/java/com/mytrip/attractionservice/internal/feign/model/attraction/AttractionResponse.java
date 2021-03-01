package com.mytrip.attractionservice.internal.feign.model.attraction;

import lombok.*;


@Data
@Setter
@ToString
@EqualsAndHashCode
public class AttractionResponse {

    private String location_id;
    private String name;
    private String latitude;
    private String longitude;
    private String website;

}
