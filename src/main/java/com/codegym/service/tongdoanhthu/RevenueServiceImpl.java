package com.codegym.service.tongdoanhthu;

import com.codegym.model.User;
import com.codegym.repository.TongDoanhThuRepository;
import com.codegym.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
public class RevenueServiceImpl implements RevenueService {
    @Autowired
    private UserService userService;

    @Autowired
    private TongDoanhThuRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RevenueServiceImpl(UserService userService,
                              TongDoanhThuRepository hireSessionRepository,
                              PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.repository = hireSessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean verifyPassword(String username, String rawPassword) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public Double revenueToday(String username) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);

        return repository.sumRevenueByCcdvAndDateRange(user.getId(), start, end);
    }

    @Override
    public Double revenueThisMonth(String username) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        YearMonth ym = YearMonth.now();
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

        return repository.sumRevenueByCcdvAndDateRange(user.getId(), start, end);
    }

    @Override
    public Double revenueInRange(String username, LocalDateTime start, LocalDateTime end) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        return repository.sumRevenueByCcdvAndDateRange(user.getId(), start, end);
    }
}