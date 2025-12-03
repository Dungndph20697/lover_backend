package com.codegym.service.impl;

import com.codegym.dto.request.HireRequestDTO;
import com.codegym.model.*;
import com.codegym.repository.*;
import com.codegym.service.IHireService;
import jakarta.transaction.Transactional;
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
@Transactional
public class HireServiceImpl implements IHireService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HireSessionCcdvservicedetailRepository hireSessionDetailRepository;

    @Autowired
    private QuanLiDonThueRepository hireSessionRepository;

    @Autowired
    private CcdvServiceDetailRepository ccdvServiceDetailRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Override

    public HireSession createHire(HireRequestDTO request) {

        // 1. Lấy người thuê
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // 2. Lấy CCDV
        User provider = userRepository.findById(request.getCcdvId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy CCDV"));

        // 3. Tính số giờ thuê
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();

        if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
            throw new RuntimeException("Thời gian thuê không hợp lệ");
        }

        long minutes = Duration.between(startTime, endTime).toMinutes();
        BigDecimal hours = BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        // 4. Lấy danh sách dịch vụ
        List<CcdvServiceDetail> details =
                ccdvServiceDetailRepository.findByIdIn(request.getServiceDetailIds());

        if (details.isEmpty()) {
            throw new RuntimeException("Bạn chưa chọn dịch vụ nào.");
        }

        boolean invalidOwner = details.stream()
                .anyMatch(d -> !d.getUser().getId().equals(provider.getId()));

        if (invalidOwner) {
            throw new RuntimeException("Có dịch vụ không thuộc về CCDV này!");
        }

        // 5. Tính tiền
        BigDecimal totalMoney = BigDecimal.ZERO;

        for (CcdvServiceDetail detail : details) {
            BigDecimal price = detail.getTotalPrice();
            if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
                totalMoney = totalMoney.add(price.multiply(hours));
            }
        }

        double totalPrice = totalMoney.doubleValue();

        // -----------------------
        // 💰 XỬ LÝ TIỀN
        // -----------------------

        // Lấy ví người thuê
        Wallet userWallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Ví người thuê không tồn tại"));

        // Kiểm tra số dư
        if (userWallet.getBalance() < totalPrice) {
            throw new RuntimeException("Số dư ví không đủ. Vui lòng nạp thêm tiền.");
        }

        // Trừ tiền user
        userWallet.setBalance(userWallet.getBalance() - totalPrice);
        walletRepository.save(userWallet);

        // Cộng tiền CCDV
        Wallet providerWallet = walletRepository.findByUser(provider)
                .orElseThrow(() -> new RuntimeException("Ví CCDV không tồn tại"));

        providerWallet.setBalance(providerWallet.getBalance() + totalPrice);
        walletRepository.save(providerWallet);

        // -----------------------
        // 📝 LƯU THUÊ
        // -----------------------

        HireSession hire = new HireSession();
        hire.setUser(user);
        hire.setCcdv(provider);
        hire.setStartTime(startTime);
        hire.setEndTime(endTime);
        hire.setStatus("PENDING");
        hire.setAddress(request.getAddress());
        hire.setTotalPrice(totalPrice);
        hire.setCreatedAt(LocalDateTime.now());
        hire.setUpdatedAt(LocalDateTime.now());

        HireSession savedSession = hireSessionRepository.save(hire);

        for (CcdvServiceDetail d : details) {
            HireSessionCcdvservicedetail link = new HireSessionCcdvservicedetail();
            link.setHireSession(savedSession);
            link.setCcdvServiceDetail(d);
            hireSessionDetailRepository.save(link);
        }

        return savedSession;
    }
}
