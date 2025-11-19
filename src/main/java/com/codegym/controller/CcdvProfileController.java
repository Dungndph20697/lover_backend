package com.codegym.controller;

import com.codegym.dto.CcdvProfileResponse;
import com.codegym.service.interfaceService.CcdvProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/providers")
@RequiredArgsConstructor
public class CcdvProfileController {
///api/users/providers/{id}

    private final CcdvProfileService profileService;

    @GetMapping("/{id}")
    public ResponseEntity<CcdvProfileResponse> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getProfileById(id));
    }
}
