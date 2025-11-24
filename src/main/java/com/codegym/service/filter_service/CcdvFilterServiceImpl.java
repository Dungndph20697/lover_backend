package com.codegym.service.filter_service;

import com.codegym.dto.CcdvFilterRequest;
import com.codegym.model.CcdvProfile;
import com.codegym.repository.CcdvFilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CcdvFilterServiceImpl implements CcdvFilterService {

    @Autowired
    private CcdvFilterRepository repository;

    @Override
    public List<CcdvProfile> filter(CcdvFilterRequest r) {

        return repository.searchProfiles(
                r.getName(),
                r.getGender(),     // chỉ cần bạn gửi "MALE" hoặc "FEMALE"
                r.getMinAge(),
                r.getMaxAge(),
                r.getAddress()
        );
    }
}