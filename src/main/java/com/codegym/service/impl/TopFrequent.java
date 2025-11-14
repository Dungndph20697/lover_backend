package com.codegym.service.impl;

import com.codegym.dto.TopFrequentCustomerDTO;
import com.codegym.dto.TopRecentCustomerDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.User;
import com.codegym.repository.CcdvProfileRepository;
import com.codegym.repository.QuanLiDonThueRepository;
import com.codegym.service.ITopFrequent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopFrequent implements ITopFrequent {
    @Autowired
    private QuanLiDonThueRepository quanLiDonThueRepository;

    @Autowired
    private CcdvProfileRepository ccdvProfileRepository;

    @Override
    public List<TopFrequentCustomerDTO> getTopFrequentCustomers(Long ccdvId) {
        Pageable limit = PageRequest.of(0, 3);
        return quanLiDonThueRepository.findTopFrequentCustomers(ccdvId, limit);
    }

    @Override
    public List<TopRecentCustomerDTO> getTopRecentCustomers(Long ccdvId) {
        Pageable limit = PageRequest.of(0, 3);
        return quanLiDonThueRepository.findTopRecentCustomers(ccdvId, limit);
    }

    @Override
    public CcdvProfile findFullInfoUser(Long userId) {
        return ccdvProfileRepository.findByUserId( userId );
    }
}
