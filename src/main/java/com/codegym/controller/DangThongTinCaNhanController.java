package com.codegym.controller;

import com.codegym.dto.CcdvProfileDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.service.CcdvProfileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ccdv-profiles")
@CrossOrigin("*")
public class DangThongTinCaNhanController {
    @Autowired
    private CcdvProfileServiceImpl ccdvProfileService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createProfile(@ModelAttribute CcdvProfileDTO dto) {

        if (dto.getFullName() == null || dto.getFullName().isBlank()
                || dto.getYearOfBirth() == null
                || dto.getGender() == null
                || dto.getCity() == null
                || dto.getNationality() == null
                || dto.getAvatar() == null || dto.getAvatar().isEmpty()
                || dto.getPortrait1() == null || dto.getPortrait1().isEmpty()
                || dto.getPortrait2() == null || dto.getPortrait2().isEmpty()
                || dto.getPortrait3() == null || dto.getPortrait3().isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng nhập đầy đủ các trường bắt buộc (*)");
        }

        try {
            CcdvProfile saved = ccdvProfileService.saveProfile(dto);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi khi lưu thông tin");
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getProfileByUserId(@PathVariable("userId") Long userId) {
        try {
            CcdvProfile profile = ccdvProfileService.findByUserId(userId);
            if (profile == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi khi lấy thông tin người dùng");
        }
    }

    @PutMapping(value = "/update/{profileId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateProfile(
            @PathVariable("profileId") Long profileId,
            @ModelAttribute CcdvProfileDTO dto
    ) {
        try {
            CcdvProfile updated = ccdvProfileService.updateProfile(profileId, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi khi cập nhật hồ sơ");
        }
    }
}
