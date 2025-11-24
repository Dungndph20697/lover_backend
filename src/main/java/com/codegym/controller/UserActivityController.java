package com.codegym.controller;

import com.codegym.service.trang_thai_hoat_dong.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-activity")
public class UserActivityController {

    @Autowired
    private UserActivityService userActivityService;

    // API lấy trạng thái 1 User
    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(userActivityService.getUserStatus(userId));
    }

    // API danh sách user đang online
    @GetMapping("/online")
    public ResponseEntity<?> getOnlineUsers() {
        return ResponseEntity.ok(userActivityService.getOnlineUsers());
    }

    @GetMapping("/admin/summary")
    public ResponseEntity<?> getSummary() {
        return ResponseEntity.ok(userActivityService.getSummary());
    }




}
