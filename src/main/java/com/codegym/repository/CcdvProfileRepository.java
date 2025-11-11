package com.codegym.repository;

import com.codegym.model.CcdvProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CcdvProfileRepository extends JpaRepository<CcdvProfile, Integer> {
    
    // Tìm profile theo user_id
    Optional<CcdvProfile> findByUserId(Long userId);
    
    // Kiểm tra profile đã tồn tại chưa
    boolean existsByUserId(Long userId);
    
    // Tìm theo thành phố
    List<CcdvProfile> findByCity(String city);
    
    // Tìm theo giới tính
    List<CcdvProfile> findByGender(String gender);
    
    // Tìm theo thành phố và giới tính
    List<CcdvProfile> findByCityAndGender(String city, String gender);
    
    // Lấy top CCDV được thuê nhiều nhất
    List<CcdvProfile> findTop10ByOrderByHireCountDesc();
    
    // Tìm CCDV mới tham gia
    List<CcdvProfile> findTop10ByOrderByJoinDateDesc();
}