package com.mytrip.attractionservice.internal.model.mapper;

import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.shared.AncestorResponse;
import com.mytrip.attractionservice.internal.feign.model.shared.SubCategoryResponse;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.model.LocationType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AttractionMapperTest {

    private static final String CITY = "CITY";
    private static final String WARSAW = "warsaw";
    private static final String POLAND = "poland";
    private static final String SUPER_RESTAURANT = "Super restaurant";

    @InjectMocks
    private AttractionMapper attractionMapper;

    private AttractionResponse attractionResponse = this.generateAttractionResponse();


    @Test
    public void apply_validData_withoutCategory(){
        attractionResponse.setCategory(Collections.EMPTY_MAP);
        Location actualLocation = attractionMapper.apply(attractionResponse);

        assertNotNull(actualLocation);

        assertEquals(attractionResponse.getLocationId(), actualLocation.getLocationId());
        assertNull(actualLocation.getLocationType());
        assertEquals(attractionResponse.getLocation(), actualLocation.getLocation());
        assertEquals(attractionResponse.getName(), actualLocation.getName());
        assertEquals(attractionResponse.getLatitude(), actualLocation.getLatitude());
        assertEquals(attractionResponse.getLongitude(), actualLocation.getLongitude());
        assertEquals(attractionResponse.getWebsite(), actualLocation.getWebsite());
        assertEquals(attractionResponse.getTimezone(), actualLocation.getTimezone());
        assertEquals(attractionResponse.getPhone(), actualLocation.getPhone());
        assertEquals(attractionResponse.getAddress(), actualLocation.getAddress());
        assertEquals(WARSAW, actualLocation.getCity());
        assertEquals(POLAND, actualLocation.getCountry());
    }

    @Test
    public void apply_validData_CategoryIsCity(){
        attractionResponse.setCategory(this.generateCategoryGeographic());
        Location actualLocation = attractionMapper.apply(attractionResponse);

        assertNotNull(actualLocation);

        assertEquals(attractionResponse.getLocationId(), actualLocation.getLocationId());
        assertNotNull(actualLocation.getLocationType());
        assertEquals(attractionResponse.getLocation(), actualLocation.getLocation());
        assertEquals(attractionResponse.getName(), actualLocation.getName());
        assertEquals(attractionResponse.getLatitude(), actualLocation.getLatitude());
        assertEquals(attractionResponse.getLongitude(), actualLocation.getLongitude());
        assertEquals(attractionResponse.getWebsite(), actualLocation.getWebsite());
        assertEquals(attractionResponse.getTimezone(), actualLocation.getTimezone());
        assertEquals(attractionResponse.getPhone(), actualLocation.getPhone());
        assertEquals(attractionResponse.getAddress(), actualLocation.getAddress());
        assertEquals(WARSAW, actualLocation.getCity());
        assertEquals(POLAND, actualLocation.getCountry());
    }

    @Test
    public void apply_validData_CategoryIsRestaurant(){
        AttractionResponse attractionResponse = this.generateAttractionResponse();
        attractionResponse.setCategory(this.generateCategoryRestaurant());
        Location actualLocation = attractionMapper.apply(attractionResponse);

        assertNotNull(actualLocation);

        assertEquals(attractionResponse.getLocationId(), actualLocation.getLocationId());
        assertNotNull(actualLocation.getLocationType());
        assertEquals(LocationType.RESTAURANT, actualLocation.getLocationType());
        assertEquals(attractionResponse.getLocation(), actualLocation.getLocation());
        assertEquals(attractionResponse.getName(), actualLocation.getName());
        assertEquals(attractionResponse.getLatitude(), actualLocation.getLatitude());
        assertEquals(attractionResponse.getLongitude(), actualLocation.getLongitude());
        assertEquals(attractionResponse.getWebsite(), actualLocation.getWebsite());
        assertEquals(attractionResponse.getTimezone(), actualLocation.getTimezone());
        assertEquals(attractionResponse.getPhone(), actualLocation.getPhone());
        assertEquals(attractionResponse.getAddress(), actualLocation.getAddress());
        assertEquals(WARSAW, actualLocation.getCity());
        assertEquals(POLAND, actualLocation.getCountry());
    }

    private Map<String, String> generateCategoryRestaurant() {
        Map<String, String> category = new HashMap<>();
        category.put("key", "restaurant");
        category.put("name", "Restaurant");
        return category;
    }

    private Map<String, String> generateCategoryGeographic() {
        Map<String, String> category = new HashMap<>();
        category.put("key", "geographic");
        category.put("name", "Geographic");
        return category;
    }

    private AttractionResponse generateAttractionResponse() {
        AttractionResponse attraction = new AttractionResponse();
        attraction.setLocationId("4872855");
        attraction.setName("Market Square");
        attraction.setLatitude("50.037384");
        attraction.setLongitude("22.004278");
        attraction.setWebsite("http://www.rzeszow.pl/miasto-rzeszow/historia/zabytki-rzeszowa/rynek");
        attraction.setCategory(Collections.EMPTY_MAP);
        attraction.setAncestors(this.generateAncestors());
        return attraction;
    }

    private List<AncestorResponse> generateAncestors() {
        return Arrays.asList(this.generateAncestorCity(), this.generateAncestorCountry(), this.generateAncestorRestaurant());
    }

    private AncestorResponse generateAncestorCity() {
        AncestorResponse ancestorResponse = new AncestorResponse();
        ancestorResponse.setName(WARSAW);
        ancestorResponse.setSubcategory(this.generateCitySubcategory());
        return ancestorResponse;
    }

    private AncestorResponse generateAncestorRestaurant() {
        AncestorResponse ancestorResponse = new AncestorResponse();
        ancestorResponse.setName(SUPER_RESTAURANT);
        ancestorResponse.setSubcategory(this.generateRestaurantSubcategory());
        return ancestorResponse;
    }

    private List<SubCategoryResponse> generateRestaurantSubcategory() {
        SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
        subCategoryResponse.setKey("restaurant");
        subCategoryResponse.setName("super restaurant");
        return Arrays.asList(subCategoryResponse);
    }

    private List<SubCategoryResponse> generateCitySubcategory() {
        SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
        subCategoryResponse.setKey("city");
        subCategoryResponse.setName("warsaw");
        return Arrays.asList(subCategoryResponse);
    }

    private AncestorResponse generateAncestorCountry() {
        AncestorResponse ancestorResponse = new AncestorResponse();
        ancestorResponse.setName(POLAND);
        ancestorResponse.setSubcategory(this.generateCountrySubcategory());
        return ancestorResponse;
    }

    private List<SubCategoryResponse> generateCountrySubcategory() {
        SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
        subCategoryResponse.setKey("country");
        subCategoryResponse.setName(POLAND);
        return Arrays.asList(subCategoryResponse);
    }


}