package com.codegym.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "ccdv_service_detail")
public class CcdvServiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_type_id", nullable = false)
    private ServiceType serviceType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Giá dịch vụ không được để trống")
    @DecimalMin(value = "0.00", message = "Giá dịch vụ phải lớn hơn hoặc bằng 10,000₫")
    @DecimalMax(value = "10000000.00", message = "Giá dịch vụ không được vượt quá 10,000,000₫")
    private BigDecimal totalPrice;

    private LocalDateTime timeEnd;

    private LocalDateTime timeStart;

}
