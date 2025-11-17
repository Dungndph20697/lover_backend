package com.codegym.controller;

import com.codegym.dto.CcdvProfileDTO;
import com.codegym.dto.CcdvProfileHomeDTO;
import com.codegym.service.TopTrangChuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class TopTrangChuController {

    @Autowired
    private TopTrangChuService homeService;

    @GetMapping("/top-ccdv")
    public ResponseEntity<List<CcdvProfileHomeDTO>> getTopCcdv() {
        List<CcdvProfileHomeDTO> list = homeService.getTopCcdvDTO();
        return ResponseEntity.ok(list);
    }
}