package com.codegym.service;

import com.codegym.model.Role;
import com.codegym.repository.UserRepository;
import com.codegym.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Đăng ký user mới
    public Map<String, Object> register(User user) {
        Map<String, Object> response = new HashMap<>();

        if (userRepository.existsByUsername(user.getUsername())) {
            response.put("success", false);
            response.put("message", "Username đã tồn tại");
            return response;
        }

        // Mặc định là CLIENT nếu không chọn
        if (user.getRole() == null) {
            Role role = new Role();
            role.setId(1L);
            user.setRole(role);
        }

        // Nếu nickname trống → dùng họ + tên
        if (user.getNickname() == null || user.getNickname().isBlank()) {
            user.setNickname(user.getFirstName() + " " + user.getLastName());
        }

        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        savedUser.setPassword(null);

        response.put("success", true);
        response.put("message", "Đăng ký thành công");
        response.put("data", savedUser);
        return response;
    }

    // Kiểm tra username tồn tại
    public boolean checkUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // Kiem tra email tồn tại
    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Kiểm tra phone tồn tại
    public boolean checkPhoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

    // Kiểm tra cccd tồn tại
    public boolean checkCccdExists(String cccd) {
        return userRepository.existsByCccd(cccd);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}