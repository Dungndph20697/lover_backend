package com.codegym.service.impl;

import com.codegym.model.CcdvServiceDetail;
import com.codegym.model.ServiceType;
import com.codegym.model.User;
import com.codegym.repository.CcdvServiceDetailRepository;
import com.codegym.repository.ServiceTypeRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.ICcdvServiceDetailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CcdvServiceDetailService implements ICcdvServiceDetailService {

    @Autowired
    private CcdvServiceDetailRepository detailRepo;

    @Autowired
    private ServiceTypeRepository serviceRepo;

    @Autowired
    private UserRepository userRepo;

    /**
     * ✅ Lưu danh sách dịch vụ mà user đăng ký (tự động thêm BASIC, tránh trùng lặp)
     */
    @Override
    @Transactional
    public void saveServicesForUser(Long userId, List<Long> serviceIds) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + userId));

        // Danh sách dịch vụ hiện có của user
        List<CcdvServiceDetail> existingDetails = detailRepo.findByUser_Id(userId);
        List<Long> existingServiceIds = existingDetails.stream()
                .map(detail -> detail.getServiceType().getId())
                .toList();

        // ✅ Lấy danh sách dịch vụ BASIC mặc định
        List<ServiceType> basicServices = serviceRepo.findAll()
                .stream()
                .filter(sv -> "BASIC".equalsIgnoreCase(sv.getType()))
                .toList();

        // ✅ Lấy danh sách dịch vụ được chọn (FREE + EXTENDED)
        List<ServiceType> selectedServices = serviceRepo.findAllById(serviceIds);

        // ✅ Gộp danh sách dịch vụ cuối cùng
        List<Long> finalServiceIds = new ArrayList<>();
        basicServices.forEach(sv -> finalServiceIds.add(sv.getId()));
        selectedServices.forEach(sv -> finalServiceIds.add(sv.getId()));

        // ✅ Thêm dịch vụ mới (nếu chưa tồn tại)
        for (Long serviceId : finalServiceIds) {
            if (!detailRepo.existsByUserAndService(userId, serviceId)) {
                ServiceType st = serviceRepo.findById(serviceId)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ với ID: " + serviceId));
                CcdvServiceDetail detail = new CcdvServiceDetail();
                detail.setServiceType(st);
                detail.setUser(user);
                detail.setTotalPrice(st.getPricePerHour());
                detail.setTimeStart(LocalDateTime.now());
                detail.setTimeEnd(LocalDateTime.now().plusHours(1));
                detailRepo.save(detail);
            }
        }

        // ✅ Xóa dịch vụ mà user bỏ tích (trừ BASIC)
        for (CcdvServiceDetail detail : existingDetails) {
            Long id = detail.getServiceType().getId();
            if (!finalServiceIds.contains(id)
                    && !"BASIC".equalsIgnoreCase(detail.getServiceType().getType())) {
                detailRepo.delete(detail);
            }
        }
    }

    /**
     * ✅ Lấy danh sách dịch vụ theo user
     */
    @Override
    public List<CcdvServiceDetail> getServicesByUser(Long userId) {
        return detailRepo.findByUser_Id(userId);
    }

    /**
     * ✅ Cập nhật giá dịch vụ mở rộng hoặc cơ bản
     */

    @Transactional
    public void updateUserServicePrice(Long userId, Long serviceId, BigDecimal newPrice) {
        CcdvServiceDetail detail = detailRepo.findByUser_Id(userId)
                .stream()
                .filter(d -> d.getServiceType().getId().equals(serviceId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ này cho người dùng."));

        String type = detail.getServiceType().getType();

        if ("FREE".equalsIgnoreCase(type)) {
            throw new RuntimeException("Không thể cập nhật giá cho dịch vụ miễn phí!");
        }

        detailRepo.updatePriceByUserAndService(userId, serviceId, newPrice);
    }
}
