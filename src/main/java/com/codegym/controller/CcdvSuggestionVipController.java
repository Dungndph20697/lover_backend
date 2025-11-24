package com.codegym.controller;

import com.codegym.dto.CcdvSuggestionDTO;
import com.codegym.service.vip_user.CcdvSuggestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ccdv")
public class CcdvSuggestionVipController {
    @Autowired
    private CcdvSuggestionServiceImpl service;

    @GetMapping("/suggestion-vip")
    public ResponseEntity<List<CcdvSuggestionDTO>> getVipSuggestions(@RequestParam(name="limit", defaultValue = "6") int limit) {
        List<CcdvSuggestionDTO> suggestions = service.getVipSuggestions(limit);
        return ResponseEntity.ok(suggestions);
    }
}
