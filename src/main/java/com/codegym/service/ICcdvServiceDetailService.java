package com.codegym.service;

import com.codegym.model.CcdvServiceDetail;

import java.util.List;

public interface ICcdvServiceDetailService {
    void saveServicesForUser(Long userId, List<Long> serviceIds);

    List<CcdvServiceDetail> getServicesByUser(Long userId);
}
