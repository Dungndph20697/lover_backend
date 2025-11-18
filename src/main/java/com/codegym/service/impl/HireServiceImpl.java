package com.codegym.service.impl;

import com.codegym.dto.request.HireRequestDTO;
import com.codegym.model.CcdvServiceDetail;
import com.codegym.model.HireSession;
import com.codegym.model.HireSessionCcdvservicedetail;
import com.codegym.model.User;
import com.codegym.repository.CcdvServiceDetailRepository;
import com.codegym.repository.HireSessionCcdvservicedetailRepository;
import com.codegym.repository.QuanLiDonThueRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.IHireService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HireServiceImpl implements IHireService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HireSessionCcdvservicedetailRepository hireSessionDetailRepository;

    @Autowired
    private QuanLiDonThueRepository hireSessionRepository;

    @Autowired
    private CcdvServiceDetailRepository ccdvServiceDetailRepository;

    @Override
    public HireSession createHire(HireRequestDTO request) {

        // 1. Lấy người đang đăng nhập (người thuê)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng đang đăng nhập"));

        // 2. Lấy người CCDV được thuê
        User provider = userRepository.findById(request.getCcdvId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy CCDV"));

        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime   = request.getEndTime();

        if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
            throw new RuntimeException("Thời gian thuê không hợp lệ");
        }

        // Tính số giờ thuê (có thể lẻ)
        long minutes = Duration.between(startTime, endTime).toMinutes();
        BigDecimal hours = BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        // 3. Lấy danh sách dịch vụ CCDV được chọn
        List<CcdvServiceDetail> details =
                ccdvServiceDetailRepository.findByIdIn(request.getServiceDetailIds());

        if (details.isEmpty()) {
            throw new RuntimeException("Bạn chưa chọn dịch vụ nào.");
        }

        // Kiểm tra tất cả dịch vụ có đúng thuộc CCDV không
        boolean invalidOwner = details.stream()
                .anyMatch(d -> !d.getUser().getId().equals(provider.getId()));

        if (invalidOwner) {
            throw new RuntimeException("Có dịch vụ không thuộc về CCDV này!");
        }

        // 4. Tính tổng tiền = SUM(detail.totalPrice * hours)
        BigDecimal totalMoney = BigDecimal.ZERO;

        for (CcdvServiceDetail detail : details) {
            BigDecimal servicePrice = detail.getTotalPrice();

            if (servicePrice != null && servicePrice.compareTo(BigDecimal.ZERO) > 0) {
                totalMoney = totalMoney.add(servicePrice.multiply(hours));
            }
        }

        // 5. Tạo HireSession chính
        HireSession hire = new HireSession();
        hire.setUser(user);
        hire.setCcdv(provider);
        hire.setStartTime(startTime);
        hire.setEndTime(endTime);
        hire.setAddress(request.getAddress());
        hire.setStatus("PENDING");
        hire.setCreatedAt(LocalDateTime.now());
        hire.setUpdatedAt(LocalDateTime.now());
        hire.setUserReport(null);
        hire.setTotalPrice(totalMoney.doubleValue());

        HireSession savedSession = hireSessionRepository.save(hire);

        // 6. Lưu các dịch vụ được thuê (chỉ lưu liên kết)
        for (CcdvServiceDetail detail : details) {
            HireSessionCcdvservicedetail hd = new HireSessionCcdvservicedetail();
            hd.setHireSession(savedSession);
            hd.setCcdvServiceDetail(detail);
            hireSessionDetailRepository.save(hd);
        }

        return savedSession;
    }
}
