package com.codegym.service;

import com.codegym.dto.CcdvProfileDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.User;
import com.codegym.repository.CcdvProfileRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.interfaceService.CcdvProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CcdvProfileServiceImpl implements CcdvProfileService {

    @Autowired
    private CcdvProfileRepository ccdvProfileRepository;

    @Autowired
    private UserRepository userRepository;

    private final String uploadDir = "C:\\Users\\duytr\\Downloads\\upload";

    @Override
    public CcdvProfile saveProfile(CcdvProfileDTO dto) throws IOException {
        CcdvProfile profile = new CcdvProfile();

        // Map dữ liệu text
        profile.setFullName(dto.getFullName());
        profile.setYearOfBirth(dto.getYearOfBirth());
        profile.setGender(dto.getGender());
        profile.setCity(dto.getCity());
        profile.setNationality(dto.getNationality());
        profile.setHeight(dto.getHeight());
        profile.setWeight(dto.getWeight());
        profile.setHobbies(dto.getHobbies());
        profile.setDescription(dto.getDescription());
        profile.setRequirement(dto.getRequirement());
        profile.setFacebookLink(dto.getFacebookLink());

        // Xử lý file
        profile.setAvatar(saveFile(dto.getAvatar()));
        profile.setPortrait1(saveFile(dto.getPortrait1()));
        profile.setPortrait2(saveFile(dto.getPortrait2()));
        profile.setPortrait3(saveFile(dto.getPortrait3()));

        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User không tồn tại với id: " + dto.getUserId()));
        profile.setUser(user);

        // Giá trị mặc định
        profile.setJoinDate(LocalDateTime.now());
        profile.setHireCount(0);

        return ccdvProfileRepository.save(profile);
    }

    private String saveFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(uploadDir + filename);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);
        return "/upload/" + filename;
    }
}

