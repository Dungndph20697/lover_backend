package com.codegym.controller.admin;

import com.codegym.model.User;
import com.codegym.repository.UserRepository;
import com.codegym.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor

public class AdminSuccessAccount {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private EmailNotificationService emailNotificationService;

    // lấy danh sách user
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> users = userRepository.findAll(pageable);
        return ResponseEntity.ok(users);
    }




    @PostMapping("/approve/{userId}")
    public ResponseEntity<?> approveUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại với id: " + userId));
        user.setIsActive(true);
        userRepository.save(user);

        // gửi email thông báo
        emailNotificationService.sendAccountApprovedEmail(
                user.getEmail(),
                user.getFirstName()
        );
        return ResponseEntity.ok("Tài khoản đã được phê duyệt và email thông báo đã được gửi.");
    }

}
