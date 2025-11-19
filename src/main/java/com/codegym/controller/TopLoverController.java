package com.codegym.controller;

import com.codegym.dto.TopCcdvDTO;
import com.codegym.model.User;
import com.codegym.service.TopLoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/toplover")
@RequiredArgsConstructor
public class TopLoverController {
    @Autowired
    private TopLoverService service;

    // API tăng view CCDV
    @PostMapping("/{id}/view")
    public ResponseEntity<?> increaseView(@PathVariable("id") Long id) {
        service.increaseView(id);
        return ResponseEntity.ok("View updated");
    }

    // API lấy thông tin CCDV (nếu bạn cần)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/top-ccdv-view")
    public ResponseEntity<List<TopCcdvDTO>> getTopCcdvByView() {
        List<TopCcdvDTO> top6 = service.getTop6CcdvByView();
        return ResponseEntity.ok(top6);
    }
}
