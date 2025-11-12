package com.codegym.service;

import com.codegym.dto.UpdatePriceRequest;
import com.codegym.model.CcdvServiceOption;
import com.codegym.repository.CcdvServiceOptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CcdvServiceOptionService {
    @Autowired
    private CcdvServiceOptionRepository ccdvServiceOptionRepository;

    @Transactional
    public CcdvServiceOption updatePrice(Long userId, UpdatePriceRequest request) {
        CcdvServiceOption ccdvServiceOption = ccdvServiceOptionRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ này"));

        ccdvServiceOption.setPricePerHour(request.getNewPrice());
        return ccdvServiceOptionRepository.save(ccdvServiceOption);
    }
}