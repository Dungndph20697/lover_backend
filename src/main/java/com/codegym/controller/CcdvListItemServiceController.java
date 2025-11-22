package com.codegym.controller;

import com.codegym.dto.CcdvListItemServiceDTO;
import com.codegym.service.CcdvListingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ccdv")
public class CcdvListItemServiceController {
    @Autowired
    private CcdvListingItemService service;

    @GetMapping("/list-item")
    public Page<CcdvListItemServiceDTO> list(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size,
            @RequestParam(value = "sort", required = false) String sortParam
    ) {
        Sort sort;
        if (sortParam == null || sortParam.isBlank()) {
            // default: newest (joinDate desc)
            sort = Sort.by(Sort.Direction.DESC, "joinDate");
        } else {
            // simple parsing like ?sort=joinDate,desc or name,asc
            String[] parts = sortParam.split(",");
            if (parts.length == 2) {
                sort = Sort.by(Sort.Direction.fromString(parts[1]), parts[0]);
            } else {
                sort = Sort.by(parts[0]);
            }
        }
        return service.listCcdvIntimateGesture(page, size, sort);
    }
}
