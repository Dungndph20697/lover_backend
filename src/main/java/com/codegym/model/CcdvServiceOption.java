package com.codegym.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "ccdv_service_options")
public class CcdvServiceOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String freeServices;

    private String extendedServices;

    private Double pricePerHour;
    private Float minHours;

}
