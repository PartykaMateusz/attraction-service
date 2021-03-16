package com.mytrip.attractionservice.internal.model.mapper;

import com.mytrip.attractionservice.internal.feign.model.city.AutoComplete;
import com.mytrip.attractionservice.internal.feign.model.shared.AncestorResponse;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.model.LocationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class RestaurantMapper implements Function<AutoComplete, Location> {

    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String PROVINCE = "province";
    private static final String REGION = "region";


    @Override
    public Location apply(final AutoComplete autoComplete) {
        Location location = new Location();
        location.setLocationType(LocationType.RESTAURANT);
        location.setCity(autoComplete.getName());
        location.setLocationId(autoComplete.getLocationId());
        location.setLocation(autoComplete.getLocation());
        location.setName(autoComplete.getName());
        location.setLatitude(autoComplete.getLatitude());
        location.setLongitude(autoComplete.getLongitude());
        location.setWebsite(autoComplete.getWebsite());
        location.setTimezone(autoComplete.getTimezone());
        location.setPhone(autoComplete.getPhone());
        location.setAddress(autoComplete.getAddress());
        this.applyAncestors(location, autoComplete.getAncestors());

        return location;
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
