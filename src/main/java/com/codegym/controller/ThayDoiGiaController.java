package com.codegym.controller;

import com.codegym.dto.UpdatePriceRequest;
import com.codegym.model.CcdvServiceOption;
import com.codegym.service.CcdvServiceOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ccdv")
public class ThayDoiGiaController {
    @Autowired
    private CcdvServiceOptionService ccdvServiceOptionService;

    @PutMapping("/price")
    public ResponseEntity<?> updatePrice(@RequestHeader(value = "userId", required = false) Long userId, @RequestBody UpdatePriceRequest request) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("Thiếu header userId");
        }

        CcdvServiceOption updated = ccdvServiceOptionService.updatePrice(userId, request);
        return ResponseEntity.ok(updated);
    }
}