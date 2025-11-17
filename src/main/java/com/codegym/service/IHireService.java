package com.codegym.service;

import com.codegym.dto.request.HireRequestDTO;
import com.codegym.model.HireSession;

public interface IHireService {
    HireSession createHire(HireRequestDTO request);
}
