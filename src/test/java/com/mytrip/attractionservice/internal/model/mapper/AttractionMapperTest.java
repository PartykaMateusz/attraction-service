package com.mytrip.attractionservice.internal.model.mapper;

import com.mytrip.attractionservice.internal.feign.model.attraction.AttractionResponse;
import com.mytrip.attractionservice.internal.feign.model.attraction.RestOkAttractionsResponse;
import com.mytrip.attractionservice.internal.feign.model.shared.AncestorResponse;
import com.mytrip.attractionservice.internal.feign.model.shared.SubCategoryResponse;
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

    @InjectMocks
    private AttractionMapper attractionMapper;

    private AttractionResponse attractionResponse = this.generateAttractionResponse();


    @Test
    public void apply_validData(){
        attractionMapper.apply(attractionResponse);
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
        return Arrays.asList(this.generateAncestor());
    }

    private AncestorResponse generateAncestor() {

        AncestorResponse ancestorResponse = new AncestorResponse();
        ancestorResponse.setName(CITY);
        ancestorResponse.setSubcategory(this.generateCitySubcategory());

        return ancestorResponse;
    }

    private List<SubCategoryResponse> generateCitySubcategory() {
        SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
        subCategoryResponse.setKey("city");
        subCategoryResponse.setName("warsaw");
        return Arrays.asList(subCategoryResponse);
    }


}