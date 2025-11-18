package com.codegym.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Đơn thuê bị tố cáo
    @ManyToOne
    @JoinColumn(name = "hire_session_id")
    private HireSession hireSession;

    // Người CCDV báo cáo
    @ManyToOne
    @JoinColumn(name = "ccdv_id")
    private User ccdv;

    // Khách hàng bị báo cáo
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "report_content", nullable = false, columnDefinition = "TEXT")
    private String reportContent;

    private LocalDateTime createdAt;
}
