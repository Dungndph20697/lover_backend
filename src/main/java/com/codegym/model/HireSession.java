package com.codegym.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

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
    private Integer id;

    // Người thuê
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // CCDV được thuê
    @ManyToOne
    @JoinColumn(name = "ccdv_id", nullable = false)
    private User ccdv;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceType serviceType;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String status;

    private Double totalPrice;
    private LocalDateTime createdAt;

}
