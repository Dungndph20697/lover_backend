package com.codegym.controller;

import com.codegym.model.CcdvServiceDetail;
import com.codegym.service.UserService;
import com.codegym.service.impl.CcdvServiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ccdv/ccdv-service-details")
public class CcdvServiceDetailController {

    @Autowired
    private CcdvServiceDetailService serviceDetailService;

    @Autowired
    private UserService userService;

    /**
     * 📦 Lưu danh sách dịch vụ cho 1 user (bao gồm cả BASIC mặc định)
     */
    @PostMapping("/save/{userId}")
    public ResponseEntity<?> saveUserServices(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> payload) {
        try {
            List<Long> serviceIds = ((List<?>) payload.get("serviceIds"))
                    .stream()
                    .map(id -> Long.valueOf(id.toString()))
                    .toList();

            serviceDetailService.saveServicesForUser(userId, serviceIds);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("✅ Dịch vụ đã được lưu thành công (bao gồm cả BASIC mặc định).");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("❌ Lỗi khi lưu dịch vụ: " + e.getMessage());
        }
    }

    /**
     * 🔍 Lấy danh sách dịch vụ mà user đã đăng ký
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserServices(@PathVariable Long userId) {
        try {
            List<CcdvServiceDetail> details = serviceDetailService.getServicesByUser(userId);
            if (details.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("❌ Lỗi khi lấy danh sách dịch vụ: " + e.getMessage());
        }
    }

    /**
     * 💰 Cập nhật giá dịch vụ mở rộng và basic cho user cụ thể
     */
    @PutMapping("/update-price")
    public ResponseEntity<?> updatePrice(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            Long serviceId = Long.valueOf(payload.get("serviceId").toString());
            BigDecimal price = new BigDecimal(payload.get("price").toString());

            serviceDetailService.updateUserServicePrice(userId, serviceId, price);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("✅ Cập nhật giá dịch vụ thành công cho người dùng!");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("❌ Lỗi khi cập nhật giá: " + e.getMessage());
        }
    }
}
