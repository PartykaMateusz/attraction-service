package com.mytrip.attractionservice.internal.model.mapper;

import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.shared.AncestorResponse;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.model.LocationType;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class AttractionMapper implements Function<AttractionResponse, Location> {

    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String PROVINCE = "province";
    private static final String REGION = "region";
    private static final String GEOGRAPHIC = "geographic";

    @Override
    public Location apply(final AttractionResponse attractionResponse) {
        Location location = new Location();
        location.setLocationType(this.getLocationType(attractionResponse.getCategory()));
        location.setLocationId(attractionResponse.getLocationId());
        location.setLocation(attractionResponse.getLocation());
        location.setName(attractionResponse.getName());
        location.setLatitude(attractionResponse.getLatitude());
        location.setLongitude(attractionResponse.getLongitude());
        location.setWebsite(attractionResponse.getWebsite());
        location.setTimezone(attractionResponse.getTimezone());
        location.setPhone(attractionResponse.getPhone());
        location.setAddress(attractionResponse.getAddress());
        this.applyAncestors(location, attractionResponse.getAncestors());

        return location;
    }

    private LocationType getLocationType(final Map<String, String> category) {

        if(category.size() < 2) {
            return null;
        }
        if(category.size() == 2) {
            final Collection<String> value = category.values();
            String next = value.iterator().next();
            if (next.equalsIgnoreCase(GEOGRAPHIC)){
                return LocationType.CITY;
            }
            return LocationType.valueOf(value.iterator().next().toUpperCase());
        }

        return LocationType.MORE_THAN_ONE_CATEGORY_ERROR;
    }

    private void applyAncestors(final Location location, final List<AncestorResponse> ancestors) {
        for (AncestorResponse ancestor : ancestors) {
            final String key = ancestor.getSubcategory().get(0).getKey();
            if (key.equals(CITY)) {
                location.setCity(ancestor.getName());
            }
            if (key.equals(COUNTRY)) {
                location.setCountry(ancestor.getName());
            }
            if (key.equals(PROVINCE)) {
                location.setProvince(ancestor.getName());
            }
            if (key.equals(REGION)) {
                location.setRegion(ancestor.getName());
            }
        }
    }
}
