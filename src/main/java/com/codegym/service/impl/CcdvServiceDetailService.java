package com.codegym.service.impl;

import com.codegym.model.CcdvServiceDetail;
import com.codegym.model.ServiceType;
import com.codegym.model.User;
import com.codegym.repository.CcdvServiceDetailRepository;
import com.codegym.repository.ServiceTypeRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.ICcdvServiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CcdvServiceDetailService implements ICcdvServiceDetailService {

    @Autowired
    private CcdvServiceDetailRepository detailRepo;

    @Autowired
    private ServiceTypeRepository serviceRepo;

    @Autowired
    private UserRepository userRepo;


    @Override
    public void saveServicesForUser(Long userId, List<Long> serviceIds) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Danh sách dịch vụ mà user hiện có
        List<CcdvServiceDetail> existingDetails = detailRepo.findByUserId(userId);
        List<Long> existingServiceIds = existingDetails.stream()
                .map(detail -> detail.getServiceType().getId())
                .toList();

        // Danh sách BASIC luôn mặc định giữ lại
        List<ServiceType> basicServices = serviceRepo.findAll()
                .stream()
                .filter(sv -> "BASIC".equalsIgnoreCase(sv.getType()))
                .toList();

        // Danh sách dịch vụ được chọn mới (FREE + EXTENDED)
        List<ServiceType> selectedServices = serviceRepo.findAllById(serviceIds);

        // Gộp tất cả dịch vụ cần có sau khi lưu
        List<Long> finalServiceIds = new java.util.ArrayList<>();
        basicServices.forEach(sv -> finalServiceIds.add(sv.getId()));
        selectedServices.forEach(sv -> finalServiceIds.add(sv.getId()));

        // ✅ Thêm dịch vụ mới chưa có
        for (Long serviceId : finalServiceIds) {
            if (!existingServiceIds.contains(serviceId)) {
                ServiceType st = serviceRepo.findById(serviceId)
                        .orElseThrow(() -> new RuntimeException("Service not found"));
                CcdvServiceDetail detail = new CcdvServiceDetail();
                detail.setServiceType(st);
                detail.setUser(user);
                detail.setTotalPrice(st.getPricePerHour());
                detail.setTimeStart(LocalDateTime.now());
                detail.setTimeEnd(LocalDateTime.now().plusHours(1));
                detailRepo.save(detail);
            }
        }

        // ✅ Xóa dịch vụ mà user bỏ tích (ngoại trừ BASIC)
        for (CcdvServiceDetail detail : existingDetails) {
            Long id = detail.getServiceType().getId();
            if (!finalServiceIds.contains(id)
                    && !"BASIC".equalsIgnoreCase(detail.getServiceType().getType())) {
                detailRepo.delete(detail);
            }
        }
    }

    public List<CcdvServiceDetail> getServicesByUser(Long userId) {
        return detailRepo.findByUserId(userId);
    }

}
