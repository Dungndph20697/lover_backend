package com.codegym.service.tongdoanhthu;

import com.codegym.model.CcdvProfile;
import com.codegym.model.HireSession;
import com.codegym.model.User;
import com.codegym.repository.CcdvProfileRepository;
import com.codegym.repository.TongDoanhThuRepository;
import com.codegym.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.TreeMap;

@Service
public class RevenueServiceImpl implements RevenueService {
    @Autowired
    private UserService userService;

    @Autowired
    private TongDoanhThuRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CcdvProfileRepository ccdvProfileRepository;

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

    @Override
    public Map<Integer, Double> revenueByWeek(String username, LocalDateTime from, LocalDateTime to) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        List<HireSession> sessions = repository.findDoneSessionsByCcdvInRange(user.getId(), from, to);

        Map<Integer, Double> weekRevenue = new TreeMap<>();
        WeekFields weekFields = WeekFields.ISO; // Monday = 1

        for (HireSession s : sessions) {
            LocalDate date = s.getStartTime().toLocalDate();
            int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
            weekRevenue.merge(weekNumber, s.getTotalPrice(), Double::sum);
        }

        return weekRevenue;
    }

    @Override
    public Map<Integer, Double> revenueByMonth(String username, LocalDateTime from, LocalDateTime to) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        List<HireSession> sessions = repository.findDoneSessionsByCcdvInRange(user.getId(), from, to);

        Map<Integer, Double> monthRevenue = new TreeMap<>();
        for (HireSession s : sessions) {
            int month = s.getStartTime().getMonthValue();
            monthRevenue.merge(month, s.getTotalPrice(), Double::sum);
        }

        return monthRevenue;
    }

    @Override
    public Map<LocalDate, Double> revenueByDay(String username, LocalDateTime start, LocalDateTime end) {
        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        List<HireSession> sessions = repository.findDoneSessionsByCcdvInRangeSafe(user.getId(), start, end);
        System.out.println("Logged in user id = " + user.getId());
        // Group theo ngày kết thúc
        Map<LocalDate, Double> dayRevenue = new TreeMap<>();
        for (HireSession s : sessions) {
            LocalDate date = s.getEndTime().toLocalDate(); // tính theo ngày kết thúc
            dayRevenue.merge(date, s.getTotalPrice(), Double::sum);
        }
        return dayRevenue;
    }
}