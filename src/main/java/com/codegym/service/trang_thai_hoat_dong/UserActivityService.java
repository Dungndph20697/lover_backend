package com.codegym.service.trang_thai_hoat_dong;

import java.util.Map;
import java.util.List;

public interface UserActivityService {

    Map<String, Object> getUserStatus(Long userId);

    List<Map<String, Object>> getOnlineUsers();

    Map<String, Object> getSummary();
}
