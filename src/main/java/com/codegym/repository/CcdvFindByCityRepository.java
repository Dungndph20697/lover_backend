package com.codegym.repository;

import com.codegym.model.CcdvProfile;
import com.codegym.model.enums.ProfileStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcdvFindByCityRepository extends JpaRepository<CcdvProfile, Long> {
    List<CcdvProfile> findTop12ByCityAndStatusOrderByJoinDateDesc(
            String city,
            ProfileStatus status,
            Pageable pageable
    );

    // Tìm theo STATUS (không cần city)
    List<CcdvProfile> findTop12ByStatusOrderByJoinDateDesc(
            ProfileStatus status,
            Pageable pageable
    );
}
