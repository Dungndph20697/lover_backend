package com.codegym.controller;
import com.codegym.dto.request.HireRequestDTO;
import com.codegym.model.HireSession;
import com.codegym.service.IHireService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hire")
@RequiredArgsConstructor
public class HireController {

    @Autowired
    private IHireService hireService;

    @PostMapping("/create")
    public ResponseEntity<?> createHire(@RequestBody HireRequestDTO request) {
        try {
            HireSession session = hireService.createHire(request);
            return ResponseEntity.ok("Thuê thành công! Tổng tiền: " + session.getTotalPrice() + " VND");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Thuê thất bại: " + e.getMessage());
        }
    }
}
