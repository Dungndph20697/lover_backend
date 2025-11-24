package com.codegym.service;

import com.codegym.model.Revenue;
import com.codegym.repository.HireSessionRepository;
import com.codegym.repository.RevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RevenueAdminService {
    private final RevenueRepository revenueRepository;
    private final HireSessionRepository hireSessionRepository;

    public Page<Revenue> getAllRevenues(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return revenueRepository.findAllRevenues(pageable);
    }

    public Double getTotalRevenueByCcdvId(Long ccdvId) {
        System.out.println(">>> Getting total revenue for CCDV ID: " + ccdvId);

        Double total = hireSessionRepository.findAll().stream()
                .filter(h -> h.getCcdv() != null && h.getCcdv().getId().equals(ccdvId))
                .filter(h -> "COMPLETED".equals(h.getStatus()) ||
                        "REPORTED".equals(h.getStatus()) ||
                        "REVIEW_REPORT".equals(h.getStatus()))
                .mapToDouble(h -> h.getTotalPrice() != null ? h.getTotalPrice() : 0)
                .sum();

        System.out.println(">>> Total revenue for CCDV " + ccdvId + ": " + total);

        return total;
    }
}