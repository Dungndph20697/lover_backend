package com.codegym.controller;

import com.codegym.dto.CcdvFilterRequest;
import com.codegym.dto.CcdvSuggestGenderDTO;
import com.codegym.dto.CcdvSuggestionDTO;
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
    public ResponseEntity<List<CcdvSuggestGenderDTO>> getProviders(@RequestParam(required = false) String gender) {
        return ResponseEntity.ok(recommendationService.suggestProviders(gender));

    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterProfiles(@RequestBody CcdvFilterRequest request) {
        return ResponseEntity.ok(recommendationService.filterProfiles(request));
    }
}
