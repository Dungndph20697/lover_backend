package com.codegym.service;

import com.codegym.dto.TopCcdvDTO;
import com.codegym.model.User;
import com.codegym.repository.TopLoverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopLoverService {
    @Autowired
    private TopLoverRepository repository;

    public void increaseView(Long id) {
        repository.increaseView(id);
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<TopCcdvDTO> getTop6CcdvByView() {
        Long ccdvRoleId = 2L; // role của CCDV
        return repository.findTopCcdvWithProfile(ccdvRoleId, PageRequest.of(0, 6));
    }
}
