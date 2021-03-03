package com.mytrip.attractionservice.internal.feign.model.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@Setter
@ToString
@EqualsAndHashCode
public class AncestorResponse {

    @JsonProperty("location_id")
    private String locationId;
    private String name;
    private List<SubCategoryResponse> subcategory;

}
