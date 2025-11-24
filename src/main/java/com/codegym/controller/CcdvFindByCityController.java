package com.codegym.controller;

import com.codegym.dto.CcdvFindByCity;
import com.codegym.dto.CcdvProfileHomeDTO;
import com.codegym.service.CcdvFindByCityService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<CcdvFindByCity> getAllCcdv() {
        return service.getAllActiveCcdv();
    }
}
