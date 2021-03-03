package com.mytrip.attractionservice.internal.model.mapper;

import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.city.City;
import com.mytrip.attractionservice.internal.feign.model.shared.AncestorResponse;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.model.LocationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class CityMapper implements Function<City, Location> {

    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String PROVINCE = "province";
    private static final String REGION = "region";


    @Override
    public Location apply(final City city) {
        Location location = new Location();
        location.setLocationType(LocationType.CITY);
        location.setCity(city.getName());
        location.setLocationId(city.getLocationId());
        location.setLocation(city.getLocation());
        location.setName(city.getName());
        location.setLatitude(city.getLatitude());
        location.setLongitude(city.getLongitude());
        location.setWebsite(city.getWebsite());
        location.setTimezone(city.getTimezone());
        location.setPhone(city.getPhone());
        location.setAddress(city.getAddress());
        this.applyAncestors(location, city.getAncestors());

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
