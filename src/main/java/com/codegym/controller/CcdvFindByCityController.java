package com.codegym.controller;

import com.codegym.dto.CcdvFindByCity;
import com.codegym.dto.CcdvProfileHomeDTO;
import com.codegym.dto.CcdvSuggestionDTO;
import com.codegym.service.CcdvFindByCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ccdv")
public class CcdvFindByCityController {
    @Autowired
    private CcdvFindByCityService service;

    @GetMapping("/city")
    public ResponseEntity<List<CcdvFindByCity>> getAllCcdv(@RequestParam(required = false) String city) {
        return ResponseEntity.ok(service.getAllActiveCcdv(city));
    }
}