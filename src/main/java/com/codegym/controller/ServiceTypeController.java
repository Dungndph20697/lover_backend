package com.codegym.controller;

import com.codegym.model.ServiceType;
import com.codegym.service.IServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ccdv/service-types")
public class ServiceTypeController {
    @Autowired
    private IServiceTypeService serviceTypeService;

    @GetMapping("/find-all")
    public ResponseEntity<List<ServiceType>> findAll(){
        List<ServiceType> serviceTypes = serviceTypeService.findAll();
        return ResponseEntity.ok(serviceTypes);
    }
}
