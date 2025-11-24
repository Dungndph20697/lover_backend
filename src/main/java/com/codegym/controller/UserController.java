package com.codegym.controller;

import com.codegym.dto.TopCcdvDTO;

import com.codegym.model.CcdvServiceDetail;

import com.codegym.model.CcdvServiceDetail;
import com.codegym.model.User;
import com.codegym.service.JwtService;
import com.codegym.service.UserService;
import com.codegym.service.impl.CcdvServiceDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private CcdvServiceDetailService serviceDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");


        User user = userService.findUserByUsername(username).get();
        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            System.out.println("aaaaaaaaaaaaaaa"+user.getStatus());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", "Tài khoản đã bị khóa, vui lòng liên hệ Admin!"));
        }


        // Kiểm tra username có tồn tại không
        User user1 = userService.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Sai username hoặc password"));

        // Kiểm tra đã được Admin duyệt chưa
        if (user1.getIsActive() == null || !user.getIsActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "success", false,
                            "message", "Tài khoản chưa được Admin duyệt"
                    ));
        }

        // 3️⃣ Xác thực mật khẩu
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // 4️⃣ Tạo JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtService.generateToken(username);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", token,
                "username", username
        ));
    }

    // Đăng ký
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody User user) {
        Map<String, Object> response = userService.register(user);
        return ResponseEntity.ok(response);
    }

    // Kiểm tra email tồn tại
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.checkEmailExists(email);
        return ResponseEntity.ok(exists);
    }

    // Kiểm tra phone tồn tại
    @GetMapping("/check-phone/{phone}")
    public ResponseEntity<Boolean> checkPhoneExists(@PathVariable String phone) {
        boolean exists = userService.checkPhoneExists(phone);
        return ResponseEntity.ok(exists);
    }

    // Kiểm tra CCCD tồn tại
    @GetMapping("/check-cccd/{cccd}")
    public ResponseEntity<Boolean> checkCccdExists(@PathVariable String cccd) {
        boolean exists = userService.checkCccdExists(cccd);
        return ResponseEntity.ok(exists);
    }

    // Kiểm tra username tồn tại
    @GetMapping("/exists/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userService.checkUsernameExists(username);
        return ResponseEntity.ok(exists);
    }

    //

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        String username = authentication.getName();

        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        //  Nếu user chưa có mã nạp tiền → tạo ngay
        if (user.getTopupCode() == null || user.getTopupCode().trim().isEmpty()) {
            String topupCode = "C0525G1" + user.getId();  // Mã gọn nhẹ
            user.setTopupCode(topupCode);
            userService.save(user); // Lưu lại DB
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/service/{userId}")
    public ResponseEntity<?> getUserServices(@PathVariable Long userId) {
        try {
            List<CcdvServiceDetail> details = serviceDetailService.getServicesByUser(userId);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("❌ Lỗi khi lấy danh sách dịch vụ: " + e.getMessage());
        }
    }
}