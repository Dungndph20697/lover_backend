package com.codegym.service.interfaceService;

import com.codegym.dto.CcdvProfileDTO;
import com.codegym.dto.CcdvProfileResponse;
import com.codegym.dto.LatestProviderDTO;
import com.codegym.model.CcdvProfile;

import java.io.IOException;
import java.util.List;

public interface CcdvProfileService {
    CcdvProfile findByUserId(Long userId);
    CcdvProfile saveProfile(CcdvProfileDTO dto) throws IOException;
    CcdvProfile updateProfile(Long profileId, CcdvProfileDTO dto) throws IOException;
    List<LatestProviderDTO> getLatestProviders(int limit);
    CcdvProfileResponse getProfileById(Long id);


}
