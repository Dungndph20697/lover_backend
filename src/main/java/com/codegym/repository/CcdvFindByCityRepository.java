package com.codegym.repository;

import com.codegym.model.CcdvProfile;
import com.codegym.model.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcdvFindByCityRepository extends JpaRepository<CcdvProfile, Long> {
    List<CcdvProfile> findTop12ByCityOrderByJoinDateDesc(String city);
    List<CcdvProfile> findByStatus(ProfileStatus status);
}
