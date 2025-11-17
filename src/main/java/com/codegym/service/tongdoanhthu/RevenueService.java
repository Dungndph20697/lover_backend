package com.codegym.service.tongdoanhthu;

import java.time.LocalDateTime;

public interface RevenueService {

    /**
     * Xác thực mật khẩu của user CCDV
     * @param username tên đăng nhập
     * @param rawPassword mật khẩu nhập từ người dùng
     * @return true nếu đúng, false nếu sai
     */
    boolean verifyPassword(String username, String rawPassword);

    /**
     * Lấy doanh thu của CCDV trong khoảng thời gian
     * @param username tên đăng nhập
     * @return tổng doanh thu
     */
    Double revenueToday(String username);
    Double revenueThisMonth(String username);
    Double revenueInRange(String username, LocalDateTime start, LocalDateTime end);
}