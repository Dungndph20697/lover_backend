package com.codegym.service.interfaceService;

import com.codegym.dto.CcdvProfileDTO;
import com.codegym.model.CcdvProfile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CcdvProfileService {
    CcdvProfile saveProfile(CcdvProfileDTO dto) throws IOException;
}
