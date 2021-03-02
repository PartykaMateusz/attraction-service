package com.mytrip.attractionservice.internal.feign.model.shared;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@ToString
@EqualsAndHashCode
public class SubCategoryResponse {

    private String key;
    private String name;
}
