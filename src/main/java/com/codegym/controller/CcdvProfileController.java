package com.codegym.controller;

import com.codegym.service.interfaceService.CcdvProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/profiles")
public class CcdvProfileController {

    @Autowired
    private CcdvProfileService ccdvProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ccdvProfileService.findByUserId(id));
    }
}
