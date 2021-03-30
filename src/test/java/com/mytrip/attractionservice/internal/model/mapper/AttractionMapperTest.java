package com.mytrip.attractionservice.internal.model.mapper;

import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.feign.model.shared.AncestorResponse;
import com.mytrip.attractionservice.internal.feign.model.shared.SubCategoryResponse;
import com.mytrip.attractionservice.internal.model.Location;
import com.mytrip.attractionservice.internal.service.impl.AttractionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AttractionMapperTest {

    private static final String CITY = "CITY";
    private static final String WARSAW = "warsaw";
    private static final String POLAND = "poland";

    @InjectMocks
    private AttractionMapper attractionMapper;

    private AttractionResponse attractionResponse = this.generateAttractionResponse();


    @Test
    public void apply_validData_withoutCategory(){
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
        return Arrays.asList(this.generateAncestorCity(), this.generateAncestorCountry());
    }

    private AncestorResponse generateAncestorCity() {
        AncestorResponse ancestorResponse = new AncestorResponse();
        ancestorResponse.setName(WARSAW);
        ancestorResponse.setSubcategory(this.generateCitySubcategory());
        return ancestorResponse;
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