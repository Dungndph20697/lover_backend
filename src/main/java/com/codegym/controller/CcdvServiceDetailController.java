package com.codegym.controller;

import com.codegym.model.CcdvServiceDetail;
import com.codegym.service.UserService;
import com.codegym.service.impl.CcdvServiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
            @PathVariable("userId") Long userId,
            @RequestBody Map<String, Object> payload) {

        try {
            // ✅ Đảm bảo payload có key "serviceIds"
            if (payload == null || !payload.containsKey("serviceIds")) {
                return ResponseEntity
                        .badRequest()
                        .body("⚠️ Thiếu danh sách serviceIds trong request body!");
            }

            List<Long> serviceIds = ((List<?>) payload.get("serviceIds"))
                    .stream()
                    .map(id -> Long.valueOf(id.toString()))
                    .toList();

            serviceDetailService.saveServicesForUser(userId, serviceIds);

            return ResponseEntity.ok("✅ Dịch vụ đã được lưu thành công (bao gồm cả BASIC mặc định).");
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
    public ResponseEntity<?> getUserServices(@PathVariable("userId") Long userId) {
        try {
            List<CcdvServiceDetail> details = serviceDetailService.getServicesByUser(userId);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("❌ Lỗi khi lấy danh sách dịch vụ: " + e.getMessage());
        }
    }

    /**
     * 💰 Cập nhật giá dịch vụ (BASIC hoặc EXTENDED)
     */
    @PutMapping("/update-price")
    public ResponseEntity<?> updatePrice(@RequestBody Map<String, Object> payload) {
        try {
            // ✅ Kiểm tra payload
            if (payload == null || !payload.containsKey("userId") || !payload.containsKey("serviceId") || !payload.containsKey("price")) {
                return ResponseEntity
                        .badRequest()
                        .body("⚠️ Thiếu thông tin userId, serviceId hoặc price trong request body!");
            }

            Long userId = Long.valueOf(payload.get("userId").toString());
            Long serviceId = Long.valueOf(payload.get("serviceId").toString());
            BigDecimal price = new BigDecimal(payload.get("price").toString());

            serviceDetailService.updateUserServicePrice(userId, serviceId, price);

            return ResponseEntity.ok("✅ Cập nhật giá dịch vụ thành công!");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("❌ Lỗi khi cập nhật giá: " + e.getMessage());
        }
    }
}
