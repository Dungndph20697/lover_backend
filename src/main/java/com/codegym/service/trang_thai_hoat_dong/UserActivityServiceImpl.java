package com.codegym.service.trang_thai_hoat_dong;

import com.codegym.model.User;
import com.codegym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {

    private final UserRepository userRepository;

    // Thời gian tính online (5 phút)
    private static final int ONLINE_MINUTES = 5;

    @Override
    public Map<String, Object> getUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        Map<String, Object> response = new HashMap<>();

        String status = calculateStatus(user.getLastActivity());

        response.put("userId", user.getId());
        response.put("fullName", user.getFirstName() + " " + user.getLastName());
        response.put("status", status);
        response.put("lastActivity", user.getLastActivity());
        response.put("lastSeenText", formatLastSeen(user.getLastActivity()));  // ⭐ thêm vào

        return response;
    }

    @Override
    public List<Map<String, Object>> getOnlineUsers() {
        LocalDateTime now = LocalDateTime.now();

        List<User> users = userRepository.findAll();
        List<Map<String, Object>> onlineList = new ArrayList<>();

        for (User u : users) {
            if (u.getLastActivity() == null) continue;

            long diff = Duration.between(u.getLastActivity(), now).toMinutes();

            if (diff <= ONLINE_MINUTES) {
                Map<String, Object> info = new HashMap<>();
                info.put("userId", u.getId());
                info.put("fullName", u.getFirstName() + " " + u.getLastName());
                info.put("lastActivity", u.getLastActivity());
                info.put("lastSeenText", formatLastSeen(u.getLastActivity())); // ⭐ thêm
                onlineList.add(info);
            }
        }

        return onlineList;
    }

    @Override
    public Map<String, Object> getSummary() {
        List<User> users = userRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        long online = users.stream()
                .filter(u -> u.getLastActivity() != null &&
                        Duration.between(u.getLastActivity(), now).toMinutes() <= ONLINE_MINUTES)
                .count();

        long activeToday = users.stream()
                .filter(u -> u.getLastActivity() != null &&
                        u.getLastActivity().toLocalDate().equals(now.toLocalDate()))
                .count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalUsers", users.size());
        summary.put("onlineNow", online);
        summary.put("activeToday", activeToday);

        return summary;
    }

    // ================================================================
    // ⭐ ⭐ ⭐   HELPER METHODS   ⭐ ⭐ ⭐
    // ================================================================

    // Xác định trạng thái từ lastActivity
    private String calculateStatus(LocalDateTime lastActivity) {
        if (lastActivity == null) return "Không hoạt động";

        long diff = Duration.between(lastActivity, LocalDateTime.now()).toMinutes();

        if (diff <= ONLINE_MINUTES) {
            return "Đang hoạt động";
        } else {
            return "Hoạt động " + formatLastSeen(lastActivity);
        }
    }

    // Format thời gian như Facebook / Zalo
    private String formatLastSeen(LocalDateTime lastActivity) {
        if (lastActivity == null) return "Chưa hoạt động";

        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(lastActivity, now).toMinutes();
        long hours = minutes / 60;
        long days = hours / 24;

        if (minutes < 1) return "vừa xong";
        if (minutes < 60) return minutes + " phút trước";
        if (hours < 24) return hours + " giờ trước";
        if (days == 1) return "hôm qua";

        return days + " ngày trước";
    }
}
