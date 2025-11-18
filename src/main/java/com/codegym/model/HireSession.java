package com.codegym.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "hire_sessions")
public class HireSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người thuê
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // CCDV được thuê
    @ManyToOne
    @JoinColumn(name = "ccdv_id", nullable = false)
    private User ccdv;

//    @ManyToOne
//    @JoinColumn(name = "service_id", nullable = false)
//    private ServiceType serviceType;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    private String status;

    @Column(name = "total_price")
    private Double totalPrice;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Thêm trường mới
    private String address; // Địa chỉ nơi cung cấp dịch vụ
    private String userReport; // Báo cáo từ người thuê về buổi thuê

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Thời gian cập nhật cuối cùng

}
