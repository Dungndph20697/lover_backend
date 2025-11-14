package com.codegym.repository;

import com.codegym.dto.CcdvProfileDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CcdvProfileRepository extends JpaRepository<CcdvProfile, Long> {
    // kiểm tra xem user có profile chưa
    boolean existsByUserId(Long userId);

    // lấy profile theo user id
    CcdvProfile findByUserId(Long userId);

    // lấy danh sách profile dang hoat dong
    List<CcdvProfile> findAllByStatus(ProfileStatus status);

}