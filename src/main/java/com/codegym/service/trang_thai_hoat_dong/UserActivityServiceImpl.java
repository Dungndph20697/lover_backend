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

    // User online nếu hoạt động trong 5 phút
    private static final int ONLINE_MINUTES = 5;

    @Override
    public Map<String, Object> getUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        Map<String, Object> response = new HashMap<>();

        String status = getStatusFromActivity(user.getLastActivity());

        response.put("userId", user.getId());
        response.put("fullName", user.getFirstName() + " " + user.getLastName());
        response.put("status", status);
        response.put("lastActivity", user.getLastActivity());

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

    // 👉 Helper method: xác định trạng thái từ thời gian hoạt động
    private String getStatusFromActivity(LocalDateTime lastActivity) {
        if (lastActivity == null) return "Không hoạt động";

        long diff = Duration.between(lastActivity, LocalDateTime.now()).toMinutes();

        if (diff <= ONLINE_MINUTES) {
            return "Đang hoạt động";
        } else {
            return "Không hoạt động";
        }
    }
}
