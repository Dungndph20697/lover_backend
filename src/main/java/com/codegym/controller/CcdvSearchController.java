package com.codegym.controller;

import com.codegym.dto.CcdvSearchRequest;
import com.codegym.repository.CcdvProfileRepository;
import com.codegym.service.CcdvSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ccdv")
public class CcdvSearchController {
    @Autowired
    private CcdvSearchService ccdvSearchService;

    @Autowired
    private CcdvProfileRepository ccdvProfileRepository;

    @PostMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> search(@RequestBody CcdvSearchRequest request) {
        return ResponseEntity.ok(ccdvSearchService.search(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCcdvDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ccdvSearchService.getDetailByCcdvId(id));
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCities() {
        List<String> cities = ccdvProfileRepository.findDistinctCities();
        return ResponseEntity.ok(cities);
    }
}
