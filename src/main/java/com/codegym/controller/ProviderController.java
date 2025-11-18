package com.codegym.controller;

import com.codegym.service.interfaceService.CcdvProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/providers")
@RequiredArgsConstructor
public class ProviderController {
///api/user/providers/latest
    private final CcdvProfileService ccdvprofileService;

    @GetMapping("/latest")
    public ResponseEntity<?> getLatest() {
        return ResponseEntity.ok(ccdvprofileService.getLatestProviders(12));
    }



}
