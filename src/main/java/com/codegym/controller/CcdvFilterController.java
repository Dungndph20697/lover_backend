package com.codegym.controller;

import com.codegym.dto.CcdvFilterRequest;
import com.codegym.model.CcdvProfile;
import com.codegym.service.filter_service.CcdvFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/filter")
public class CcdvFilterController {

    @Autowired
    private CcdvFilterService service;

    @PostMapping
    public ResponseEntity<List<CcdvProfile>> filter(@RequestBody CcdvFilterRequest request) {
        return ResponseEntity.ok(service.filter(request));
    }
}