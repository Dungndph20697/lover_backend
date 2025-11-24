package com.codegym.controller;

import com.codegym.dto.CcdvFilterRequest;
import com.codegym.model.CcdvProfile;
import com.codegym.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/providers")
    public ResponseEntity<List<CcdvProfile>> getProviders() {
        List<CcdvProfile> data = recommendationService.getAllActiveProviders();
        return ResponseEntity.ok(data);
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterProfiles(@RequestBody CcdvFilterRequest request) {
        return ResponseEntity.ok(recommendationService.filterProfiles(request));
    }
}
