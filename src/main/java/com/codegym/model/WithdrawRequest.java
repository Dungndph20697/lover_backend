package com.codegym.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// lớp yêu cầu rút tiền
@Entity
@Table(name = "withdraw_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Min(1)
    private Double amount;
    private Double fee;
    private Double amountReceived;

    @NotBlank
    private String bankName;
    @NotBlank// Tên ngân hàng
    private String bankAccountNumber;
    @NotBlank// Số tài khoản
    private String bankAccountName;      // Tên chủ tài khoản

    private String otp;
    private String status = "PENDING";

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime otpCreatedAt = LocalDateTime.now();
}
