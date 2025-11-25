package com.codegym.service;

import com.codegym.dto.TopCcdvDTO;
import com.codegym.dto.TopViewLoverDTO;
import com.codegym.model.User;
import com.codegym.repository.RoleRepository;
import com.codegym.repository.TopLoverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TopLoverService {
    @Autowired
    private TopLoverRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    public void increaseView(Long id) {
        repository.increaseView(id);
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<TopViewLoverDTO> getTop6CcdvByView() {
//        Long ccdvRoleId = 2L; // role của CCDV:
//        // TODO: lấy role ccdv từ csdl
        Long ccdvRoleId = roleRepository.findByName("SERVICE_PROVIDER")
                .orElseThrow(() -> new RuntimeException("Role not found"))
                .getId();
        return repository.findTopCcdvWithProfile(ccdvRoleId, PageRequest.of(0, 6));
    }
}
