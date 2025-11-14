package com.codegym.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.codegym.dto.CcdvProfileDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.User;
import com.codegym.model.enums.ProfileStatus;
import com.codegym.repository.CcdvProfileRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.interfaceService.CcdvProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class CcdvProfileServiceImpl implements CcdvProfileService {

    @Autowired
    private CcdvProfileRepository ccdvProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

//    private final String uploadDir = "C:\\Users\\duytr\\Downloads\\upload";

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
        profile.setAvatar(uploadFile(dto.getAvatar()));
        profile.setPortrait1(uploadFile(dto.getPortrait1()));
        profile.setPortrait2(uploadFile(dto.getPortrait2()));
        profile.setPortrait3(uploadFile(dto.getPortrait3()));

        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User không tồn tại với id: " + dto.getUserId()));
        profile.setUser(user);

        // Giá trị mặc định
        profile.setJoinDate(LocalDateTime.now());
        profile.setHireCount(0);

        return ccdvProfileRepository.save(profile);
    }

    @Override
    public CcdvProfile findByUserId(Long userId) {
        return ccdvProfileRepository.findByUserId(userId);
    }

    @Override
    public CcdvProfile updateProfile(Long profileId, CcdvProfileDTO dto) throws IOException {
        CcdvProfile existing = ccdvProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile không tồn tại"));

        // Text
        if (dto.getFullName() != null && !dto.getFullName().isBlank()) existing.setFullName(dto.getFullName());
        if (dto.getGender() != null && !dto.getGender().isBlank()) existing.setGender(dto.getGender());
        if (dto.getCity() != null && !dto.getCity().isBlank()) existing.setCity(dto.getCity());
        if (dto.getNationality() != null && !dto.getNationality().isBlank()) existing.setNationality(dto.getNationality());
        if (dto.getHobbies() != null && !dto.getHobbies().isBlank()) existing.setHobbies(dto.getHobbies());
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) existing.setDescription(dto.getDescription());
        if (dto.getRequirement() != null && !dto.getRequirement().isBlank()) existing.setRequirement(dto.getRequirement());
        if (dto.getFacebookLink() != null && !dto.getFacebookLink().isBlank()) existing.setFacebookLink(dto.getFacebookLink());

        // Numeric
        if (dto.getYearOfBirth() != null) existing.setYearOfBirth(dto.getYearOfBirth());
        if (dto.getHeight() != null) existing.setHeight(dto.getHeight());
        if (dto.getWeight() != null) existing.setWeight(dto.getWeight());

        // Files
        if (dto.getAvatar() != null && !dto.getAvatar().isEmpty()) existing.setAvatar(uploadFile(dto.getAvatar()));
        if (dto.getPortrait1() != null && !dto.getPortrait1().isEmpty()) existing.setPortrait1(uploadFile(dto.getPortrait1()));
        if (dto.getPortrait2() != null && !dto.getPortrait2().isEmpty()) existing.setPortrait2(uploadFile(dto.getPortrait2()));
        if (dto.getPortrait3() != null && !dto.getPortrait3().isEmpty()) existing.setPortrait3(uploadFile(dto.getPortrait3()));

        return ccdvProfileRepository.save(existing);
    }

    private String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));

        return (String) uploadResult.get("secure_url");
    }

    // Chuyển trạng thái ACTIVE <-> INACTIVE

    public CcdvProfile toggleStatus(Long userId) {
        CcdvProfile profile = ccdvProfileRepository.findByUserId(userId);
        if (profile == null) {
            throw new RuntimeException("Không tìm thấy hồ sơ CCDV của userId: " + userId);
        }
        if (profile.getStatus() == ProfileStatus.ACTIVE) {
            profile.setStatus(ProfileStatus.INACTIVE);
        } else {
            profile.setStatus(ProfileStatus.ACTIVE);
        }
        return ccdvProfileRepository.save(profile);
    }
}

