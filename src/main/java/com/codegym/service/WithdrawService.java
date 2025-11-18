package com.codegym.service;

import com.codegym.model.User;
import com.codegym.model.Wallet;
import com.codegym.model.WithdrawRequest;
import com.codegym.repository.WalletRepository;
import com.codegym.repository.WithdrawRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class WithdrawService {

    @Autowired
    private WalletRepository walletRepo;

    @Autowired
    private WithdrawRequestRepository withdrawRepo;

    @Autowired
    private EmailNotificationService mailService;


    // 1) CCDV gửi yêu cầu rút tiền
    public Map<String, Object> createWithdrawRequest(
            User user,
            Double amount,
            String bankName,
            String bankAccountNumber,
            String bankAccountName) {

        Wallet wallet = walletRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (amount <= 0)
            throw new RuntimeException("Số tiền không hợp lệ");

        if (amount > wallet.getBalance())
            throw new RuntimeException("Số dư không đủ để rút");

        double fee = amount * 0.05;
        double amountReceived = amount - fee;

        // tạo OTP
        String otp = String.valueOf((int)(Math.random() * 900000 + 100000));

        WithdrawRequest wr = new WithdrawRequest();
        wr.setUser(user);
        wr.setAmount(amount);
        wr.setFee(fee);
        wr.setAmountReceived(amountReceived);

        wr.setBankName(bankName);
        wr.setBankAccountNumber(bankAccountNumber);
        wr.setBankAccountName(bankAccountName);

        wr.setOtp(otp);
        wr.setOtpCreatedAt(LocalDateTime.now());
        wr.setStatus("PENDING");

        withdrawRepo.save(wr);

        // gửi email OTP
        mailService.sendWithdrawOtp(
                user.getEmail(),
                user.getFirstName(),
                otp
        );

        return Map.of(
                "success", true,
                "message", "OTP đã gửi về email",
                "requestId", wr.getId()
        );
    }


    // 2) CCDV nhập OTP
    public boolean verifyOtp(Long requestId, String otp) {

        WithdrawRequest req = withdrawRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (!req.getStatus().equals("PENDING"))
            throw new RuntimeException("Yêu cầu đã được xử lý hoặc không hợp lệ");

        // kiểm tra OTP còn hạn 5 phút
        if (req.getOtpCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new RuntimeException("OTP đã hết hạn, vui lòng gửi lại");
        }

        if (!req.getOtp().equals(otp))
            return false;

        req.setStatus("OTP_VERIFIED");
        withdrawRepo.save(req);
        return true;
    }


    // 3) CCDV Gửi lại OTP (KHÔNG sửa model)
    public boolean resendOtp(Long requestId) {

        WithdrawRequest req = withdrawRepo.findById(requestId)
                .orElse(null);

        if (req == null) return false;

        // chỉ gửi lại nếu vẫn đang chờ OTP
        if (!req.getStatus().equals("PENDING"))
            return false;

        LocalDateTime now = LocalDateTime.now();

        // chống spam: 30 giây mới được gửi lại
        if (req.getOtpCreatedAt().isAfter(now.minusSeconds(30))) {
            return false;
        }

        // nếu request quá cũ (>10 phút) thì không resend
        if (req.getOtpCreatedAt().isBefore(now.minusMinutes(10))) {
            return false;
        }

        // tạo OTP mới
        String newOtp = String.valueOf((int)(Math.random() * 900000 + 100000));

        req.setOtp(newOtp);
        req.setOtpCreatedAt(now);

        withdrawRepo.save(req);

        // gửi lại OTP
        mailService.sendWithdrawOtp(
                req.getUser().getEmail(),
                req.getUser().getFirstName(),
                newOtp
        );

        return true;
    }

    // 4) ADMIN duyệt rút tiền
    public Map<String, Object> approveWithdraw(Long requestId) {

        WithdrawRequest req = withdrawRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        if (!req.getStatus().equals("OTP_VERIFIED"))
            throw new RuntimeException("Chưa xác minh OTP");

        Wallet wallet = walletRepo.findByUser(req.getUser())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance() < req.getAmount())
            throw new RuntimeException("Số dư không đủ");

        // trừ tiền
        wallet.setBalance(wallet.getBalance() - req.getAmount());
        walletRepo.save(wallet);

        // cập nhật trạng thái
        req.setStatus("APPROVED");
        withdrawRepo.save(req);

        // email thông báo
        mailService.sendWithdrawApproved(
                req.getUser().getEmail(),
                req.getUser().getFirstName(),
                req.getAmount(),
                req.getAmountReceived()
        );

        return Map.of(
                "success", true,
                "message", "Đã duyệt rút tiền"
        );
    }

    // 5) ADMIN từ chối rút tiền
    public Map<String, Object> rejectWithdraw(Long requestId) {

        WithdrawRequest req = withdrawRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu"));

        req.setStatus("REJECTED");
        withdrawRepo.save(req);

        // gửi email thông báo từ chối
        mailService.sendWithdrawRejected(
                req.getUser().getEmail(),
                req.getUser().getFirstName(),
                req.getAmount()
        );

        return Map.of(
                "success", true,
                "message", "Đã từ chối rút tiền"
        );
    }

    // 6) CCDV xem lịch sử rút tiền
    public List<WithdrawRequest> getHistory(User user) {
        return withdrawRepo.findByUserOrderByCreatedAtDesc(user);
    }

    public List<WithdrawRequest> getAdminList() {
        // nếu vẫn muốn list full:
        return withdrawRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    // admin tìm kiếm theo keyword + phân trang cho DataTable
    public Map<String, Object> searchAdmin(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<WithdrawRequest> result;

        if (keyword == null || keyword.isBlank()) {
            result = withdrawRepo.findAllByOrderByCreatedAtDesc(pageable);
        } else {
            result = withdrawRepo.searchAdmin(keyword.trim(), pageable);
        }

        return Map.of(
                "data", result.getContent(),
                "total", result.getTotalElements(),
                "page", result.getNumber(),
                "size", result.getSize()
        );
    }

}
