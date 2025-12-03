package com.codegym.service.filter_service;

import com.codegym.dto.CcdvFilterRequest;
import com.codegym.model.CcdvProfile;

import java.util.List;

public interface CcdvFilterService {
    List<CcdvProfile> filter(CcdvFilterRequest request);
}
