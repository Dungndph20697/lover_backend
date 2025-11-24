package com.codegym.repository;

import com.codegym.model.ServiceType;
import com.codegym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypeFindByCityRepository extends JpaRepository<ServiceType, Long> {
    List<ServiceType> findByCcdv(User ccdv);
}
