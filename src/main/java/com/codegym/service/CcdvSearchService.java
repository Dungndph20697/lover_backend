package com.codegym.service;

import com.codegym.dto.CcdvSearchRequest;
import com.codegym.model.CcdvProfile;
import com.codegym.repository.CcdvProfileRepository;
import com.codegym.specification.CcdvProfileSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CcdvSearchService {
    @Autowired
    private CcdvProfileRepository repository;

    public Page<CcdvProfile> search(CcdvSearchRequest request) {
        Specification<CcdvProfile> specification =
                CcdvProfileSpecs.nameContains(request.getName())
                .and(CcdvProfileSpecs.genderEquals(request.getGender()))
                .and(CcdvProfileSpecs.cityEquals(request.getCity()))
                .and(CcdvProfileSpecs.ageBetween(request.getAgeFrom(), request.getAgeTo()));

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                buildSort(request.getSort())
        );

        return repository.findAll(specification, pageable);
    }

    private Sort buildSort(String sortParam) {
        if (sortParam == null) return Sort.unsorted();

        switch (sortParam) {
            case "view_desc": return Sort.by("user.viewCount").descending();
            case "view_asc": return Sort.by("user.viewCount").ascending();
            case "hire_desc": return Sort.by("hireCount").descending();
            case "hire_asc": return Sort.by("hireCount").ascending();
        }
        return Sort.unsorted();
    }

    public Object getDetailByCcdvId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("CCDV not found"));
    }
}
