package com.codegym.service;

import com.codegym.model.TopupTransaction;
import com.codegym.model.User;
import com.codegym.repository.TopupTransactionRepository;
import com.codegym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SepayService {

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final TopupTransactionRepository transactionRepo;

    public void processTopup(Map<String, Object> payload) {

        // --- Lấy đúng key từ JSON của SePay ---
        Double amount = Double.valueOf(payload.get("transferAmount").toString());
        String content = payload.get("content").toString(); // nội dung chuyển khoản
        String txnId = payload.get("referenceCode").toString(); // mã giao dịch

        System.out.println("Webhook nhận: amount=" + amount + ", content=" + content + ", txn=" + txnId);

        // --- Bỏ qua giao dịch trùng ---
        if (transactionRepo.existsByBankTxnId(txnId)) {
            System.out.println("Giao dịch trùng -> bỏ qua");
            return;
        }

        // --- Tách userId từ nội dung chuyển khoản ---
        Long userId = extractUserId(content);
        if (userId == null) {
            System.out.println("Không tìm thấy mã C0525G1 hoặc extract thất bại trong content: " + content);
            return;
        }

        // --- Kiểm tra user ---
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            System.out.println("User không tồn tại id=" + userId);
            return;
        }

        // --- Cộng ví ---
        walletService.topUp(user, amount);

        // --- Lưu lịch sử giao dịch ---
        TopupTransaction tx = new TopupTransaction();
        tx.setUser(user);
        tx.setAmount(amount);
        tx.setBankTxnId(txnId);
        tx.setContent(content);

        transactionRepo.save(tx);

        System.out.println(">>> NẠP TIỀN THÀNH CÔNG CHO USER ID: " + userId + " (+ " + amount + ")");
    }



    public boolean verifySignature(String rawBody, String signature) {
        // Nếu bạn không dùng gói SePay Pro, Signature luôn rỗng
        return true;
    }

    private Long extractUserId(String content) {
        try {
            // Ví dụ mã có dạng C0525G14
            Pattern pattern = Pattern.compile("C0525G1(\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);

            if (matcher.find()) {
                return Long.parseLong(matcher.group(1)); // lấy số ID phía sau
            }

        } catch (Exception ignored) {}

        return null;
    }

    // QR tĩnh – ai cũng dùng được – không cần API Pro
    public String generateStaticQR(Double amount, Long userId) {
        return "https://img.vietqr.io/image/MB-9704227459-compact.png"
                + "?amount=" + amount
                + "&addInfo=NAPTIEN_USER_" + userId;
    }
}
