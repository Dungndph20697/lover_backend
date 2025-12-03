package com.codegym.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "service_types")
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    private BigDecimal pricePerHour;

    // service thuộc về CCDV (User)
    @ManyToOne
    @JoinColumn(name = "ccdv_id")
    private User ccdv;
}
