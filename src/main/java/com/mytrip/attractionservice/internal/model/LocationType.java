package com.mytrip.attractionservice.internal.model;

public enum LocationType {

    CITY("city"),
    ATTRACTION("attraction"),
    ROLLUP("rollup"),
    GEOGRAPHIC("geographic"),
    MORE_THAN_ONE_CATEGORY_ERROR("more_than_one_category_error");

    private String value;

    LocationType(final String value) {
        this.value = value;
    }

    public LocationType from(final String type) {
        return LocationType.valueOf(type);
    }

}
