package com.codegym.repository;

import com.codegym.model.CcdvServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CcdvServiceDetailRepository extends JpaRepository<CcdvServiceDetail, Long> {

    List<CcdvServiceDetail> findByUserId(Long userId);
}