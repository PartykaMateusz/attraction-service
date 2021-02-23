package com.mytrip.attractionservice.api.controller;

import com.mytrip.attractionservice.internal.model.Attraction;
import com.mytrip.attractionservice.internal.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/attraction")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    @GetMapping("/city/{cityId}")
    public ResponseEntity<?> getAttractionsByCityId(@PathVariable Long cityId){
        List<Attraction> attractions = attractionService.getAttractionsByCityId(cityId);
        return new ResponseEntity<>(attractions, HttpStatus.OK);
    }

    @GetMapping("/{attractionId}")
    public ResponseEntity<?> getAttractionById(@PathVariable Long attractionId){
        Attraction attractions = attractionService.getAttractionsByAttractionId(attractionId);
        return new ResponseEntity<>(attractions, HttpStatus.OK);
    }

    @GetMapping("/search/{attractionName}")
    public ResponseEntity<?> getAttractionByName(@PathVariable String attractionName){
        Set<Attraction> attractions = attractionService.getAttractionsByAttractionName(attractionName);
        return new ResponseEntity<>(attractions, HttpStatus.OK);
    }
}
